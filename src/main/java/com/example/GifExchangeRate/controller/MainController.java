package com.example.GifExchangeRate.controller;

import com.example.GifExchangeRate.client.GiphyClient;
import com.example.GifExchangeRate.client.OpenExchangeRatesClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    private final GiphyClient giphyClient;
    private final OpenExchangeRatesClient openExchangeRatesClient;
    @Value("${giphy.tag.up}")
    String tagUp;
    @Value("${giphy.tag.down}")
    String tagDown;


    public MainController(OpenExchangeRatesClient openExchangeRatesClient, GiphyClient giphyClient) {
        this.openExchangeRatesClient = openExchangeRatesClient;
        this.giphyClient = giphyClient;
    }

    @GetMapping(value = "/get/{currency}", produces = MediaType.IMAGE_GIF_VALUE)
    public @ResponseBody byte[] getCurrency(@PathVariable String currency) {
        double rate = openExchangeRatesClient.rateWithYesterday(currency.toUpperCase());
        return giphyClient.getRandom(rate > 1 ? tagUp : tagDown);
    }
}
