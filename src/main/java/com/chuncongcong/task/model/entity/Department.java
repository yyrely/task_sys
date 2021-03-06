package com.chuncongcong.task.model.entity;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column department.id
     *
     * @mbg.generated
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column department.name
     *
     * @mbg.generated
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column department.head_id
     *
     * @mbg.generated
     */
    private Long headId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column department.create_time
     *
     * @mbg.generated
     */
    private Instant createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column department.update_time
     *
     * @mbg.generated
     */
    private Instant updateTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column department.state
     *
     * @mbg.generated
     */
    private Byte state;
}