package com.example.GifExchangeRate.controller;

import com.example.GifExchangeRate.client.GiphyClient;
import com.example.GifExchangeRate.client.OpenExchangeRatesClient;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OpenExchangeRatesClient openExchangeRatesClient;

    @MockBean
    private GiphyClient giphyClient;

    @Value("${giphy.tag.up}")
    private String tagUp;

    @Value("${giphy.tag.down}")
    private String tagDown;


    @Test
    void whenTagUpRequest_thenStatus200() throws Exception {
        String cur = "RUB";
        Mockito.when(openExchangeRatesClient.rateWithYesterday(cur))
                .thenReturn(1.5); // > 1 Up
        Mockito.when(giphyClient.getRandom(tagUp))
                .thenReturn(new byte[5]);
        mockMvc.perform(get("/get/" + cur))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_GIF))
        ;
    }

    @Test
    void whenTagDownRequest_thenStatus200() throws Exception {
        String cur = "RUB";
        Mockito.when(openExchangeRatesClient.rateWithYesterday(cur))
                .thenReturn(0.5); // < 1 Up
        Mockito.when(giphyClient.getRandom(tagDown))
                .thenReturn(new byte[5]);
        mockMvc.perform(get("/get/" + cur))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_GIF))
        ;
    }

    @Test
    void whenWrongRequestThenStatus400() throws Exception {
        String cur = "WRONG";
        Mockito.when(openExchangeRatesClient.rateWithYesterday(cur))
                .thenThrow(new RuntimeException("error"));
        mockMvc.perform(get("/get/" + cur))
                .andExpect(status().isBadRequest())
                .andExpect(content().bytes(new byte[0]))
        ;
    }

    // Тест на "проброс" http-статуса ответа от сервисов
    @Test
    void whenFeignClientErrorThenStatusFromService() throws Exception {
        String cur = "RUB";
        Map<String, Collection<String>> responseHeaders = new HashMap<>();
        responseHeaders.put("error", Collections.singleton("wrong query"));

        Mockito.when(openExchangeRatesClient.rateWithYesterday(cur))
                .thenThrow(new FeignException.NotFound(
                        "",
                        Request.create(
                                Request.HttpMethod.GET,
                                "/get/" + cur,
                                new HashMap<>(),
                                (byte[]) null,
                                null
                        ),
                        null,
                        responseHeaders
                ));
        mockMvc.perform(get("/get/" + cur))
                .andExpect(status().isNotFound())
                .andExpect(content().bytes(new byte[0]))
        ;
    }

}