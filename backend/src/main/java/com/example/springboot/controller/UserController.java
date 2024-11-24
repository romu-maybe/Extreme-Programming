package com.example.springboot.controller;


import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.springboot.common.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.User;
import com.example.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    //新增
    @PostMapping("/add")

    public Result add(@RequestBody User user){
        userService.insertUser(user);
        return Result.success();
    }

    @PutMapping("/update")

    public Result update(@RequestBody User user){
        userService.updateUser(user);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")

    public Result delete(@PathVariable Integer id){
        userService.deleteUser(id);
        return Result.success();
    }

    @DeleteMapping("/delete/batch")

    public Result batchdelete(@RequestBody  List<Integer> ids){
        userService.batchDeleteUser(ids);
        return Result.success();
    }

    @GetMapping("/selectAll")

    public Result selectAll(){
        List<User> userList = userService.selectAll();
        return Result.success(userList);
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        User user = userService.selectById(id);
        return Result.success(user);
    }

    @GetMapping("/selectByName/{name}")
    public Result selectByName(@PathVariable String name){
        List<User> userList = userService.selectByName(name);
        return Result.success(userList);
    }
    @GetMapping("/selectByCollect")
    public Result selectByCollect() {
        List<User> userList = userService.selectByCollect();
        return Result.success(userList);
    }


    /**
     * 多条件模糊查询用户信息
     * pageNum 当前的页码
     * pageSize 每页查询的个数
     */
    @GetMapping("/selectByPage")

    public Result selectByPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam String username,
                               @RequestParam String name,
                               @RequestParam String collect) {
        Page<User> page = userService.selectByPage(pageNum, pageSize, username, name, collect);
        return Result.success(page);
    }

    @GetMapping("/export")
    public void exportData(@RequestParam(required = false) String username,
                           @RequestParam(required = false) String name,
                           HttpServletResponse response) throws IOException {
        ExcelWriter writer = ExcelUtil.getWriter(true);
        // 第一种全部导出
        List<User> list = new ArrayList<>();
        if(StrUtil.isBlank(username)&&StrUtil.isBlank(name)){
            list = userService.selectAll();
        }


        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("联系人信息表", "UTF-8") + ".xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        writer.flush(outputStream, true);
        writer.close();
        outputStream.flush();
        outputStream.close();
    }
    @PostMapping("/import")
    public Result importData(MultipartFile file) throws IOException {
        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        List<User> userList = reader.readAll(User.class);

        try {
            for (User user : userList) {
                userService.insertUser(user); // 调用逐条插入的方法
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("逐条保存数据时出错：" + e.getMessage());
        }
        return Result.success("数据导入成功");
    }





}
