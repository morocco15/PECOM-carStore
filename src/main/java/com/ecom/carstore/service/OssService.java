package com.ecom.carstore.service;

import org.springframework.web.multipart.MultipartFile;

public interface OssService {


    void uploadImageToServer(MultipartFile imageFile);


    MultipartFile downloadImageFromServer(String url);
}

