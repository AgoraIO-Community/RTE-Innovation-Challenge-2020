package org.daigua.crushonmeetings.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class UploadOSS {

    private static final Logger LOG = LoggerFactory.getLogger(UploadOSS.class.getClass());

    private static final String endpoint = "http://oss-cn-beijing.aliyuncs.com";

    private static final String accessKeyId = ""; // Confidential Information by 807852007@qq.com
    private static final String accessKeySecret = ""; // Confidential Information by 807852007@qq.com

    public static boolean uploadByInputStream(InputStream fis, String fileName) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        InputStream inputStream = fis;
        try {
            ossClient.putObject("cmz-daigua", fileName, inputStream);
            return true;
        } catch (Exception e) {
            LOG.info(e.toString());
            return false;
        } finally {
            ossClient.shutdown();
        }
    }

    public static boolean uploadByFile(String filepath, String fileName) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            ossClient.putObject("cmz-daigua", fileName, new File(filepath));
            return true;
        } catch (Exception e) {
            LOG.info(e.toString());
            return false;
        } finally {
            ossClient.shutdown();
        }
    }
}
