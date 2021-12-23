package com.example.GifExchangeRate.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;
import java.util.Map;

@FeignClient(name = "GifClient", url = "${giphy.url}")
public interface GiphyClient {

    @GetMapping("?api_key=${giphy.key}")
    Map<?, ?> search(@RequestParam("tag") String tag);

    @GetMapping
    byte[] get(URI uri);

    default byte[] getRandom(String tag) {

        Map<?, ?> body = search(tag);

        Map<?, ?> data = (Map<?, ?>) body.get("data");
        Map<?, ?> images = (Map<?, ?>) data.get("images");
        Map<?, ?> original = (Map<?, ?>) images.get("original");

        String url = original.get("url").toString();

        return get(URI.create(url));
    }
}
