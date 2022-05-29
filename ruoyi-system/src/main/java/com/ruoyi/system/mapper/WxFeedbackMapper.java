package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.WxFeedback;

/**
 * 反馈管理Mapper接口
 * 
 * @author jandmla
 * @date 2022-03-24
 */
public interface WxFeedbackMapper 
{
    /**
     * 查询反馈管理
     * 
     * @param id 反馈管理主键
     * @return 反馈管理
     */
    public WxFeedback selectWxFeedbackById(Integer id);

    /**
     * 查询反馈管理列表
     * 
     * @param wxFeedback 反馈管理
     * @return 反馈管理集合
     */
    public List<WxFeedback> selectWxFeedbackList(WxFeedback wxFeedback);

    /**
     * 新增反馈管理
     * 
     * @param wxFeedback 反馈管理
     * @return 结果
     */
    public int insertWxFeedback(WxFeedback wxFeedback);

    /**
     * 修改反馈管理
     * 
     * @param wxFeedback 反馈管理
     * @return 结果
     */
    public int updateWxFeedback(WxFeedback wxFeedback);

    /**
     * 删除反馈管理
     * 
     * @param id 反馈管理主键
     * @return 结果
     */
    public int deleteWxFeedbackById(Integer id);

    /**
     * 批量删除反馈管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWxFeedbackByIds(String[] ids);
}
