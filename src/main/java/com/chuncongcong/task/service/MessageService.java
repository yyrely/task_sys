package com.chuncongcong.task.service;

import com.chuncongcong.task.model.constants.MessageType;
import com.chuncongcong.task.model.constants.ReadType;
import com.chuncongcong.task.model.constants.SignType;
import com.chuncongcong.task.model.dto.MessageBoardDTO;
import com.chuncongcong.task.model.dto.MessageDriftDTO;
import com.chuncongcong.task.model.entity.MessageBoard;

/**
 * @author Hu
 * @date 2018/8/10 10:22
 */


public interface MessageService {

    /**
     * 添加留言
     *
     * @param messageBoardDTO
     * @return
     */
    Object addMessage(MessageBoardDTO messageBoardDTO) throws Exception;

    /**
     * 获取收到或发送信息
     * @param messageType
     * @return
     */
    Object getMessageList(MessageType messageType, ReadType read, Integer page, Integer pageSize);

    /**
     * 获取留言详情
     *
     * @param messageId
     * @return
     */
    Object getMessage(Long messageId);

    /**
     * 收藏留言
     *
     * @param messageId
     * @param sign
     * @return
     */
    Object updateMessageSign(Long messageId, SignType sign);

    /**
     * 删除留言
     *
     * @param messageId
     * @return
     */
    Object removeMessage(Long messageId);

    /**
     * 添加回复
     *
     * @param messageId
     * @param comment
     * @return
     */
    Object addComment(Long messageId, MessageBoard comment);

    /**
     * 删除回复
     *
     * @param messageId
     * @param commentId
     * @return
     */
    Object removeComment(Long messageId, Long commentId);

    /**
     * 保存为草稿
     *
     * @param messageDriftDTO
     * @return
     */
    Object addDrift(MessageDriftDTO messageDriftDTO);

    /**
     * 获取草稿列表
     *
     * @return
     */
    Object getDriftMessageList(Integer page, Integer pageSize);

    /**
     * 获取草稿详情
     *
     * @param driftId
     * @return
     */
    Object getDriftMessage(Long driftId);

    /**
     * 更新草稿详情
     *
     * @param messageDriftDTO
     * @return
     */
    Object updateDriftMessage(Long driftId, MessageDriftDTO messageDriftDTO);

    /**
     * 删除草稿
     *
     * @param driftId
     * @return
     */
    Object removeDriftMessage(Long driftId);


}
