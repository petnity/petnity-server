package com.example.petnity.data.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class LocateDTO {
    @NotNull
    String Locate_x;
    @NotNull
    String Locate_y;
    @NotNull
    String Keyword_search;
    @NotNull
    String Page;
}
