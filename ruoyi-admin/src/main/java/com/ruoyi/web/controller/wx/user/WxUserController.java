package com.ruoyi.web.controller.wx.user;

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
import com.ruoyi.system.domain.WxUser;
import com.ruoyi.system.service.IWxUserService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 微信用户信息Controller
 * 
 * @author 23909
 * @date 2022-03-10
 */
@Controller
@RequestMapping("/system/wx_user")
public class WxUserController extends BaseController
{
    private String prefix = "system/wx_user";

    @Autowired
    private IWxUserService wxUserService;

    @RequiresPermissions("system:wx_user:view")
    @GetMapping()
    public String wx_user()
    {
        return prefix + "/wx_user";
    }

    /**
     * 查询微信用户信息列表
     */
    @RequiresPermissions("system:wx_user:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(WxUser wxUser)
    {
        startPage();
        List<WxUser> list = wxUserService.selectWxUserList(wxUser);
        return getDataTable(list);
    }

    /**
     * 导出微信用户信息列表
     */
    @RequiresPermissions("system:wx_user:export")
    @Log(title = "微信用户信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(WxUser wxUser)
    {
        List<WxUser> list = wxUserService.selectWxUserList(wxUser);
        ExcelUtil<WxUser> util = new ExcelUtil<WxUser>(WxUser.class);
        return util.exportExcel(list, "微信用户信息数据");
    }

    /**
     * 新增微信用户信息
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存微信用户信息
     */
    @RequiresPermissions("system:wx_user:add")
    @Log(title = "微信用户信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(WxUser wxUser)
    {
        return toAjax(wxUserService.insertWxUser(wxUser));
    }

    /**
     * 修改微信用户信息
     */
    @RequiresPermissions("system:wx_user:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        WxUser wxUser = wxUserService.selectWxUserById(id);
        mmap.put("wxUser", wxUser);
        return prefix + "/edit";
    }

    /**
     * 修改保存微信用户信息
     */
    @RequiresPermissions("system:wx_user:edit")
    @Log(title = "微信用户信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(WxUser wxUser)
    {
        return toAjax(wxUserService.updateWxUser(wxUser));
    }

    /**
     * 删除微信用户信息
     */
    @RequiresPermissions("system:wx_user:remove")
    @Log(title = "微信用户信息", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(wxUserService.deleteWxUserByIds(ids));
    }
}
