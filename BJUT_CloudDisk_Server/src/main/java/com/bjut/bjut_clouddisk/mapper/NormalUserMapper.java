package com.bjut.bjut_clouddisk.mapper;

import com.bjut.bjut_clouddisk.entity.NormalUserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NormalUserMapper {
    // 判断注册是否成功
    public boolean NormalUserRegister(NormalUserEntity normalUserEntity);
    // 判断登录是否成功
    public NormalUserEntity NormalUserLogin(NormalUserEntity normalUserEntity);
}
