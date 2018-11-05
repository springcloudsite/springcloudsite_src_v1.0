package com.scs.usermanagement.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 *
 * 通过SCS工具自动生成。表sys_user的VO对象<br/>
 * 对应表名：sys_user,备注：系统人员表
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SysUserVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 对应字段：id,备注：主键 */
	private Integer id;

	/** 对应字段：login_name,备注：登录名称 */
	private String loginName;

	/** 对应字段：nickname,备注：昵称 */
	private String nickname;

	/** 对应字段：password,备注：密码 */
//	private String password;

	/** 对应字段：login_type,备注：登录名类型，1：登录名，2：手机，3：邮箱 */
	private String loginType;

	/** 对应字段：is_disable,备注：是否禁用，0：可用，1：禁用 */
	private String isDisable;

	/** 对应字段：is_delete,备注：逻辑删除，0：未删除，1：删除 */
	private String isDelete;

	/** 对应字段：created_by,备注：创建人 */
	private String createdBy;

	/** 对应字段：created_date,备注：创建时间 */
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss" , timezone="GMT+8")
	private Date createdDate;

	/** 对应字段：last_modified_by,备注：修改人 */
	private String lastModifiedBy;

	/** 对应字段：last_modified_date,备注：修改时间 */
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss" , timezone="GMT+8")
	private Date lastModifiedDate;


}
