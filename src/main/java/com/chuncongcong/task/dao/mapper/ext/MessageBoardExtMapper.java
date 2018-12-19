package com.chuncongcong.task.dao.mapper.ext;

import com.chuncongcong.task.model.constants.SignType;
import com.chuncongcong.task.model.domain.MessageCondition;
import com.chuncongcong.task.model.entity.MessageBoard;
import com.chuncongcong.task.model.vo.MessageBoardVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageBoardExtMapper extends com.chuncongcong.task.dao.mapper.MessageBoardMapper {

    /**
     * 查询收发留言列表
     * @param messageCondition
     * @return
     */
    List<MessageBoardVO> getMessageList(
            @Param("messageCondition") MessageCondition messageCondition);

    /**
     * 获取留言下的评论
     *
     * @param parentId
     * @return
     */
    List<MessageBoardVO> getCommentList(
            @Param("parentId") Long parentId);

    /**
     * 获取留言信息
     *
     * @param messageId
     * @return
     */
    MessageBoard getMessage(Long messageId);

    /**
     * 根据id查询留言信息
     *
     * @param messageId
     * @return
     */
    MessageBoardVO getMessageAndComment(
            @Param("messageId") Long messageId);


    /**
     * 更新留言收藏状态
     *
     * @param messageId
     * @param sign
     */
    void updateSign(
            @Param("messageId") Long messageId,
            @Param("sign") SignType sign);

    /**
     * 更新为已读
     *
     * @param messageIds
     */
    void updateIsRead(
            @Param("messageIds") List<Long> messageIds);

    /**
     * 删除留言
     *
     * @param id
     */
    void removeMessage(Long id);

    /**
     * 删除评论或草稿
     *
     * @param id
     */
    void removeComment(Long id);



}