package com.bjut.bjut_clouddisk.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用返回结果，服务端响应的数据最终都会被封装成这种对象
 * @param <T>
 */
@Data
public class MyResponse<T> {
    //TODO 建一个枚举类
    private Integer code; // 编码：只有1为成功
    private String message; // 错误信息
    private T data; // 数据
    private Map map = new HashMap(); // 动态数据，token可以放在这

    // 返回成功响应
    public static <T> MyResponse<T> success(T object) {
        MyResponse<T> myResponse = new MyResponse<>();
        myResponse.code = 1;
        myResponse.data = object;
        return myResponse;
    }

    // 返回失败响应
    public static <T> MyResponse<T> error(T object) {
        MyResponse<T> myResponse = new MyResponse();
        myResponse.code = 0;
        myResponse.data = object;
        return myResponse;
    }

    // 设置message
    public MyResponse<T> message(String message) {
        this.setMessage(message);
        return this;
    }

    // 设置动态信息
    public MyResponse<T> map(String t1, T t2) {
        this.map.put(t1, t2);
        return this;
    }
}
