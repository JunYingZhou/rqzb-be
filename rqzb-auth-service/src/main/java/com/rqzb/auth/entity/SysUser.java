package com.rqzb.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_user")
public class SysUser {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long deptId;

    private String username;

    private String password;

    private String nickname;

    private String realName;

    private String email;

    private String phone;

    private Integer sex;

    private String avatar;

    private Integer status;

    private String loginIp;

    private LocalDateTime loginTime;

    @TableLogic
    private Integer deleted;
}
