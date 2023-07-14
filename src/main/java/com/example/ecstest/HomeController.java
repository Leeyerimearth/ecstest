package com.example.ecstest;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;

@RestController
public class HomeController {

    @GetMapping("/home")
    public String home() {
        return "hello world";
    }

    @GetMapping("/")
    public String healthyCheck() {

        return "healthy!";
    }

    @PostMapping("/upload")
    public String upload(@RequestBody MultipartFile imgFile) throws IOException, InterruptedException {

        Toolkit toolKit = Toolkit.getDefaultToolkit();
        MediaTracker mtracker = new MediaTracker(new Container());
        Image image = toolKit.createImage(imgFile.getBytes());
        mtracker.addImage(image, 1);
        mtracker.waitForID(1);

        System.out.println("이미지 가로 사이즈 : " + image.getWidth(null));
        System.out.println("이미지 세로 사이즈 : " + image.getHeight(null));

        String key = imgFile.getOriginalFilename();
        imageFileUploadToS3("cloudfronttutorialyerim", key, imgFile.getContentType(), imgFile.getBytes());

        return "upload complete !";
    }

    @GetMapping("/getImage")
    public String getImageUrl(@RequestParam String key) {
        Region region = Region.AP_NORTHEAST_2;
        S3Client s3 = S3Client.builder()
                .region(region)
                .build();
        GetUrlRequest request = GetUrlRequest.builder()
                .bucket("cloudfronttutorialyerim")
                .key(key)
                .build();

        String cfUrl = "https://d2v37tmbf353ng.cloudfront.net/" + key;
        URL url = s3.utilities().getUrl(request);

        //return url.toString();
        return cfUrl;
    }

    public void imageFileUploadToS3(String bucket, String key, String contentType, byte[] fileData) {
        Region region = Region.AP_NORTHEAST_2;
        S3Client s3 = S3Client.builder()
                .region(region)
                .build();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .build();

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        s3.deleteObject(deleteObjectRequest);
        s3.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromByteBuffer(ByteBuffer.wrap(fileData)));
    }
}
