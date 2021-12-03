package com.ecom.carstore.service;
import com.ecom.carstore.domain.Voiture;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.ecom.carstore.repository.VoitureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@Transactional
public class OssService {

    @Autowired
    private VoitureRepository voitureRepository;

    public static final String endpoint = "oss-eu-central-1.aliyuncs.com";

    public static final String accessKeyId = "LTAI5t5YZa6tzhSUbAyXWK1C";

    public static final String accessKeySecret = "MpX0NYVM0miTEerlaOl1q0uUOzQInJ";

    public static final String bucket = "cars-store";

    //uploader une image a partir d'un MultipartFile vient du front
    public String uploadImageToServer(MultipartFile imageFile,Long idVoiture,int n) {

        String filename = idVoiture + ";" + n;
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
        storeURL(idVoiture,n,url);
        return url;
    }

    //uploader une image a partir d'un path local
    public String uploadImageFileFromPath( String urlLocal,Long idVoiture,int n){

        File file = new File(urlLocal);
        String filename = idVoiture + ";" + n;
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentType("image/jpg");
        meta.setContentLength(file.length());
        try {
            InputStream in = new FileInputStream(file);
            ossClient.putObject(bucket, filename,in,meta);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = "https://" + bucket + "." + endpoint + "/" +filename;
        Voiture voiture = voitureRepository.getById(idVoiture);
        storeURL(idVoiture,n,url);
        return url;
    }

    public MultipartFile  downloadImageFromServer(String url) {
        return null;
    }

    public String getServerImageUrl(int idVoiture,int n){
        return null;
    }

    private void storeURL(Long idVoiture,int n,String url){
        Voiture voiture = voitureRepository.getById(idVoiture);
        switch (n){
            case 1:
                voiture.setImage1(url);
                break;
            case 2:
                voiture.setImage2(url);
                break;
            case 3:
                voiture.setImage3(url);
                break;
            default:
                break;
        }
        voitureRepository.save(voiture);
    }
}

