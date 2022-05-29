package com.ruoyi.system.domain.wx;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

/**
 * @author Jandmla
 * @version 1.0
 * @description TODO
 * @data 2022/3/23 21:29
 */
@Data
@Accessors(chain = true)
@TableName("wx_file")
public class WxFile {

    /** 主键 自增 */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /** 文件名 */
    @TableField("file_name")
    private String fileName;

    /** 文件保存路径 */
    @TableField("file_path")
    private String filePath;

    /** 文件访问地址 */
    @TableField("file_url")
    private String fileUrl;

    /** 文件上传时间 */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Timestamp createTime;

}
