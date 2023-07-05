package com.bjut.bjut_clouddisk.service;

import com.bjut.bjut_clouddisk.mapper.NormalUserMapper;
import com.bjut.bjut_clouddisk.utils.FileManager;
import com.bjut.bjut_clouddisk.entity.NormalUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NormalUserService {
    @Autowired
    NormalUserMapper normalUserMapper;

    public enum RegisterCode {
        SUCCEED,
        CREATE_FOLDER_ERROE,
        DATABASE_ERROE,
    }

    public RegisterCode NormalUserRegister(NormalUserEntity normalUserEntity) {
        String userPath = normalUserEntity.getUsername();
        boolean isFolderSucceed = true;
        boolean isDbSucceed = true;
        try {
            if (!normalUserMapper.NormalUserRegister(normalUserEntity)){
                isDbSucceed = false;
                throw new RuntimeException();
            }
            final String[][] createPathsAndNames = {
                    {"", normalUserEntity.getUsername()},
                    {userPath, "/.userdata"},
                    {userPath, "/.userdata/.trash"}
            };
            for (String[] pn : createPathsAndNames){
                if (!FileManager.mkdir(pn[0], pn[1])) {
                    isFolderSucceed = false;
                    throw new RuntimeException();
                }
            }
        }
        catch (RuntimeException e){
            if (!isFolderSucceed){
                return RegisterCode.CREATE_FOLDER_ERROE;
            }
            else {
                return RegisterCode.DATABASE_ERROE;
            }
        }
        return RegisterCode.SUCCEED;
    }

    public enum LoginCode {
        SUCCEED,
        USERNAME_EMPTY,
        PASSWORD_EMPYT,
        USERNAME_OR_PASSWORD_ERROR
    }
    public LoginCode NormalUserLogin(NormalUserEntity normalUserEntity) {
        // 判空
        if (normalUserEntity.getUsername().isEmpty()){
            return LoginCode.USERNAME_EMPTY;
        }
        if (normalUserEntity.getPassword().isEmpty()){
            return LoginCode.PASSWORD_EMPYT;
        }
        // 搜索数据库
        NormalUserEntity returnNormalUserEntity = normalUserMapper.NormalUserLogin(normalUserEntity);
        if (returnNormalUserEntity != null) {
            return LoginCode.SUCCEED;
        }
        else {
            return LoginCode.USERNAME_OR_PASSWORD_ERROR;
        }
    }
}
