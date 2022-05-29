package com.ruoyi.system.QueryDto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月19日 22:27:00
 */
@Data
public class QueryDto <T,V>{
    private T entity;
    private Page<V> page;
}
