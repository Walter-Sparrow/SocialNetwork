package com.dataart.secondmonth.controller;

import com.dataart.secondmonth.dto.LinkDto;
import com.dataart.secondmonth.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class OpenGraphController {

    private final LinkService linkService;

    @GetMapping("/share")
    public ResponseEntity<LinkDto> getOpenMetaData(@RequestParam(value = "url") String url) {
        return ResponseEntity.ok(linkService.extractLinkDtoFromUrl(url));
    }

}
