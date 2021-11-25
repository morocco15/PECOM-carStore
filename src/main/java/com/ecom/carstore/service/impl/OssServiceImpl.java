package com.ecom.carstore.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.ecom.carstore.OssConstant;
import com.ecom.carstore.repository.VoitureRepository;
import com.ecom.carstore.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@Transactional
public class OssServiceImpl implements OssService, OssConstant {

    @Autowired
    private VoitureRepository voitureRepository;

    //adresse d'accès---à modifier
    public static final String endpoint = "oss-cn-beijing.aliyuncs.com";

    //demande KEY_ID---à modifier
    public static final String accessKeyId = "LTAIFrerFxxxxxxEwJfNSPoL";

    //demande KEY_SECRET ---à modifier
    public static final String accessKeySecret = "jG46wbefAxxxxxx1OQuyl8MLcuGoO5";

    //espace de stockage ---à modifier
    public static final String bucket = "nicle-oss-bucket";

    @Override
    public void uploadImageToServer(MultipartFile imageFile) {

        int i = imageFile.getOriginalFilename().lastIndexOf(".");
        String suffix = imageFile.getOriginalFilename().substring(i);
        String uuid = UUID.randomUUID().toString();
        String datePath = "Angular";
        String filename = datePath + "/" + uuid + suffix;
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            ossClient.putObject(bucket, filename,imageFile.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ossClient.shutdown();
        String url = "https://" + bucket + "." + endpoint + "/" + filename;

        //méthode à moodifier
        voitureRepository.insertImage(url);
    }

    @Override
    public MultipartFile  downloadImageFromServer(String url) {

        return null;
    }
}
