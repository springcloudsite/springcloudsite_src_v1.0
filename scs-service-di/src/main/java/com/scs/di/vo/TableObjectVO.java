package com.scs.di.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * 分析对象列表对象
 * 
 *
 * @version Scs-DI v1.0
 * @author Sun Yunxu, 2018年10月23日
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TableObjectVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 对应字段：id,备注：主键ID */
    private Integer id;

    /** 对应字段：businessid,备注：业务唯一标识（在分支业务系统内的ID(来自于采集的表，如股票代码，热力站名称),在相同typeid时候，不可以重复） */
    private String businessid;

    /** 对应字段：name,备注：对象名称 */
    private String name;

    /** 对应字段：typeid,备注：关联[对象类型表].[ID] */
    private Integer typeid;

    /** 对应字段：description,备注：对象描述 */
    private String description;

    /** 对应字段：founder,备注：创建人 */
    private String founder;

    /** 分析对象类型显示字段 */
    private String objectType;

}
