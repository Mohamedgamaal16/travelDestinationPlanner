package com.travelDestinationPlanner.fawry.client;

import com.travelDestinationPlanner.fawry.client.dto.RestCountryV3Dto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "restCountries",
        url = "${app.restcountries.base-url:https://restcountries.com}")
public interface RestCountriesClient {

    @GetMapping("/v3.1/all")
    List<RestCountryV3Dto> getAllCountries(
            @RequestParam("fields") String fields);
}
