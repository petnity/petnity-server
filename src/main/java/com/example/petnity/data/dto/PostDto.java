package com.example.petnity.data.dto;

import lombok.*;

import java.time.LocalDateTime;

public class PostDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Builder
    public static class SaveDto{
        private String userEmail;
        private String postTitle;
        private String postBody;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Builder
    public static class GetPostDto{
        private Long postId;
        private String postTitle;
        private String postBody;
        private int commentCount;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;

    }

}