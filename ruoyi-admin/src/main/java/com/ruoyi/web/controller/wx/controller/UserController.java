package com.ruoyi.web.controller.wx.controller;

import com.ruoyi.common.core.domain.resutl.ResponseResult;
import com.ruoyi.common.core.domain.resutl.ResultInfo;
import com.ruoyi.system.domain.WxUser;
import com.ruoyi.system.domain.wx.Address;
import com.ruoyi.system.service.wx.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年01月18日 16:39:00
 */
@RestController
@RequestMapping("/wx/user")
public class UserController {
    @Autowired
    private UserService userService;


    @GetMapping("/userArr/{arrId}")
    public ResponseResult<String> overArrea(@PathVariable Integer arrId){
        return userService.overArrea(arrId);
    }


    @GetMapping("/arrea/{userId}")
    public ResponseResult<List<Map<String, Object>>> getUserArrea(@PathVariable String userId){
        return userService.getUserArre(userId);
    }

    @GetMapping("/arreInfo/{openId}")
    public ResponseResult<List<Map<String, Object>>> getArreInfo(@PathVariable("openId") String openId){
        return userService.getArreInfo(openId);
    }

    @PostMapping("/passwdByUser")
    public ResponseResult<WxUser> passWordByUser(@RequestParam("userName") String userName, @RequestParam("password") String password){
        return userService.passWordLogin(userName,password);
    }


    @GetMapping("/getAddressById")
    public ResponseResult<Address> getAddressById(@RequestParam("id") String id){
        return userService.getAddressById(id);
    }

    @PostMapping("/saveOrInsert")
    public ResponseResult<String> updateOrInsert(@RequestBody Address address){
        return userService.addAddress(address);
    }

    @PostMapping("/setCheck")
    public ResponseResult<String> setCheck(@RequestParam("id") String id, @RequestParam("userId") String userId){
        return userService.setCheck(id,userId);
    }

    @PostMapping("/deleteAddress")
    public ResponseResult<String> deleteAddress(@RequestBody WxUser user){
        return userService.deleteUserAddress(user.getUserId());
    }

    @GetMapping("/addressList/{userId}")
    public ResponseResult<List<Address>> getUserList(@PathVariable("userId") String userId){
        return userService.getUserAddressList(userId);
    }

    @PostMapping("/uploadUser")
    public ResponseResult<String> uploadUser(@RequestBody WxUser user){
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
    public ResponseResult<String> uploadContrast(@RequestParam("filePath") String filePath, @RequestParam("userName") String userName, @RequestParam("card") String card, @RequestParam("userId") String userId){
        return userService.checkCard(filePath,userName,card,userId);
    }

    @PostMapping("/check")
    public ResponseResult<WxUser> checkSms(@RequestParam("phone") String phone, @RequestParam("sms") String sms){
        return userService.checkSms(phone,sms);
    }

    @PostMapping("/getUser")
    public ResponseResult<WxUser> getUser(@RequestParam("openId") String openId){
        return userService.getUser(openId);
    }

    @PostMapping("/signOrLogin")
    public ResponseResult<WxUser> signOrLogin(@RequestBody WxUser user){
        return userService.signOrLogin(user);
    }

    @PostMapping("/sign")
    public ResponseResult<String> signUser(@RequestBody WxUser user){
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
