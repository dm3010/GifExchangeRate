package com.example.GifExchangeRate.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.Date;
import java.util.Map;
import java.util.Objects;

@FeignClient(name = "openexchangerates-client", url = "${openexchangerates.url}")
public interface OpenExchangeRatesClient {

    @GetMapping(value = "/latest.json?app_id=${openexchangerates.key}&base=${openexchangerates.base}")
    Map<?, ?> getCurrent();

    @GetMapping(value = "/historical/{date}.json?app_id=${openexchangerates.key}&base=${openexchangerates.base}")
    Map<?, ?> getByDate(@PathVariable("date") String date);

    default double rateWithYesterday(String base) {

        Map<?, ?> bodyCurrent = Objects.requireNonNull(this.getCurrent());

        String yesterday = new Date((Long.parseLong(bodyCurrent.get("timestamp").toString()) - 24 * 60 * 60) * 1000).toString();

        Map<?, ?> bodyYesterday = Objects.requireNonNull(this.getByDate(yesterday));

        return Double.parseDouble(((Map<?, ?>) bodyCurrent.get("rates")).get(base).toString()) /
                Double.parseDouble(((Map<?, ?>) bodyYesterday.get("rates")).get(base).toString());
    }

}
