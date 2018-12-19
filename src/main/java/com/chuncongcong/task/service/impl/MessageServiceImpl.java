package com.chuncongcong.task.service.impl;

import com.chuncongcong.task.context.RequestContext;
import com.chuncongcong.task.exception.BaseErrorCode;
import com.chuncongcong.task.exception.ServiceException;
import com.chuncongcong.task.manager.MessageBoardManager;
import com.chuncongcong.task.manager.MessageDriftManager;
import com.chuncongcong.task.manager.UsersManager;
import com.chuncongcong.task.model.constants.MessageType;
import com.chuncongcong.task.model.constants.ReadType;
import com.chuncongcong.task.model.constants.SignType;
import com.chuncongcong.task.model.domain.MessageCondition;
import com.chuncongcong.task.model.dto.MessageBoardDTO;
import com.chuncongcong.task.model.dto.MessageDriftDTO;
import com.chuncongcong.task.model.entity.MessageBoard;
import com.chuncongcong.task.model.entity.MessageDrift;
import com.chuncongcong.task.model.entity.Users;
import com.chuncongcong.task.model.vo.MessageBoardVO;
import com.chuncongcong.task.model.vo.MessageDriftVO;
import com.chuncongcong.task.model.vo.PageVO;
import com.chuncongcong.task.service.MessageService;
import com.chuncongcong.task.utils.JsonUtils;
import com.chuncongcong.task.utils.MessageBoardUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Hu
 * @date 2018/8/10 10:23
 */

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageBoardManager messageBoardManager;
    private final MessageDriftManager messageDriftManager;
    private final UsersManager usersManager;

    public MessageServiceImpl(MessageBoardManager messageBoardManager, MessageDriftManager messageDriftManager, UsersManager usersManager) {
        this.messageBoardManager = messageBoardManager;
        this.messageDriftManager = messageDriftManager;
        this.usersManager = usersManager;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object addMessage(MessageBoardDTO messageBoardDTO) throws Exception {

        Users user = RequestContext.getUserInfo();

        if (messageBoardDTO.getToUserIds() == null || StringUtils.isEmpty(messageBoardDTO.getMessage())) {
            throw new ServiceException(BaseErrorCode.PARAM_ILLEGAL);
        }

        MessageBoard messageBoard = new MessageBoard();
        BeanUtils.copyProperties(messageBoard, messageBoardDTO);
        messageBoard.setFromUserId(user.getId());
        messageBoard.setExtFile(JsonUtils.toJSON(messageBoardDTO.getExtFile()));
        //群发
        for (Long userId : messageBoardDTO.getToUserIds()) {
            messageBoard.setToUserId(userId);
            messageBoardManager.addMessage(messageBoard);
        }

        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public Object getMessageList(MessageType messageType, ReadType read, Integer page, Integer pageSize) {

        Users user = RequestContext.getUserInfo();

        MessageCondition messageCondition = new MessageCondition();
        if (messageType.equals(MessageType.RECEIVE) && read.equals(ReadType.UNREAD)) {
            messageCondition.setToUserId(user.getId());
            messageCondition.setRead(ReadType.UNREAD);
        } else if (messageType.equals(MessageType.RECEIVE)) {
            messageCondition.setToUserId(user.getId());
        } else if (messageType.equals(MessageType.SEND)) {
            messageCondition.setFromUserId(user.getId());
        } else if (messageType.equals(MessageType.SIGN)) {
            messageCondition.setSign(SignType.SIGN);
            messageCondition.setFromUserId(user.getId());
            messageCondition.setToUserId(user.getId());
        } else {
            throw new ServiceException(BaseErrorCode.PARAM_ILLEGAL);
        }

        List<MessageBoardVO> messageAllList = messageBoardManager.getMessageList(messageCondition);

        PageVO<MessageBoardVO> pageVO = new PageVO<>();

        List<MessageBoardVO> newMessageList = MessageBoardUtils.isReadComment(messageAllList, user.getUsername()).stream().parallel().sorted((x, y) -> {
            if (x.getIsRead().ordinal() == y.getIsRead().ordinal()) {
                return y.getUpdateTime().compareTo(x.getUpdateTime());
            } else {
                return x.getIsRead().ordinal() > y.getIsRead().ordinal() ? 1 : -1;
            }
        }).collect(Collectors.toList()).subList((page - 1) * pageSize, page * pageSize > messageAllList.size() ? messageAllList.size() : page * pageSize);

        pageVO.setPageNum(page);
        pageVO.setPageSize(pageSize);
        pageVO.setSize(newMessageList.size());
        pageVO.setTotal(messageAllList.size());
        pageVO.setPages((int) Math.ceil((double) messageAllList.size() / pageSize));
        pageVO.setList(newMessageList);
        //排序未读的最新的排前面
        return pageVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object getMessage(Long messageId) {

        Users user = RequestContext.getUserInfo();
        MessageBoardVO messageBoardVO = messageBoardManager.getMessageAndComment(messageId);

        if (messageBoardVO.getFromUserId() != user.getId() && messageBoardVO.getToUserId() != user.getId()) {
            throw new ServiceException(BaseErrorCode.AUTHORITY_ILLEGAL);
        }

        //未读留言
        List<Long> unReadMessageIds = new ArrayList<>();
        if (messageBoardVO.getIsRead().ordinal() == 1) {
            unReadMessageIds.add(messageId);
        }

        //未读评论
        if (messageBoardVO.getComments() != null && messageBoardVO.getComments().size() != 0) {
            for (MessageBoardVO comment : messageBoardVO.getComments()) {
                if (!comment.getFromUserName().equals(user.getUsername()) && comment.getIsRead().ordinal() == 1) {
                    unReadMessageIds.add(comment.getId());
                }
            }
        }
        if (unReadMessageIds.size() != 0) {
            messageBoardManager.updateIsRead(unReadMessageIds);
        }

        return messageBoardVO;
    }

    @Override
    public Object updateMessageSign(Long messageId, SignType sign) {
        Users user = RequestContext.getUserInfo();
        MessageBoard message = messageBoardManager.getMessage(messageId);
        if (message == null) {
            throw new ServiceException(BaseErrorCode.PARAM_ILLEGAL);
        }
        if (message.getToUserId() != user.getId() && message.getFromUserId() != user.getId()) {
            throw new ServiceException(BaseErrorCode.AUTHORITY_ILLEGAL);
        }
        messageBoardManager.updateSign(messageId, sign);

        return null;
    }

    @Override
    public Object removeMessage(Long messageId) {
        Users user = RequestContext.getUserInfo();
        MessageBoard messageBoard = messageBoardManager.getMessage(messageId);

        if (messageBoard == null) {
            throw new ServiceException(BaseErrorCode.PARAM_ILLEGAL);
        }
        if (messageBoard.getToUserId() != user.getId() && messageBoard.getFromUserId() != user.getId()) {
            throw new ServiceException(BaseErrorCode.AUTHORITY_ILLEGAL);
        }
        messageBoardManager.removeMessage(messageId);
        return null;
    }

    @Override
    public Object addComment(Long messageId, MessageBoard comment) {

        Users user = RequestContext.getUserInfo();

        comment.setParentId(messageId);
        comment.setFromUserId(user.getId());

        messageBoardManager.addMessage(comment);

        return null;
    }

    @Override
    public Object removeComment(Long messageId, Long commentId) {
        Users user = RequestContext.getUserInfo();
        MessageBoard messageBoard = messageBoardManager.getMessage(commentId);

        if (messageBoard == null || messageId != messageBoard.getParentId()) {
            throw new ServiceException(BaseErrorCode.PARAM_ILLEGAL);
        }

        if (user.getId() != messageBoard.getFromUserId()) {
            throw new ServiceException(BaseErrorCode.AUTHORITY_ILLEGAL);
        }
        messageBoardManager.removeComment(commentId);

        return null;
    }

    //草稿箱的操作

    @Override
    public Object addDrift(MessageDriftDTO messageDriftDTO) {
        Users user = RequestContext.getUserInfo();
        messageDriftManager.addDriftMessage(MessageDrift.builder()
                .fromUserId(user.getId())
                .message(messageDriftDTO.getMessage())
                .extFile(JsonUtils.toJSON(messageDriftDTO.getExtFile()))
                .toUser(JsonUtils.toJSON(messageDriftDTO.getToUserIds())).build());
        return null;
    }

    @Override
    public Object getDriftMessageList(Integer page, Integer pageSize) {
        Users user = RequestContext.getUserInfo();
        PageHelper.startPage(page, pageSize);
        List<MessageDrift> messageDrifts = messageDriftManager.getDriftMessageList(user.getId());
        List<MessageDriftVO> messageDriftVOS = new ArrayList<>();
        for (MessageDrift messageDrift : messageDrifts) {
            List<Long> userIds = JsonUtils.getListObject(messageDrift.getToUser(), Long.class);
            List<Users> users = new ArrayList<>();
            if (userIds != null && userIds.size() != 0) {
                users = usersManager.batchUserList(userIds);
            }
            messageDriftVOS.add(MessageDriftVO.builder()
                    .id(messageDrift.getId())
                    .fromUserId(messageDrift.getFromUserId())
                    .toUser(users)
                    .message(messageDrift.getMessage())
                    .updateTime(messageDrift.getUpdateTime())
                    .build());
        }

        return new PageInfo<>(messageDriftVOS);
    }

    @Override
    public Object getDriftMessage(Long driftId) {
        Users user = RequestContext.getUserInfo();
        if (driftId == null || driftId <= 0) {
            throw new ServiceException(BaseErrorCode.PARAM_ILLEGAL);
        }
        MessageDrift messageDrift = messageDriftManager.getDriftMessage(driftId);
        if (messageDrift == null) {
            throw new ServiceException(BaseErrorCode.PARAM_ILLEGAL);
        }
        if (!messageDrift.getFromUserId().equals(user.getId())) {
            throw new ServiceException(BaseErrorCode.AUTHORITY_ILLEGAL);
        }
        //批量查询用户信息
        List<Long> userIds = JsonUtils.getListObject(messageDrift.getToUser(), Long.class);
        List<Users> users = new ArrayList<>();
        if (userIds != null && userIds.size() != 0) {
            users = usersManager.batchUserList(userIds);
        }
        return MessageDriftVO.builder()
                .id(messageDrift.getId())
                .fromUserId(messageDrift.getFromUserId())
                .toUser(users)
                .message(messageDrift.getMessage())
                .extFile(JsonUtils.getListObject(messageDrift.getExtFile(),String.class))
                .updateTime(messageDrift.getUpdateTime())
                .build();
    }

    @Override
    public Object updateDriftMessage(Long driftId, MessageDriftDTO messageDriftDTO) {
        Users user = RequestContext.getUserInfo();

        messageDriftManager.updateDriftMessage(MessageDrift.builder()
                .id(driftId)
                .fromUserId(user.getId())
                .message(messageDriftDTO.getMessage())
                .extFile(JsonUtils.toJSON(messageDriftDTO.getExtFile()))
                .toUser(JsonUtils.toJSON(messageDriftDTO.getToUserIds()))
                .updateTime(new Date().toInstant())
                .build());

        return null;
    }


    @Override
    public Object removeDriftMessage(Long driftId) {
        Users user = RequestContext.getUserInfo();
        MessageDrift messageDrift = messageDriftManager.getDriftMessage(driftId);

        if (messageDrift == null) {
            throw new ServiceException(BaseErrorCode.PARAM_ILLEGAL);
        }
        if (!messageDrift.getFromUserId().equals(user.getId())) {
            throw new ServiceException(BaseErrorCode.AUTHORITY_ILLEGAL);
        }

        messageDriftManager.removeDriftManager(driftId);

        return null;
    }


}


















