package com.dataart.secondmonth.service.image;

import com.dataart.secondmonth.dto.ImageDto;
import com.dataart.secondmonth.persistence.entity.Image;
import com.dataart.secondmonth.persistence.repository.ImageRepository;
import com.dataart.secondmonth.service.image.util.ImageTransformer;
import com.dataart.secondmonth.service.image.util.PostImageTransformer;
import com.dataart.secondmonth.service.image.util.ProfileImageTransformer;
import com.dataart.secondmonth.validation.imageExtensionValid.ImageExtensionValid;
import liquibase.util.file.FilenameUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class ImageDBService implements ImageService {

    private final ImageRepository repository;
    private final ModelMapper mapper;

    @Override
    public ImageDto add(MultipartFile imageFile, ImageTransformer transformer) {
        BufferedImage transformedImage = transformer.transform(imageFile);

        Image image = Image.builder()
                .data(getBytesFromBufferedImage(transformedImage, FilenameUtils.getExtension(imageFile.getOriginalFilename())))
                .type(imageFile.getContentType())
                .size(imageFile.getSize())
                .height(transformedImage.getHeight())
                .width(transformedImage.getWidth())
                .build();

        return mapper.map(repository.save(image), ImageDto.class);
    }

    @SneakyThrows
    private byte[] getBytesFromBufferedImage(BufferedImage image, String imageType) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, imageType, baos);
            baos.flush();
            return baos.toByteArray();
        }
    }

    @Override
    public ImageDto getById(Long id) {
        return mapper.map(repository.getById(id), ImageDto.class);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Cacheable("images")
    public List<ImageDto> getPostImagesByPostId(Long postId) {
        return repository.getPostImagesByPostId(postId).stream()
                .map(image -> mapper.map(image, ImageDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ImageDto addAvatar(MultipartFile imageFile) {
        return add(imageFile, new ProfileImageTransformer());
    }

    @Override
    public List<ImageDto> addPostImages(List<MultipartFile> imageFiles) {
        return imageFiles.stream()
                .map(imageFile -> add(imageFile, new PostImageTransformer()))
                .toList();
    }

}
