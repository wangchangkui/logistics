package com.ruoyi.web.controller.wx.tx;

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
import com.ruoyi.system.domain.Withdraw;
import com.ruoyi.system.service.IWithdrawService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 提现管理Controller
 * 
 * @author 23909
 * @date 2022-03-10
 */
@Controller
@RequestMapping("/system/tx")
public class WithdrawController extends BaseController {
    private String prefix = "system/tx";

    @Autowired
    private IWithdrawService withdrawService;

    @RequiresPermissions("system:tx:view")
    @GetMapping()
    public String tx()
    {
        return prefix + "/tx";
    }

    /**
     * 查询提现管理列表
     */
    @RequiresPermissions("system:tx:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Withdraw withdraw)
    {
        startPage();
        List<Withdraw> list = withdrawService.selectWithdrawList(withdraw);
        return getDataTable(list);
    }

    /**
     * 导出提现管理列表
     */
    @RequiresPermissions("system:tx:export")
    @Log(title = "提现管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Withdraw withdraw)
    {
        List<Withdraw> list = withdrawService.selectWithdrawList(withdraw);
        ExcelUtil<Withdraw> util = new ExcelUtil<Withdraw>(Withdraw.class);
        return util.exportExcel(list, "提现管理数据");
    }

    /**
     * 新增提现管理
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存提现管理
     */
    @RequiresPermissions("system:tx:add")
    @Log(title = "提现管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Withdraw withdraw)
    {
        return toAjax(withdrawService.insertWithdraw(withdraw));
    }

    /**
     * 修改提现管理
     */
    @RequiresPermissions("system:tx:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        Withdraw withdraw = withdrawService.selectWithdrawById(id);
        mmap.put("withdraw", withdraw);
        return prefix + "/edit";
    }

    /**
     * 修改保存提现管理
     */
    @RequiresPermissions("system:tx:edit")
    @Log(title = "提现管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Withdraw withdraw)
    {
        return toAjax(withdrawService.updateWithdraw(withdraw));
    }

    /**
     * 删除提现管理
     */
    @RequiresPermissions("system:tx:remove")
    @Log(title = "提现管理", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(withdrawService.deleteWithdrawByIds(ids));
    }

    /**
     * 拒绝提现管理
     */
    @RequiresPermissions("system:tx:refuse")
    @Log(title = "提现管理", businessType = BusinessType.REFUSE)
    @PostMapping( "/refuse/{id}")
    @ResponseBody
    public AjaxResult refuse(@PathVariable Long id) {
        return toAjax(withdrawService.refuseOrder(id));
    }

    /**
     * 同意提现管理
     */
    @RequiresPermissions("system:tx:agree")
    @Log(title = "提现管理", businessType = BusinessType.AGREE)
    @PostMapping( "/agree/{id}")
    @ResponseBody
    public AjaxResult agree(@PathVariable Long id) {
        return toAjax(withdrawService.agreeOrder(id));
    }
}
