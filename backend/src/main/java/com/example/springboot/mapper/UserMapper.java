package com.example.springboot.mapper;


import com.example.springboot.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Insert("insert into `user` (username,  name, phone, email, address,  socialid, collect)" +
            "values (#{username},  #{name}, #{phone}, #{email}, #{address}, #{socialid}, #{collect})" )
    void insert(User user);

    @Update("update `user` set username = #{username},  name = #{name}, phone = #{phone}, " +
            "email = #{email}, address = #{address},  socialid = #{socialid}, collect = #{collect}  where id = #{id}")
    void updateUser(User user);

    @Delete("delete from `user` where id = #{id}")
    void deleteUser(Integer id);

    @Select("select * from `user` order by id desc")
    List<User> selectAll();

    @Select("select * from `user` where id = #{id} order by id desc")
    User selectById(Integer id);

    @Select("select * from `user` where name = #{name} order by id desc")
    List<User> selectByName(String name);

    @Select("<script>" +
            "select * from `user` " +
            "where username like concat('%', #{username}, '%') " +
            "and name like concat('%', #{name}, '%') " +
            "<if test='collect != null and collect != \"\"'>" +
            "and collect = #{collect} " +
            "</if>" +
            "order by id desc limit #{skipNum}, #{pageSize}" +
            "</script>")
    List<User> selectByPage(@Param("skipNum") Integer skipNum,
                            @Param("pageSize") Integer pageSize,
                            @Param("username") String username,
                            @Param("name") String name,
                            @Param("collect") String collect);

    @Select("select count(id) from `user` where username like concat('%', #{username}, '%') and name like concat('%', #{name}, '%') order by id desc")
    int selectCountByPage(@Param("username") String username, @Param("name") String name, @Param("collect") String collect);

    @Select("select * from `user` where collect = true order by id desc")
    List<User> selectByCollect();

}
