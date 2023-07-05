package com.bjut.bjut_clouddisk.controller;

import com.bjut.bjut_clouddisk.service.NormalUserService;
import com.bjut.bjut_clouddisk.common.MyResponse;
import com.bjut.bjut_clouddisk.entity.NormalUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NormalUserController {
    @Autowired
    NormalUserService normalUserService;

    // 注册
    @PostMapping("/Register")
    public MyResponse<String> NormalUserRegister(NormalUserEntity normalUserEntity) {
        // 先判断是否注册成功
        NormalUserService.RegisterCode registerCode = normalUserService.NormalUserRegister(normalUserEntity);

        switch (registerCode){
            case SUCCEED -> {
                return MyResponse.success("注册成功").message("注册成功");
            }
            case CREATE_FOLDER_ERROE -> {
                return MyResponse.error("注册失败").message("创建用户目录失败");
            }
            case DATABASE_ERROE -> {
                return MyResponse.error("注册失败").message("用户已存在");
            }
            default -> {
                return MyResponse.error("注册失败").message("未知错误");
            }
        }
    }

    // 登录
    @PostMapping("/Login")
    public MyResponse<String> NormalUserLogin(NormalUserEntity normalUserEntity) {
        // 先判断是否登录成功
        NormalUserService.LoginCode loginCode = normalUserService.NormalUserLogin(normalUserEntity);

        switch (loginCode){
            case SUCCEED -> {
                return MyResponse.success("登录成功").message("登录成功");
            }
            case USERNAME_EMPTY -> {
                return MyResponse.error("登录失败").message("用户名为空");
            }
            case PASSWORD_EMPYT -> {
                return MyResponse.error("登录失败").message("密码为空");
            }
            case USERNAME_OR_PASSWORD_ERROR -> {
                return MyResponse.error("登录失败").message("用户名或密码错误");
            }
            default -> {
                return MyResponse.error("登录失败").message("未知错误");
            }
        }
    }
}
