package com.chuncongcong.task.manager;

import com.chuncongcong.task.dao.mapper.ext.MessageBoardExtMapper;
import com.chuncongcong.task.model.constants.SignType;
import com.chuncongcong.task.model.domain.MessageCondition;
import com.chuncongcong.task.model.entity.MessageBoard;
import com.chuncongcong.task.model.vo.MessageBoardVO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Hu
 * @date 2018/8/13 10:25
 */

@Component
public class MessageBoardManager {

    private final MessageBoardExtMapper messageBoardExtMapper;

    public MessageBoardManager(MessageBoardExtMapper messageBoardExtMapper) {
        this.messageBoardExtMapper = messageBoardExtMapper;
    }

    public void addMessage(MessageBoard messageBoard) {
        messageBoardExtMapper.insertSelective(messageBoard);
    }

    public List<MessageBoardVO> getMessageList(MessageCondition messageCondition) {
        return messageBoardExtMapper.getMessageList(messageCondition);
    }

    /**
     * 获取留言及留言下的评论
     *
     * @param messageId
     * @return
     */
    public MessageBoardVO getMessageAndComment(Long messageId) {
        return messageBoardExtMapper.getMessageAndComment(messageId);
    }

    /**
     * 获取留言信息
     *
     * @param messageId
     * @return
     */
    public MessageBoard getMessage(Long messageId) {
        return messageBoardExtMapper.getMessage(messageId);
    }

    @Async
    public void updateIsRead(List<Long> unReadMessageIds) {
        messageBoardExtMapper.updateIsRead(unReadMessageIds);
    }

    public void removeMessage(Long messageId) {
        messageBoardExtMapper.removeMessage(messageId);
    }

    public void removeComment(Long id) {
        messageBoardExtMapper.removeComment(id);
    }

    public void updateSign(Long messageId, SignType sign) {
        messageBoardExtMapper.updateSign(messageId, sign);
    }
}
