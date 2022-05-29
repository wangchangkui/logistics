package com.ruoyi.web.controller.wx.feedback;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.config.ServerConfig;
import com.ruoyi.common.core.domain.resutl.ResponseResult;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.system.domain.WxFeedback;
import com.ruoyi.system.domain.wx.WxFile;
import com.ruoyi.system.service.IWxFeedbackService;
import com.ruoyi.system.service.impl.WxFeedbackServiceImpl;
import com.ruoyi.system.service.wx.WxFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author Jandmla
 * @version 1.0
 * @description TODO
 * @data 2022/3/23 18:12
 */

@RestController
@RequestMapping("/wx/feedback")
public class FeedbackController {

    private final IWxFeedbackService feedbackService;

    private final WxFileService fileService;

    private final ServerConfig serverConfig;

    private static final Logger log = LoggerFactory.getLogger(FeedbackController.class);

    public FeedbackController(WxFileService fileService, ServerConfig serverConfig, IWxFeedbackService feedbackService) {
        this.fileService = fileService;
        this.serverConfig = serverConfig;
        this.feedbackService = feedbackService;
    }

    /**
     * 上传图片
     */
    @PostMapping("/updateImage")
    @ResponseBody
    public ResponseResult<String> updateAvatar(@RequestParam("avatarfile") MultipartFile file) {
        try {
            if (!file.isEmpty()) {
                String avatar = FileUploadUtils.upload(RuoYiConfig.getAvatarPath(), file);
                return ResponseResult.success(avatar);
            }
            return ResponseResult.error("file is empty");
        } catch (Exception e) {
            log.error("图片上传是吧！", e);
            return ResponseResult.error(e.getMessage());
        }
    }

    /**
     * 通用上传请求（单个）
     */
    @PostMapping("/upload")
    @ResponseBody
    public ResponseResult<WxFile> uploadFile(MultipartFile file) throws Exception {
        try {
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();
            String originalFilename = file.getOriginalFilename();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String path = filePath + File.separator + fileName;
            String url = serverConfig.getUrl() + fileName;
            WxFile wxFile = new WxFile().setFilePath(path).setFileName(originalFilename).setFileUrl(url);
            fileService.save(wxFile);
            return ResponseResult.success(wxFile);
        } catch (Exception e) {
            return ResponseResult.error(e.getMessage());
        }
    }

    /**
     * 提交反馈
     */
    @PostMapping("/feedback")
    public ResponseResult<Integer> feedback(@RequestBody WxFeedback feedback){

        return ResponseResult.success(feedbackService.insertWxFeedback(feedback));
    }


}
