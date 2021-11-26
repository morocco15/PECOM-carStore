package com.ecom.carstore.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.ecom.carstore.OssConstant;
import com.ecom.carstore.repository.VoitureRepository;
import com.ecom.carstore.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@Transactional
public class OssServiceImpl implements OssService, OssConstant {

    @Autowired
    private VoitureRepository voitureRepository;

    public static final String endpoint = "oss-eu-central-1.aliyuncs.com";

    public static final String accessKeyId = "LTAI5t5YZa6tzhSUbAyXWK1C";

    public static final String accessKeySecret = "MpX0NYVM0miTEerlaOl1q0uUOzQInJ";

    public static final String bucket = "cars-store";

    @Override
    public String uploadImageToServer(MultipartFile imageFile,int idVoiture,int n) {

        String filename = idVoiture + "/" + n;
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentType("image/jpg");
        try {
            InputStream in = imageFile.getInputStream();
            ossClient.putObject(bucket, filename,in,meta);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ossClient.shutdown();
        String url = "https://" + bucket + "." + endpoint + "/" +filename;

        //méthode à moodifier
        //voitureRepository.insertImage();

        return url;
    }

    @Override
    public MultipartFile  downloadImageFromServer(String url) {

        return null;
    }
    @Override
    public String getServerImageUrl(int idVoiture,int n){
        //voitureRepository.getImageById();
        String url = "https://cars-store.oss-eu-central-1.aliyuncs.com/test/bonneVoiture.jpg";

        return url;
    }
    @Override
    public MultipartFile uploadImageFileFromPath(String imagePath,int idVoiture,int n){
        File file = new File(imagePath);



        return null;
    }
    public  static void main(String[] args) {

        OssService ossService=new OssServiceImpl();


        String resultat = ossService.uploadImageToServer(null,1,2);
        System.out.println("url du image= "+resultat);

    }
}
