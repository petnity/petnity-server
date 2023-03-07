package com.example.petnity.service.impl;

import com.example.petnity.data.dto.LocateDTO;
import com.example.petnity.kakaoAPI.KakaoMapAPI;
import com.example.petnity.service.MapService;
import org.springframework.stereotype.Service;

@Service
public class MapServiceImpl implements MapService {
    @Override
    public String Give_list(String locate_x, String locate_y, String query, String Page) {
        LocateDTO locateDTO = LocateDTO.builder()
                .Locate_x(locate_x)
                .Locate_y(locate_y)
                .Keyword_search(query)
                .Page(Page)
                .build();

        KakaoMapAPI search_list = new KakaoMapAPI(locateDTO);

        return search_list.get_list();
    }
}
