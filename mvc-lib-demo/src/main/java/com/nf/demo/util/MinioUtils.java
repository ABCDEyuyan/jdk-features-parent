package com.nf.demo.util;

import io.minio.*;
import io.minio.http.Method;

import java.io.InputStream;

public class MinioUtils {
    private static MinioClient client = MinioClientUtils.getMinioClient();

    public static boolean exitsBucket(String bucket) {
        boolean found = false;
        try {
            found = client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        } catch (Exception e) {
            throw new RuntimeException("minio桶创建失败");
        }
        return found;
    }

    public static InputStream downloadStream(String bucket, String objectName) {
        try {
            return  client.getObject(GetObjectArgs.builder().bucket(bucket).object(objectName).build());
        } catch (Exception e) {
            throw new RuntimeException("从minio服务器获取文件失败", e);
        }
    }

    public static String putObjectStream(InputStream inputStream, String fileName, String bucket) {
        try {
            validBucket(bucket);

            client.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket).object(fileName).stream(inputStream, inputStream.available(), -1).build());
            inputStream.close();
            return getObjectUrl(bucket, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getObjectUrl(String bucket, String objectName) {
        return getObjectUrl(bucket, objectName, Method.GET);
    }

    public static String getObjectUrl(String bucket, String objectName,Method method) {
        try {
            return client.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .bucket(bucket).object(objectName).method(method).build());
        } catch (Exception e) {
            throw new RuntimeException("获取文件路径失败", e);
        }
    }

    private static void validBucket(String bucket) {
        boolean bucketExsit = exitsBucket(bucket);
        if (!bucketExsit) {
            throw new RuntimeException(bucket + "-不存在");
        }
    }
}
