package com.dataart.secondmonth.service.image.util;

import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class PostImageTransformer implements ImageTransformer {

    @SneakyThrows
    @Override
    public BufferedImage transform(MultipartFile image) {
        return ImageIO.read(image.getInputStream());
    }

}
