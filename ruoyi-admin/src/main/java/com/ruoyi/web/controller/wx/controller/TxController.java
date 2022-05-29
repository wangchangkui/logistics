package com.ruoyi.web.controller.wx.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.common.core.domain.resutl.ResponseResult;
import com.ruoyi.system.QueryDto.QueryDto;
import com.ruoyi.system.domain.Withdraw;
import com.ruoyi.system.service.wx.TxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月23日 11:24:00
 */
@RestController
@RequestMapping("/wx/tx")
public class TxController {
    @Autowired
    private TxService txService;


    @PostMapping("/insertTx")
    public ResponseResult<String> insertTx(@RequestBody Withdraw tx){
        return txService.insertTx(tx);
    }

    @PostMapping("/selectTx")
    public IPage<Withdraw> getPageTx(@RequestBody QueryDto<Withdraw,Withdraw> queryDto){
        return txService.getTxPage(queryDto);
    }
}
