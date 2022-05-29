package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WxFeedbackMapper;
import com.ruoyi.system.domain.WxFeedback;
import com.ruoyi.system.service.IWxFeedbackService;
import com.ruoyi.common.core.text.Convert;

/**
 * 反馈管理Service业务层处理
 * 
 * @author jandmla
 * @date 2022-03-24
 */
@Service
public class WxFeedbackServiceImpl implements IWxFeedbackService {

    private final WxFeedbackMapper wxFeedbackMapper;

    public WxFeedbackServiceImpl(WxFeedbackMapper wxFeedbackMapper) {
        this.wxFeedbackMapper = wxFeedbackMapper;
    }

    /**
     * 查询反馈管理
     * 
     * @param id 反馈管理主键
     * @return 反馈管理
     */
    @Override
    public WxFeedback selectWxFeedbackById(Integer id)
    {
        return wxFeedbackMapper.selectWxFeedbackById(id);
    }

    /**
     * 查询反馈管理列表
     * 
     * @param wxFeedback 反馈管理
     * @return 反馈管理
     */
    @Override
    public List<WxFeedback> selectWxFeedbackList(WxFeedback wxFeedback)
    {
        return wxFeedbackMapper.selectWxFeedbackList(wxFeedback);
    }

    /**
     * 新增反馈管理
     * 
     * @param wxFeedback 反馈管理
     * @return 结果
     */
    @Override
    public int insertWxFeedback(WxFeedback wxFeedback)
    {
        wxFeedback.setCreateTime(DateUtils.getNowDate());
        return wxFeedbackMapper.insertWxFeedback(wxFeedback);
    }

    /**
     * 修改反馈管理
     * 
     * @param wxFeedback 反馈管理
     * @return 结果
     */
    @Override
    public int updateWxFeedback(WxFeedback wxFeedback)
    {
        return wxFeedbackMapper.updateWxFeedback(wxFeedback);
    }

    /**
     * 批量删除反馈管理
     * 
     * @param ids 需要删除的反馈管理主键
     * @return 结果
     */
    @Override
    public int deleteWxFeedbackByIds(String ids)
    {
        return wxFeedbackMapper.deleteWxFeedbackByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除反馈管理信息
     * 
     * @param id 反馈管理主键
     * @return 结果
     */
    @Override
    public int deleteWxFeedbackById(Integer id)
    {
        return wxFeedbackMapper.deleteWxFeedbackById(id);
    }
}
