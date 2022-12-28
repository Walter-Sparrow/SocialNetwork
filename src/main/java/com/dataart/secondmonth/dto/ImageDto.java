package com.dataart.secondmonth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.Base64Utils;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageDto {

    private Long id;

    private byte[] data;

    public String getBase64() {
        return Base64Utils.encodeToString(data);
    }

    private long size;

    private int width;

    private int height;

    private String type;

}
