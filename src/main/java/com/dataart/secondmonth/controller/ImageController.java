package com.dataart.secondmonth.controller;

import com.dataart.secondmonth.dto.ImageDto;
import com.dataart.secondmonth.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("images/avatar")
    public ResponseEntity<ImageDto> uploadAvatar(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(imageService.addAvatar(file));
    }

    @PostMapping("images/post-images")
    public ResponseEntity<List<ImageDto>> uploadPostImages(@RequestParam("files") List<MultipartFile> files) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(imageService.addPostImages(files));
    }

}
