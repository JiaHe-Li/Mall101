package com.mall.thirdparty;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest
class MallThirdPartyApplicationTests {

    @Test
    void contextLoads() {
    }

    @Resource
    private OSSClient ossClient;

    @Test
    public void testUpload() throws FileNotFoundException {

        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\lee\\Downloads\\e09ce1e70ec5d7cd6e81d6aa831f04d9.jpeg");
        PutObjectRequest putObjectRequest= new PutObjectRequest("gulixueyuandemo","haha2.jpg", fileInputStream);
        ossClient.putObject(putObjectRequest);
        ossClient.shutdown();
    }

}
