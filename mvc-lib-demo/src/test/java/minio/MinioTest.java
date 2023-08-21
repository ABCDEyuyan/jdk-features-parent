package minio;

import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class MinioTest {
    public static void main(String[] args)
            throws Exception {
        // minioHello();
        // minioStream();
        getObjectUrl();
    }

    private static void minioHello() throws InvalidKeyException, IOException, NoSuchAlgorithmException {
        try {
            // Create a minioClient with the MinIO server playground, its access key and secret key.
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint("http://127.0.0.1:9000")
                            .credentials("0fHsVQbJozJ4gHCqTIMh", "b8KdV04R9D7iXhp5qSJrxBm9GSlKbBuPUD2RT8aK")
                            .build();

            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket("firstbuckets").build());
            if (!found) {

                minioClient.makeBucket(MakeBucketArgs.builder().bucket("firstbuckets").build());
            } else {
                System.out.println("Bucket 'firstbuckets' already exists.");
            }

            //uploadObject:Uploads data from a file to an object(从一个文件中上传数据为minio的对象).
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket("firstbuckets")
                            .object("1.jpg")
                            .filename("e:/image/1.jpg")
                            .build());
            System.out.println(
                    "'e:/image/1.jpg' is successfully uploaded as "
                            + "object '1.jpg' to bucket 'firstbuckets'.");
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e.httpTrace());
        }
    }

    private static void minioStream() throws Exception {


        InputStream inputStream = new FileInputStream("e:/image/2.jpg");
        try {
            // Create a minioClient with the MinIO server playground, its access key and secret key.
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint("http://127.0.0.1:9000")
                            .credentials("0fHsVQbJozJ4gHCqTIMh", "b8KdV04R9D7iXhp5qSJrxBm9GSlKbBuPUD2RT8aK")
                            .build();


            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket("firstbuckets").build());
            if (!found) {

                minioClient.makeBucket(MakeBucketArgs.builder().bucket("firstbuckets").build());
            } else {
                System.out.println("Bucket 'firstbuckets' already exists.");
            }

            //putObject:Uploads data from a stream to an object((从一个流中上传数据为minio的对象)).
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket("firstbuckets")
                            .object("2.jpg")
                            .stream(inputStream, inputStream.available(), -1)
                            .build());

            System.out.println(
                    "'e:/image/2.jpg' is successfully uploaded as "
                            + "object '2.jpg' to bucket 'firstbuckets'.");

        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e.httpTrace());
        } finally {
            inputStream.close();
        }
    }

    private static void getObjectUrl() throws Exception {
        MinioClient minioClient =
                MinioClient.builder()
                        .endpoint("http://127.0.0.1:9000")
                        .credentials("0fHsVQbJozJ4gHCqTIMh", "b8KdV04R9D7iXhp5qSJrxBm9GSlKbBuPUD2RT8aK")
                        .build();
        //用来添加额外的查询字符串的
        Map<String, String> reqParams = new HashMap<String, String>();
        reqParams.put("a", "123");

        GetPresignedObjectUrlArgs objectUrlArgs = GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket("firstbuckets")
                .object("2.jpg")
                .extraQueryParams(reqParams)
                .build();
        //直接在浏览器访问下面的地址，会直接下载文件
        //值是：http://127.0.0.1:9000/firstbuckets/2.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=heomSzxWE3xUMuZNFH20%2F20230613%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20230613T035810Z&X-Amz-Expires=604800&X-Amz-SignedHeaders=host&X-Amz-Signature=3e28164ee3dbc8d07026adca9a026087bd49da6d40b1492bf7e99477d6b4ab31
        String objectUrl = minioClient.getPresignedObjectUrl(objectUrlArgs);
        System.out.println("objectUrl = " + objectUrl);

    }
}