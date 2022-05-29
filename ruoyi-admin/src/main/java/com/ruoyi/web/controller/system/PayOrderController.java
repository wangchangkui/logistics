package com.ruoyi.web.controller.system;

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
import com.ruoyi.system.domain.PayOrder;
import com.ruoyi.system.service.IPayOrderService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 支付管理Controller
 * 
 * @author 23909
 * @date 2022-02-26
 */
@Controller
@RequestMapping("/system/order")
public class PayOrderController extends BaseController
{
    private String prefix = "system/order";

    @Autowired
    private IPayOrderService payOrderService;

    @RequiresPermissions("system:order:view")
    @GetMapping()
    public String order() {
        return prefix + "/order";
    }

    /**
     * 查询支付管理列表
     */
    @RequiresPermissions("system:order:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(PayOrder payOrder) {
        startPage();
        List<PayOrder> list = payOrderService.selectPayOrderList(payOrder);
        return getDataTable(list);
    }

    /**
     * 导出支付管理列表
     */
    @RequiresPermissions("system:order:export")
    @Log(title = "支付管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(PayOrder payOrder) {
        List<PayOrder> list = payOrderService.selectPayOrderList(payOrder);
        ExcelUtil<PayOrder> util = new ExcelUtil<PayOrder>(PayOrder.class);
        return util.exportExcel(list, "支付管理数据");
    }

    /**
     * 新增支付管理
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存支付管理
     */
    @RequiresPermissions("system:order:add")
    @Log(title = "支付管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(PayOrder payOrder) {
        return toAjax(payOrderService.insertPayOrder(payOrder));
    }

    /**
     * 修改支付管理
     */
    @RequiresPermissions("system:order:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap) {
        PayOrder payOrder = payOrderService.selectPayOrderById(id);
        mmap.put("payOrder", payOrder);
        return prefix + "/edit";
    }

    /**
     * 修改保存支付管理
     */
    @RequiresPermissions("system:order:edit")
    @Log(title = "支付管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(PayOrder payOrder) {
        return toAjax(payOrderService.updatePayOrder(payOrder));
    }

    /**
     * 删除支付管理
     */
    @RequiresPermissions("system:order:remove")
    @Log(title = "支付管理", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(payOrderService.deletePayOrderByIds(ids));
    }
}
