package com.ruoyi.web.controller.bean;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.bean.OrderBean;
import com.ruoyi.system.service.bean.OrderBeanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Jandmla
 * @version 1.0
 * @description TODO
 * @data 2022/2/23 11:49
 */
@Controller
public class OrderBeanController {

    private final OrderBeanService orderBeanService;

    public OrderBeanController(OrderBeanService orderBeanService) {
        this.orderBeanService = orderBeanService;
    }

    @RequestMapping("initOrderBean")
    public String initOrderBean(ModelMap modelMap){
        modelMap.put("bean", orderBeanService.initOrderBean(null));
        return "main_v1";
    }

    @RequestMapping("initOrderVO")
    @ResponseBody
    public AjaxResult initOrderVO(){
        return AjaxResult.success(orderBeanService.initOrderVO(null));
    }
}
