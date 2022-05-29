package com.ruoyi.web.controller.order;

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
import com.ruoyi.system.domain.Orders;
import com.ruoyi.system.service.IOrdersService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 订单管理Controller
 * 
 * @author ruoyi
 * @date 2022-02-23
 */
@Controller
@RequestMapping("/system/orders")
public class OrdersController extends BaseController
{
    private String prefix = "system/orders";

    @Autowired
    private IOrdersService ordersService;

    @RequiresPermissions("system:orders:view")
    @GetMapping()
    public String orders()
    {
        return prefix + "/orders";
    }

    /**
     * 查询订单管理列表
     */
    @RequiresPermissions("system:orders:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Orders orders)
    {
        startPage();
        List<Orders> list = ordersService.selectOrdersList(orders);
        return getDataTable(list);
    }

    /**
     * 导出订单管理列表
     */
    @RequiresPermissions("system:orders:export")
    @Log(title = "订单管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Orders orders)
    {
        List<Orders> list = ordersService.selectOrdersList(orders);
        ExcelUtil<Orders> util = new ExcelUtil<Orders>(Orders.class);
        return util.exportExcel(list, "订单管理数据");
    }

    /**
     * 新增订单管理
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存订单管理
     */
    @RequiresPermissions("system:orders:add")
    @Log(title = "订单管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Orders orders)
    {
        return toAjax(ordersService.insertOrders(orders));
    }

    /**
     * 修改订单管理
     */
    @RequiresPermissions("system:orders:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        Orders orders = ordersService.selectOrdersById(id);
        mmap.put("orders", orders);
        return prefix + "/edit";
    }

    /**
     * 修改保存订单管理
     */
    @RequiresPermissions("system:orders:edit")
    @Log(title = "订单管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Orders orders)
    {
        return toAjax(ordersService.updateOrders(orders));
    }

    /**
     * 删除订单管理
     */
    @RequiresPermissions("system:orders:remove")
    @Log(title = "订单管理", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(ordersService.deleteOrdersByIds(ids));
    }
}
