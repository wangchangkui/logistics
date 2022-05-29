//package com.ruoyi.web.controller.client;
//
//import java.util.List;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import com.ruoyi.common.annotation.Log;
//import com.ruoyi.common.enums.BusinessType;
//import com.ruoyi.system.domain.Carousel;
//import com.ruoyi.system.service.ICarouselService;
//import com.ruoyi.common.core.controller.BaseController;
//import com.ruoyi.common.core.domain.AjaxResult;
//import com.ruoyi.common.utils.poi.ExcelUtil;
//import com.ruoyi.common.core.page.TableDataInfo;
//
///**
// * 轮播图Controller
// *
// * @author jandmla
// * @date 2022-03-09
// */
//@Controller
//@RequestMapping("/system/carousel")
//public class CarouselController extends BaseController
//{
//    private String prefix = "system/carousel";
//
//    @Autowired
//    private ICarouselService carouselService;
//
//    @RequiresPermissions("system:carousel:view")
//    @GetMapping()
//    public String carousel()
//    {
//        return prefix + "/carousel";
//    }
//
//    /**
//     * 查询轮播图列表
//     */
//    @RequiresPermissions("system:carousel:list")
//    @PostMapping("/list")
//    @ResponseBody
//    public TableDataInfo list(Carousel carousel)
//    {
//        startPage();
//        List<Carousel> list = carouselService.selectCarouselList(carousel);
//        return getDataTable(list);
//    }
//
//    /**
//     * 导出轮播图列表
//     */
//    @RequiresPermissions("system:carousel:export")
//    @Log(title = "轮播图", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    @ResponseBody
//    public AjaxResult export(Carousel carousel)
//    {
//        List<Carousel> list = carouselService.selectCarouselList(carousel);
//        ExcelUtil<Carousel> util = new ExcelUtil<Carousel>(Carousel.class);
//        return util.exportExcel(list, "轮播图数据");
//    }
//
//    /**
//     * 新增轮播图
//     */
//    @GetMapping("/add")
//    public String add()
//    {
//        return prefix + "/add";
//    }
//
//    /**
//     * 新增保存轮播图
//     */
//    @RequiresPermissions("system:carousel:add")
//    @Log(title = "轮播图", businessType = BusinessType.INSERT)
//    @PostMapping("/add")
//    @ResponseBody
//    public AjaxResult addSave(Carousel carousel)
//    {
//        return toAjax(carouselService.insertCarousel(carousel));
//    }
//
//    /**
//     * 修改轮播图
//     */
//    @RequiresPermissions("system:carousel:edit")
//    @GetMapping("/edit/{id}")
//    public String edit(@PathVariable("id") Long id, ModelMap mmap)
//    {
//        Carousel carousel = carouselService.selectCarouselById(id);
//        mmap.put("carousel", carousel);
//        return prefix + "/edit";
//    }
//
//    /**
//     * 修改保存轮播图
//     */
//    @RequiresPermissions("system:carousel:edit")
//    @Log(title = "轮播图", businessType = BusinessType.UPDATE)
//    @PostMapping("/edit")
//    @ResponseBody
//    public AjaxResult editSave(Carousel carousel)
//    {
//        return toAjax(carouselService.updateCarousel(carousel));
//    }
//
//    /**
//     * 删除轮播图
//     */
//    @RequiresPermissions("system:carousel:remove")
//    @Log(title = "轮播图", businessType = BusinessType.DELETE)
//    @PostMapping( "/remove")
//    @ResponseBody
//    public AjaxResult remove(String ids)
//    {
//        return toAjax(carouselService.deleteCarouselByIds(ids));
//    }
//}
