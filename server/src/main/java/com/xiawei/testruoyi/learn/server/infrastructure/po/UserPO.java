package com.xiawei.testruoyi.learn.server.infrastructure.po;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 用户持久化对象
 *
 * <p>继承 Model 支持 ActiveRecord 模式，提供 insert/updateById/deleteById 等方法。</p>
 */
@Data
@Accessors(chain = true)
@TableName("sys_user")
public class UserPO extends Model<UserPO> {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名，唯一
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 加密密码（BCrypt）
     */
    private String password;

    /**
     * 状态：0-禁用，1-正常
     */
    private Integer status;

    /**
     * 所属部门ID
     */
    private Long deptId;

    /**
     * 岗位ID
     */
    private Long postId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 逻辑删除标志：0-未删除，非0-已删除（值为被删除记录的ID）
     */
    @TableLogic(value = "0", delval = "id")
    private Long isDeleted;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
    private Date updateTime;
}
