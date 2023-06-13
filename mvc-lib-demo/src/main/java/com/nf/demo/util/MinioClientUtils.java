package com.nf.demo.util;

import io.minio.MinioClient;

public class MinioClientUtils {

    private static String domainUrl = "http://localhost:9000";

    private static String accessKey="heomSzxWE3xUMuZNFH20";

    private static String secretKey="P1chfRwfrdIo4hSApUolfrekFFzYUiCjUE7qeec4";

    private  static MinioClient minioClient;

    static {
        minioClient = MinioClient.builder()
                .endpoint(domainUrl)
                .credentials(accessKey, secretKey)
                .build();
    }

    public static MinioClient getMinioClient() {
        return minioClient;
    }
}
