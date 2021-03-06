package com.chuncongcong.task.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskUpdate {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column task_update.id
     *
     * @mbg.generated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column task_update.task_id
     *
     * @mbg.generated
     */
    private Long taskId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column task_update.update_user
     *
     * @mbg.generated
     */
    private String updateUser;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column task_update.create_time
     *
     * @mbg.generated
     */
    private Instant createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column task_update.update_time
     *
     * @mbg.generated
     */
    private Instant updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column task_update.state
     *
     * @mbg.generated
     */
    private Byte state;
}