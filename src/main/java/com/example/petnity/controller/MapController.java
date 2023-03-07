package com.example.petnity.controller;

import com.example.petnity.service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/kakaoMap")
public class MapController {

    private final MapService mapService;

    public MapController(MapService mapService){
        this.mapService = mapService;
    }

    @GetMapping(value = "/search",produces="application/json")
    public String Search(@RequestParam String Locate_x,
                         @RequestParam String Locate_y,
                         @RequestParam String Keyword_Search,
                         @RequestParam String Page) {

        return mapService.Give_list(Locate_x, Locate_y, Keyword_Search, Page);
    }
}
