package com.example.springboot.entity;


import cn.hutool.core.annotation.Alias;
import lombok.Data;

@Data
public class User {
    @Alias("编号")
    private Integer id;
    @Alias("用户名")
    private String username;

    @Alias("姓名")
    private String name;
    @Alias("电话")
    private String phone;
    @Alias("邮箱")
    private String email;
    @Alias("地址")
    private String address;

    @Alias("社媒账号")
    private String socialid;
    @Alias("是否收藏")
    private String collect;


}
