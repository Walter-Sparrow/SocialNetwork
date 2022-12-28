package com.dataart.secondmonth.service.image.util;

import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;

public interface ImageTransformer {

    BufferedImage transform(MultipartFile image);

}
