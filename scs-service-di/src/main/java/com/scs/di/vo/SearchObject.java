package com.scs.di.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * 分析对象查询条件
 * 
 *
 * @version Scs-DI v1.0
 * @author Sun Yunxu, 2018年10月23日
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchObject implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 对应字段：name,备注：对象名称 */
    private String name;

    /** 对应字段：name,备注：对象名称 （模糊查询） */
    private String nameLike;
    
    /** 对象类型名称  */
    private String objectType;

    /** 对象类型名称 （模糊查询） */
    private String objectTypeLike;

}
