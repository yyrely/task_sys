package com.chuncongcong.task.dao.mapper;

import com.chuncongcong.task.model.entity.MessageDrift;

public interface MessageDriftMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table message_drift
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table message_drift
     *
     * @mbg.generated
     */
    int insert(MessageDrift record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table message_drift
     *
     * @mbg.generated
     */
    int insertSelective(MessageDrift record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table message_drift
     *
     * @mbg.generated
     */
    MessageDrift selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table message_drift
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(MessageDrift record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table message_drift
     *
     * @mbg.generated
     */
    int updateByPrimaryKeyWithBLOBs(MessageDrift record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table message_drift
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(MessageDrift record);
}