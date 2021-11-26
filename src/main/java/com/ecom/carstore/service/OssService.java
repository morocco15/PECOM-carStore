package com.ecom.carstore.service;

import org.springframework.web.multipart.MultipartFile;

public interface OssService {


    String uploadImageToServer(MultipartFile imageFile,int idVoiture,int n);


    MultipartFile downloadImageFromServer(String url);
    String getServerImageUrl(int idVoiture,int n);
    MultipartFile uploadImageFileFromPath(String imagePath,int idVoiture,int n);
}

