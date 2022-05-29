package com.ruoyi.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 轮播图对象 carousel
 * 
 * @author jandmla
 * @date 2022-03-10
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("carousel")
public class Carousel extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID 主键  自增 */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /** 图片URL */
    @TableField("header_url")
    @Excel(name = "图片URL")
    private String headerUrl;

    /** 是否展示 */
    @TableField("is_show")
    @Excel(name = "是否展示")
    private Long shows;

    /** 标题 */
    @TableField("title")
    @Excel(name = "标题")
    private String title;

    /** 跳转地址 */
    @TableField("url")
    @Excel(name = "跳转地址")
    private String url;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setHeaderUrl(String headerUrl) 
    {
        this.headerUrl = headerUrl;
    }

    public String getHeaderUrl() 
    {
        return headerUrl;
    }
    public void setShow(Long show) 
    {
        this.shows = show;
    }

    public Long getShow() 
    {
        return shows;
    }
    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }
    public void setUrl(String url) 
    {
        this.url = url;
    }

    public String getUrl() 
    {
        return url;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("headerUrl", getHeaderUrl())
            .append("show", getShow())
            .append("title", getTitle())
            .append("url", getUrl())
            .toString();
    }
}
