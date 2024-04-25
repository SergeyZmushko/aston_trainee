package com.aston.trainee.service.dto;

import java.util.List;

public record UpdateNewsDtoRequest (
        String title,
        String content,
        String author,
        List<String> tags
){
}
