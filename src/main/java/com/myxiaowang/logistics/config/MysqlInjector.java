package com.myxiaowang.logistics.config;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.extension.injector.methods.additional.AlwaysUpdateSomeColumnById;
import com.baomidou.mybatisplus.extension.injector.methods.additional.InsertBatchSomeColumn;
import com.baomidou.mybatisplus.extension.injector.methods.additional.LogicDeleteByIdWithFill;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 实现自定义的批量删除增加和修改功能
 */
@Configuration
public class MysqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        //获取父类选装容器
        List<AbstractMethod> methods=super.getMethodList(mapperClass);
        //添加选装件
        methods.add(new InsertBatchSomeColumn());
        methods.add(new AlwaysUpdateSomeColumnById());
        methods.add(new LogicDeleteByIdWithFill());
        return methods;
    }
}
