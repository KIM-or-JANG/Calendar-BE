package com.example.calendar.common.S3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile) throws IOException {
        String s3FileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(multipartFile.getInputStream().available());

        amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objMeta);

        return amazonS3.getUrl(bucket, s3FileName).toString();
    }
    //메타데이터인 객체를 만들어  파일의 크기를 설정
    //파일의 속성과 정보 제공: 메타데이터를 사용하여 파일의 속성을 정의하고 정보를 제공할 수 있습니다. 예를 들어,
    // 파일의 크기를 메타데이터로 설정하면 사용자가 파일의 크기를 쉽게 확인할 수 있습니다.

    //mutipartfile.getInputStream()은 파일의 내용을 읽기 위한 InputStream반환.

    public void delete(String fileUrl) throws IOException {
        String url = fileUrl.substring(50);
        String[] temp = url.split("_");
        String fileKey = temp[0] + "_" + URLDecoder.decode(temp[1], StandardCharsets.UTF_8);
        amazonS3.deleteObject(bucket, fileKey);
    }
}


