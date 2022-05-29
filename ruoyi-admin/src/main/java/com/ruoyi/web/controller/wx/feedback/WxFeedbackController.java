package com.ruoyi.web.controller.wx.feedback;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.WxFeedback;
import com.ruoyi.system.service.IWxFeedbackService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 反馈管理Controller
 * 
 * @author jandmla
 * @date 2022-03-24
 */
@Controller
@RequestMapping("/system/feedback")
public class WxFeedbackController extends BaseController
{
    private String prefix = "system/feedback";

    @Autowired
    private IWxFeedbackService wxFeedbackService;

    @RequiresPermissions("system:feedback:view")
    @GetMapping()
    public String feedback()
    {
        return prefix + "/feedback";
    }

    /**
     * 查询反馈管理列表
     */
    @RequiresPermissions("system:feedback:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(WxFeedback wxFeedback)
    {
        startPage();
        List<WxFeedback> list = wxFeedbackService.selectWxFeedbackList(wxFeedback);
        return getDataTable(list);
    }

    /**
     * 导出反馈管理列表
     */
    @RequiresPermissions("system:feedback:export")
    @Log(title = "反馈管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(WxFeedback wxFeedback)
    {
        List<WxFeedback> list = wxFeedbackService.selectWxFeedbackList(wxFeedback);
        ExcelUtil<WxFeedback> util = new ExcelUtil<WxFeedback>(WxFeedback.class);
        return util.exportExcel(list, "反馈管理数据");
    }

    /**
     * 新增反馈管理
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存反馈管理
     */
    @RequiresPermissions("system:feedback:add")
    @Log(title = "反馈管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(WxFeedback wxFeedback)
    {
        return toAjax(wxFeedbackService.insertWxFeedback(wxFeedback));
    }

    /**
     * 修改反馈管理
     */
    @RequiresPermissions("system:feedback:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        WxFeedback wxFeedback = wxFeedbackService.selectWxFeedbackById(id);
        mmap.put("wxFeedback", wxFeedback);
        return prefix + "/edit";
    }

    /**
     * 修改保存反馈管理
     */
    @RequiresPermissions("system:feedback:edit")
    @Log(title = "反馈管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(WxFeedback wxFeedback)
    {
        return toAjax(wxFeedbackService.updateWxFeedback(wxFeedback));
    }

    /**
     * 删除反馈管理
     */
    @RequiresPermissions("system:feedback:remove")
    @Log(title = "反馈管理", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(wxFeedbackService.deleteWxFeedbackByIds(ids));
    }
}
