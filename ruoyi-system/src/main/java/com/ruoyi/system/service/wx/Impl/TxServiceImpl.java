package com.ruoyi.system.service.wx.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ruoyi.common.core.domain.resutl.ResponseResult;
import com.ruoyi.system.QueryDto.QueryDto;
import com.ruoyi.system.domain.Withdraw;
import com.ruoyi.system.domain.WxUser;
import com.ruoyi.system.plus.mapper.wx.TxMapper;
import com.ruoyi.system.plus.mapper.wx.UserMapper;
import com.ruoyi.system.service.wx.TxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月23日 11:22:00
 */
@Service
public class TxServiceImpl extends ServiceImpl<TxMapper, Withdraw> implements TxService {

    @Autowired
    private TxMapper txMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<String> insertTx(Withdraw tx) {
        WxUser user = userMapper.selectOne(new QueryWrapper<WxUser>().eq("user_id", tx.getUserId()));
        BigDecimal decimals = user.getDecimals();
        if(decimals.compareTo(tx.getMoney())<0){
            return ResponseResult.error("金额不足，无法提现");
        }
        BigDecimal subtract = decimals.subtract(tx.getMoney());
        Integer version = Math.toIntExact(user.getVersion());
        user.setDecimals(subtract);
        user.setVersion(user.getVersion()+1);
        user.setCreateTime(new Timestamp(System.currentTimeMillis()));
        userMapper.update(user,new QueryWrapper<WxUser>().eq("user_id",tx.getUserId()).eq("money",decimals).eq("version",version));
        save(tx);
        return ResponseResult.success("操作成功");
    }

    @Override
    public IPage<Withdraw> getTxPage(QueryDto<Withdraw, Withdraw> queryDto) {
        return txMapper.getUserTx(queryDto.getPage(), queryDto.getEntity().getUserId());
    }
}
