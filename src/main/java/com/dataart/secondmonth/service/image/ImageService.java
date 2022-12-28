package com.dataart.secondmonth.service.image;

import com.dataart.secondmonth.dto.ImageDto;
import com.dataart.secondmonth.service.image.util.ImageTransformer;
import com.dataart.secondmonth.validation.imageExtensionValid.ImageExtensionValid;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {

    @SneakyThrows
    ImageDto add(@ImageExtensionValid MultipartFile imageFile, ImageTransformer transformer);

    ImageDto getById(Long id);

    void deleteById(Long id);

    List<ImageDto> getPostImagesByPostId(Long postId);

    ImageDto addAvatar(@ImageExtensionValid MultipartFile imageFile);

    List<ImageDto> addPostImages(List<@ImageExtensionValid MultipartFile> imageFiles);

}
