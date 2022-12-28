package com.dataart.secondmonth.service;

import com.dataart.secondmonth.dto.LinkDto;
import com.dataart.secondmonth.persistence.entity.Link;
import com.dataart.secondmonth.persistence.repository.LinkRepository;
import com.dataart.secondmonth.utils.CommonURLUtils;
import com.google.common.net.InternetDomainName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class LinkService {

    private final LinkRepository repository;

    private final ModelMapper mapper;

    public LinkDto getRichLinkByPostId(Long postId) {
        Link link = repository.getRichLinkByPostId(postId);
        if (link == null) {
            return null;
        }
        return mapper.map(link, LinkDto.class);
    }

    public LinkDto extractLinkDtoFromUrl(String url) {
        url = CommonURLUtils.addHttpToUrlString(url);

        Document document;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            log.error("Unable to connect.", e);
            return null;
        }
        String title = getMetaTagContent(document, "meta[name=title]");
        String desc = getMetaTagContent(document, "meta[name=description]");
        String ogUrl = StringUtils.defaultIfBlank(getMetaTagContent(document, "meta[property=og:url]"), url);
        String ogTitle = getMetaTagContent(document, "meta[property=og:title]");
        String ogDesc = getMetaTagContent(document, "meta[property=og:description]");
        String ogImage = getMetaTagContent(document, "meta[property=og:image]");
        String ogImageAlt = getMetaTagContent(document, "meta[property=og:image:alt]");
        String domain = ogUrl;

        try {
            domain = InternetDomainName.from(new URL(ogUrl).getHost()).topPrivateDomain().toString();
        } catch (MalformedURLException e) {
            log.warn("Unable to connect to extract domain name from : {}", url);
        }

        return LinkDto.builder()
                .domain(domain)
                .url(url)
                .title(StringUtils.defaultIfBlank(ogTitle, title))
                .desc(StringUtils.defaultIfBlank(ogDesc, desc))
                .image(ogImage)
                .imageAlt(ogImageAlt)
                .build();
    }

    private String getMetaTagContent(Document document, String cssQuery) {
        Element element = document.select(cssQuery).first();

        if (element != null) {
            return element.attr("content");
        }

        return "";
    }

    public void removeAllUnusedLinks() {
        repository.removeAllUnusedLinks();
    }

    public long getCount() {
        return repository.count();
    }

}
