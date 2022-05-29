package com.ruoyi.framework.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectionException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;


/**
 * 创建自动填充策略的
 * @author 23909
 */
public class MyDataHandler implements MetaObjectHandler {
  // 插入时的填充策略
  @Override
  public void insertFill(MetaObject metaObject) {
    // 创建最后保存，（若存在则不更改，反之则更改）
    // 字段检测（此处以get,set方法作为验证，都存在则认为有此字段，个人需求，
    // 若确保一定有，可以不写）
    if (metaObject.hasGetter("lastSave") && metaObject.hasSetter("lastSave")){
      // 获取旧值
      Object lastSave = metaObject.getValue("lastSave");
      // 判断旧值不存在，或者是我想要的类型
      if (Objects.isNull(lastSave) || lastSave instanceof String){
        // 旧值的合法性检测
        if (!"01".equals(lastSave) && !"02".equals(lastSave)){
          // 不合法的值默认为没有，则手动设置（存在且合法则自动跳过，不填充）
          setFieldValByName("lastSave", "01",metaObject);
        }

      }
    }
    // 创建“创建时间”
    // 字段检测
    if ( metaObject.hasGetter("createTime") && metaObject.hasSetter("createTime")){
      try {
        // 设置默认值（也无需求，不需要检测是否存在）
        setFieldValByName("createTime", new Date(),metaObject);
      }catch (ReflectionException e){
        setFieldValByName("createTime", Timestamp.valueOf(LocalDateTime.now()),metaObject);
      }

    }

  }

  @Override
  public void updateFill(MetaObject metaObject) {
    // 填充 “修改时间”
    // 字段检测
    if ( metaObject.hasGetter("updateTime") && metaObject.hasSetter("updateTime")){
      // 设置默认值（业务确定，不需要检测是否存在，直接更新）
      try {
        setFieldValByName("updateTime", new Date(),metaObject);
      }catch (ReflectionException e){
        setFieldValByName("updateTime", Timestamp.valueOf(LocalDateTime.now()),metaObject);
      }

    }
  }
}