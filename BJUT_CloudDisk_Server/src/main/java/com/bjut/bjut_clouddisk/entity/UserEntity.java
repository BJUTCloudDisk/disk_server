package com.bjut.bjut_clouddisk.entity;

import lombok.Data;

@Data
public class UserEntity {
    private String username;  // 用户名 用户名就是该用户的云盘的盘符
    private String password;  // 密码
    private String headImgUrl;  // 头像路径
    private String email;       // 邮箱
}
