package com.myxiaowang.logistics.util.OSS;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.common.utils.DateUtil;
import com.aliyun.oss.model.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2021年12月24日 17:51:00
 */
public class OSSClient implements AutoCloseable {
    private OSS build = null;

    private OSSClient(OSSBuilder ossbuilder) {
        String END_POINT = ossbuilder.END_POINT;
        String ACCESS_KEY_ID = ossbuilder.ACCESS_KEY_ID;
        String ACCESS_KEY_SECRET = ossbuilder.ACCESS_KEY_SECRET;
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSupportCname(true);
        build = new OSSClientBuilder().build(END_POINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
    }


    /**
     * 获取文件的访问URL
     * 一小时后文件自动过期 不能访问
     *
     * @param BucketName Bucket名称
     * @param ObjectName 文件名称
     * @return 访问的URL
     */
    public URL fileUrl(String BucketName, String ObjectName) {
        // 设置签名URL过期时间为3600秒（1小时）。
        Date expiration = new Date(System.currentTimeMillis() + 3600 * 1000);
        return build.generatePresignedUrl(BucketName, ObjectName, expiration);
    }

    /**
     * 指定过期时间的文件访问
     *
     * @param BucketName BucketName
     * @param ObjectName 文件名称
     * @param expiration 访问过期时间
     * @return 结果
     */
    public URL fileUrl(String BucketName, String ObjectName, Date expiration) {
        // 设置签名URL过期时间为3600秒（1小时）。
        return build.generatePresignedUrl(BucketName, ObjectName, expiration);
    }


    /**
     * 断点续传的方式下载问年间
     *
     * @param BucketName BucketName
     * @param ObjectName 文件名称
     * @param FilePath   下载文件地址
     */
    public void multipleDownload(String BucketName, String ObjectName, String FilePath) {
        DownloadFileRequest downloadFileRequest = new DownloadFileRequest(BucketName, ObjectName);
        downloadFileRequest.setDownloadFile(FilePath);
        downloadFileRequest.setPartSize(1024 * 1024);
        downloadFileRequest.setTaskNum(10);
        downloadFileRequest.setEnableCheckpoint(true); //开启断点续传
        try {
            build.downloadFile(downloadFileRequest);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 简单的文件下载
     *
     * @param BucketName BucketName
     * @param ObjectName 文件名称
     * @param target     下载到的文件位置
     */
    public void downloadFile(String BucketName, String ObjectName, File target) {
        build.getObject(new GetObjectRequest(BucketName, ObjectName), target);
    }

    /**
     * 断点续传 大文件专用
     *
     * @param BucketName Bucket名称
     * @param ObjectName 文件名称
     * @param filePath   文件地址
     */
    public void muiltFileUpload(String BucketName, String ObjectName, String filePath) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("text/plain");
        UploadFileRequest uploadFileRequest = new UploadFileRequest(BucketName, ObjectName);
        uploadFileRequest.setUploadFile(filePath); //文件位置
        uploadFileRequest.setTaskNum(10);  // 开启线程数量
        uploadFileRequest.setPartSize(10 * 1024 * 1024); //每个分片的大小
        uploadFileRequest.setEnableCheckpoint(true); // 开启断点上传
        uploadFileRequest.setCheckpointFile("C:\\Users\\Myxiaowang\\Desktop\\r.txt"); //断点续传结果存放文件
        uploadFileRequest.setObjectMetadata(objectMetadata);
        uploadFileRequest.setCallback(null);
        try {
            build.uploadFile(uploadFileRequest);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 分片上传文件
     *
     * @param BucketName BucketName
     * @param ObjectName 文件名称
     * @param filePath   文件地址
     */
    public void initMuiltPartUpload(String BucketName, String ObjectName, String filePath) {
        InitiateMultipartUploadRequest initiateMultipartUploadRequest = new InitiateMultipartUploadRequest(BucketName, ObjectName); // 分片请求
        InitiateMultipartUploadResult initiateMultipartUploadResult = build.initiateMultipartUpload(initiateMultipartUploadRequest); // 初始化分片
        String uploadId = initiateMultipartUploadResult.getUploadId(); // 获取分片id
        ArrayList<PartETag> partETags = new ArrayList<>();
        long partSize = 10 * 1024 * 1024;
        File file = new File(filePath);
        int partCount = (int) (file.length() / partSize); // 获取分片数量
        // 如果文件的长度比分片大小要小 则增加一个分片
        if (file.length() % partSize != 0) {
            partCount++;
        }
        // 分段上传文件
        for (int i = 0; i < partCount; i++) {
            long start = i * partSize; // 起始位置
            long curPartSize = (i + 1 == partCount) ? (file.length() - start) : partSize; // 当前位置
            try (InputStream inputStream = new FileInputStream(file)) {
                inputStream.skip(start); // 跳过多少位置
                UploadPartRequest p = new UploadPartRequest(BucketName, ObjectName);
                p.setUploadId(uploadId);
                p.setInputStream(inputStream);
                p.setPartSize(curPartSize);
                p.setPartNumber(i + 1);
                UploadPartResult uploadPartResult = build.uploadPart(p);
                partETags.add(uploadPartResult.getPartETag());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        // 合并文件
        CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(BucketName, ObjectName, uploadId, partETags);
        // 处理分片之后的文件
        build.completeMultipartUpload(completeMultipartUploadRequest);
    }


    /**
     * 简单的上传文件
     *
     * @param BucketName BucketName
     * @param ObjectName 文件名称
     * @param file       文件
     */
    public void uploadFile(String BucketName, String ObjectName, File file) {
        try (InputStream inputStream = new FileInputStream(file)) {
            build.putObject(BucketName, ObjectName, inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件
     *
     * @param bucketName BucketName
     * @param ObjectName 文件名称
     */
    public void deleteFile(String bucketName, String ObjectName) {
        build.deleteObject(bucketName, ObjectName);
    }


    /**
     * 批量删除文件
     *
     * @param bucketName  BucketName
     * @param ObjectNames 文件名称
     */
    public void deleteFiles(String bucketName, List<String> ObjectNames) {
        build.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(ObjectNames).withEncodingType("utf-8"));
    }


    /**
     * 获取指定前缀的文件名称集合
     *
     * @param prefix     前缀名称
     * @param BucketName BucketName
     * @return 文件集合
     */
    public List<String> getFilesByPrefix(String prefix, String BucketName) {
        return build.listObjects(new ListObjectsRequest(BucketName).withPrefix(prefix))
                .getObjectSummaries()
                .stream()
                .map(OSSObjectSummary::getKey)
                .collect(Collectors.toList());
    }


    /**
     * 获取指定数量的文件
     *
     * @param BucketName BucketName
     * @param size       需要的文件数量
     * @return 文件的名称集合
     */
    public List<String> getFilesBySize(String BucketName, int size) {
        return build.listObjects(new ListObjectsRequest(BucketName).withMaxKeys(size))
                .getObjectSummaries()
                .stream()
                .map(OSSObjectSummary::getKey)
                .collect(Collectors.toList());
    }


    /**
     * 获取指定前缀的所有文件
     * 如果前缀为空 则查询所有文件
     *
     * @param BucketName Bucket名
     * @param prefix     前缀 允许为空
     * @return 文件名称集合
     */
    public List<String> listFiles(String BucketName, String prefix) {
        ObjectListing objectListing = build.listObjects(BucketName, Optional.of(prefix).orElse(""));
        return objectListing.getObjectSummaries().stream().map(OSSObjectSummary::getKey).collect(Collectors.toList());
    }


    /**
     * 获取Bucket地域
     *
     * @param BucketName Bucket名称
     * @return 地域名称
     */
    public String BucketLocation(String BucketName) {
        return build.getBucketLocation(BucketName);
    }

    /**
     * 判断Bucket是否存在
     *
     * @param BucketName Bucket名称
     * @return 判断是否存在
     */
    public boolean BucketExists(String BucketName) {
        return build.doesBucketExist(BucketName);
    }


    /**
     * 获取所有的Bucket
     *
     * @return Bucket的名称的集合
     */
    public List<String> getBucketList() {
        List<Bucket> buckets = build.listBuckets();
        return buckets.stream().map(Bucket::getName).collect(Collectors.toList());
    }

    /**
     * 获取指定前缀的Bucket
     *
     * @param prefix 前缀
     * @return Bucket的名称集合
     */
    public List<String> getBucketList(String prefix) {
        ListBucketsRequest listBucketsRequest = new ListBucketsRequest();
        // 列举指定前缀的存储空间。
        listBucketsRequest.setPrefix(prefix);
        List<Bucket> bucketList = build.listBuckets(listBucketsRequest).getBucketList();
        return bucketList.stream().map(Bucket::getName).collect(Collectors.toList());
    }

    /**
     * 获取指定个数的Bucket名称
     *
     * @param size 查询的数量
     * @return Bucket名称集合
     */
    public List<String> getBucketList(Integer size) {
        ListBucketsRequest listBucketsRequest = new ListBucketsRequest();
        // 列举指定前缀的存储空间。
        listBucketsRequest.setMaxKeys(size);
        List<Bucket> bucketList = build.listBuckets(listBucketsRequest).getBucketList();
        return bucketList.stream().map(Bucket::getName).collect(Collectors.toList());
    }

    /**
     * 创建Bucket
     *
     * @param bucketName Bucket名称
     */
    public void createBucket(String bucketName) {
        CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
        createBucketRequest.setStorageClass(StorageClass.Standard); //设置为标准存储
        createBucketRequest.setDataRedundancyType(DataRedundancyType.LRS); // 设置未本地冗余
        createBucketRequest.setCannedACL(CannedAccessControlList.PublicReadWrite); // 设置为可以读写
        build.createBucket(createBucketRequest);
    }


    /**
     * 获取元信息
     *
     * @param Bucket      Bucket对象
     * @param ProjectName 项目名称
     * @return ObjectMetadata 类存储所有源信息
     */
    public ObjectMetadata getMessageInfo(String Bucket, String ProjectName) {
        return build.getObjectMetadata(Bucket, ProjectName);
    }

    /**
     * 修改元文件信息
     *
     * @param Bucket        Bucket对象
     * @param ObjectName    项目名称
     * @param property      属性
     * @param propertyValue 属性值
     * @throws ParseException 解析异常
     */
    public void changeMessage(String Bucket, String ObjectName, String property, String propertyValue) throws ParseException {
        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(Bucket, ObjectName, Bucket, ObjectName);
        //文件源信息
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentDisposition("attachment; filename=\"DownloadFilename\"");
        // 设置文件的上传内容类型
        objectMetadata.setContentType("text/plan");
        // 设置内容被下载的时候网页的缓存行为
        objectMetadata.setCacheControl("Download Action");
        // 设置缓存过期时间，格式是格林威治时间（GMT）。
        objectMetadata.setExpirationTime(DateUtil.parseIso8601Date(LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        // 设置下载时候的编码
        objectMetadata.setContentEncoding("UTF-8");
        objectMetadata.addUserMetadata(property, propertyValue);
        copyObjectRequest.setNewObjectMetadata(objectMetadata);
        build.copyObject(copyObjectRequest);
    }

    /**
     * 设置文件的源信息
     *
     * @param message     源信息
     * @param Bucket      OSSBucket对象
     * @param projectName 项目的名称
     * @throws ParseException 解析
     */
    public void SetMessageMd5(String message, String Bucket, String projectName) throws ParseException {
        //文件源信息
        ObjectMetadata objectMetadata = new ObjectMetadata();
        // 2次加密 先转md5 在base64
        String msg = BinaryUtil.toBase64String(BinaryUtil.calculateMd5(message.getBytes(StandardCharsets.UTF_8)));
        // 再次md5
        objectMetadata.setContentMD5(msg);
        // 设置文件下载的长度
        objectMetadata.setContentLength(msg.length());
        // 设置文件下载的名称
        objectMetadata.setContentDisposition("attachment; filename=\"DownloadFilename\"");
        // 设置文件的上传内容类型
        objectMetadata.setContentType("text/plan");
        // 设置内容被下载的时候网页的缓存行为
        objectMetadata.setCacheControl("Download Action");
        // 设置缓存过期时间，格式是格林威治时间（GMT）。
        objectMetadata.setExpirationTime(DateUtil.parseIso8601Date(LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        // 设置下载时候的编码
        objectMetadata.setContentEncoding("UTF-8");
        build.putObject(Bucket, projectName, new ByteArrayInputStream(msg.getBytes(StandardCharsets.UTF_8)), objectMetadata);
    }

    @Override
    public void close() throws Exception {
        build.shutdown();
    }


    @Component
    @ConfigurationProperties(prefix = "aliyun")
    public static class OSSBuilder {
        public String END_POINT;
        public String ACCESS_KEY_ID;
        public String ACCESS_KEY_SECRET;

        public OSSBuilder setEND_POINT(String endPoint) {
            this.END_POINT = endPoint;
            return this;
        }

        public OSSBuilder setACCESS_KEY_ID(String accessKey) {
            this.ACCESS_KEY_ID = accessKey;
            return this;
        }

        public OSSBuilder setACCESS_KEY_SECRET(String accessKeySecret) {
            this.ACCESS_KEY_SECRET = accessKeySecret;
            return this;
        }

        public OSSClient buidOSS(OSSBuilder os) {
            return new OSSClient(os);
        }

    }
}
