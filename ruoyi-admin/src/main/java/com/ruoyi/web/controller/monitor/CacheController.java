package com.ruoyi.web.controller.monitor;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.framework.web.service.CacheService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;
import java.util.Properties;

/**
 * 缓存监控
 * @author 23909
 */
@Controller
@RequestMapping("/monitor/cache")
public class CacheController extends BaseController {

    private final String prefix = "monitor/cache";

    private final CacheService cacheService;

    private final RedisTemplate<String, String> redisTemplate;

    public CacheController(CacheService cacheService, RedisTemplate<String, String> redisTemplate) {
        this.cacheService = cacheService;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 获取 Redis 的系统信息
     * info 参考文档：https://blog.csdn.net/xybelieve1990/article/details/87865549
     * @param mmap model
     * @return 页面名称
     */
    @RequiresPermissions("monitor:cache:view")
    @GetMapping()
    public String cache(ModelMap mmap) {
        Properties info = (Properties) redisTemplate.execute((RedisCallback<Object>) RedisServerCommands::info);
        Object dbSize = Optional.ofNullable(redisTemplate.execute((RedisCallback<Object>) RedisServerCommands::dbSize)).orElse(0);
        if (null != info){
            // 计算占用系统内存
            info.setProperty("used_memory_rss",String.valueOf(Integer.parseInt(info.get("used_memory_rss").toString())/1024));
            // 计算已经使用内存
            info.setProperty("used_memory",Integer.parseInt(info.get("used_memory").toString())/1024 + "K");
            // 计算已经使用 cpu
            info.setProperty("used_cpu_sys_children",String.format("%.2f", Double.parseDouble(info.get("used_cpu_sys_children").toString()))+"%");
            // 计算 内存使用峰值
            info.setProperty("used_memory_peak",String.format("%.3f",Double.parseDouble(info.get("used_memory_peak").toString())/(double)1024/1024) + "MB");
            // 计算系统总内存
            info.setProperty("total_system_memory",Integer.parseInt(info.get("total_system_memory").toString())/1024/1024 + "MB");
        }
        mmap.put("info", info);
        mmap.put("dbSize", dbSize);
        mmap.put("cacheNames", cacheService.getCacheNames());
        return prefix + "/cache";
    }

    @RequiresPermissions("monitor:cache:view")
    @PostMapping("/getNames")
    public String getCacheNames(String fragment, ModelMap mmap)
    {
        mmap.put("cacheNames", cacheService.getCacheNames());
        return prefix + "/cache::" + fragment;
    }

    @RequiresPermissions("monitor:cache:view")
    @PostMapping("/getKeys")
    public String getCacheKeys(String fragment, String cacheName, ModelMap mmap)
    {
        mmap.put("cacheName", cacheName);
        mmap.put("cacheKyes", cacheService.getCacheKeys(cacheName));
        return prefix + "/cache::" + fragment;
    }

    @RequiresPermissions("monitor:cache:view")
    @PostMapping("/getValue")
    public String getCacheValue(String fragment, String cacheName, String cacheKey, ModelMap mmap)
    {
        mmap.put("cacheName", cacheName);
        mmap.put("cacheKey", cacheKey);
        mmap.put("cacheValue", cacheService.getCacheValue(cacheName, cacheKey));
        return prefix + "/cache::" + fragment;
    }

    @RequiresPermissions("monitor:cache:view")
    @PostMapping("/clearCacheName")
    @ResponseBody
    public AjaxResult clearCacheName(String cacheName, ModelMap mmap)
    {
        cacheService.clearCacheName(cacheName);
        return AjaxResult.success();
    }

    @RequiresPermissions("monitor:cache:view")
    @PostMapping("/clearCacheKey")
    @ResponseBody
    public AjaxResult clearCacheKey(String cacheName, String cacheKey, ModelMap mmap)
    {
        cacheService.clearCacheKey(cacheName, cacheKey);
        return AjaxResult.success();
    }

    @RequiresPermissions("monitor:cache:view")
    @GetMapping("/clearAll")
    @ResponseBody
    public AjaxResult clearAll(ModelMap mmap)
    {
        cacheService.clearAll();
        return AjaxResult.success();
    }
}
