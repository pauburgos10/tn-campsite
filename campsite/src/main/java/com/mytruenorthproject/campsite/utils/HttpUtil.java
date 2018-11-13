package com.mytruenorthproject.campsite.utils;

import org.springframework.web.client.RestTemplate;

public class HttpUtil {

    public static String performGet(String url) {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        return response;
    }
}
