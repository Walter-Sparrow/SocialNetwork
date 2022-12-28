package com.dataart.secondmonth.service.image.util;

import lombok.SneakyThrows;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;

public class ProfileImageTransformer implements ImageTransformer {

    private static final Integer DEFAULT_PROFILE_PICTURE_WIDTH = 128;

    private static final Integer DEFAULT_PROFILE_PICTURE_HEIGHT = 128;

    @SneakyThrows
    @Override
    public BufferedImage transform(MultipartFile image) {
        return Thumbnails.of(image.getInputStream())
                .size(DEFAULT_PROFILE_PICTURE_WIDTH, DEFAULT_PROFILE_PICTURE_HEIGHT)
                .asBufferedImage();
    }
}
