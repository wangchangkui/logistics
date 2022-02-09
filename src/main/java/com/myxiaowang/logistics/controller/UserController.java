package com.myxiaowang.logistics.controller;

import com.myxiaowang.logistics.pojo.Address;
import com.myxiaowang.logistics.pojo.User;
import com.myxiaowang.logistics.service.UserService;
import com.myxiaowang.logistics.util.Reslut.ResponseResult;
import com.myxiaowang.logistics.util.Reslut.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.ResultSet;
import java.util.List;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年01月18日 16:39:00
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;


    @GetMapping("/getAddressById")
    public ResponseResult<Address> getAddressById(@RequestParam("id") String id){
        return userService.getAddressById(id);
    }

    @PostMapping("/saveOrInsert")
    public ResponseResult<String> updateOrInsert(@RequestBody Address address){
        return userService.addAddress(address);
    }

    @PostMapping("/setCheck")
    public ResponseResult<String> setCheck(@RequestParam("id") String id,@RequestParam("userId") String userId){
        return userService.setCheck(id,userId);
    }

    @PostMapping("/deleteAddress")
    public ResponseResult<String> deleteAddress(@RequestParam("id") String id){
        return userService.deleteUserAddress(id);
    }

    @GetMapping("/addressList/{userId}")
    public ResponseResult<List<Address>> getUserList(@PathVariable("userId") String userId){
        return userService.getUserAddressList(userId);
    }

    @PostMapping("/uploadUser")
    public ResponseResult<String> uploadUser(@RequestBody User user){
       return userService.updateUser(user);
    }

    @PostMapping("/upload")
    public ResponseResult<String> uploadHeader(String userId, MultipartFile file){
        return userService.uploadHeader(userId,file);
    }

    @PostMapping("/upload/check")
    public ResponseResult<String> uploadCheck(MultipartFile file){
        return userService.uploadFileCheck(file);
    }

    @PostMapping("/upload/contrast")
    public ResponseResult<String> uploadContrast(@RequestParam("filePath") String filePath,@RequestParam("userName") String userName, @RequestParam("card") String card,@RequestParam("userId") String userId){
        return userService.checkCard(filePath,userName,card,userId);
    }

    @PostMapping("/check")
    public ResponseResult<User> checkSms(@RequestParam("phone") String phone,@RequestParam("sms") String sms){
        return userService.checkSms(phone,sms);
    }

    @PostMapping("/getUser")
    public ResponseResult<User> getUser(@RequestParam("openId") String openId){
        return userService.getUser(openId);
    }

    @PostMapping("/sign")
    public ResponseResult<String> signUser(@RequestBody User user){
        return userService.signUser(user);
    }

    @GetMapping("/address/{openId}")
    public ResponseResult<Address> getAddress(@PathVariable("openId") String openId){
        return userService.getUserAddress(openId);
    }


    /**
     * 这个接口需要做幂等处理
     * @param phone
     * @return
     */
    @PostMapping("/sendSms")
    public ResponseResult<ResultInfo> sendSms(@RequestParam("phone") String phone){
        return userService.sendSms(phone);
    }





}
