package com.chuncongcong.task.dao.mapper.ext;

import com.chuncongcong.task.model.entity.MessageDrift;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageDriftExtMapper extends com.chuncongcong.task.dao.mapper.MessageDriftMapper {
    /**
     * 获取草稿列表
     *
     * @param fromUserId
     * @return
     */
    List<MessageDrift> getDriftMessageList(
            @Param("fromUserId") Long fromUserId);

    /**
     * 获取草稿详情
     *
     * @param driftId
     * @return
     */
    MessageDrift getDriftMessage(Long driftId);

    /**
     * 删除草稿
     *
     * @param driftId
     */
    void removeDriftManager(
            @Param("driftId") Long driftId);


}