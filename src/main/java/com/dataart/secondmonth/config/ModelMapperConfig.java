package com.dataart.secondmonth.config;

import com.dataart.secondmonth.dto.GroupPostDto;
import com.dataart.secondmonth.persistence.projection.GroupPostProjection;
import lombok.RequiredArgsConstructor;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;

@Configuration
@RequiredArgsConstructor
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.addConverter(new AbstractConverter<GroupPostProjection, GroupPostDto>() {
            @Override
            protected GroupPostDto convert(GroupPostProjection postProjection) {
                return GroupPostDto.builder()
                        .id(postProjection.getId())
                        .text(postProjection.getText())
                        .likesCount(postProjection.getLikesCount())
                        .dislikesCount(postProjection.getDislikesCount())
                        .likedByAuthorized(postProjection.getLikedByAuthorized())
                        .createdAt(postProjection.getCreatedAt().atZone(ZoneId.systemDefault()))
                        .build();
            }
        });
        return modelMapper;
    }

}
