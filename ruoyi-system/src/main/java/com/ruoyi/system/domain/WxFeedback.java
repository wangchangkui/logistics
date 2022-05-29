package com.ruoyi.system.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 反馈管理对象 wx_feedback
 * 
 * @author jandmla
 * @date 2022-03-24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class WxFeedback extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** ID主键 */
    private Integer id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private String userId;

    /** 标题 */
    @Excel(name = "标题")
    private String title;

    /** 类型 */
    @Excel(name = "类型")
    private Integer type;

    /** 图片ID */
    @Excel(name = "图片ID")
    private Long fileId;

    /** 图片连接 */
    @Excel(name = "图片连接")
    private String fileUrl;

    /** 反馈类容 */
    @Excel(name = "反馈类容")
    private String content;

    /** 反馈状态 */
    @Excel(name = "反馈状态")
    private Integer status;

    public void setId(Integer id) 
    {
        this.id = id;
    }

    public Integer getId() 
    {
        return id;
    }
    public void setUserId(String userId) 
    {
        this.userId = userId;
    }

    public String getUserId() 
    {
        return userId;
    }
    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }
    public void setType(Integer type) 
    {
        this.type = type;
    }

    public Integer getType() 
    {
        return type;
    }
    public void setFileId(Long fileId) 
    {
        this.fileId = fileId;
    }

    public Long getFileId() 
    {
        return fileId;
    }
    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("title", getTitle())
            .append("type", getType())
            .append("fileId", getFileId())
            .append("content", getContent())
            .append("createTime", getCreateTime())
            .append("status", getStatus())
            .toString();
    }
}
