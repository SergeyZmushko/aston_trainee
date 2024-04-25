package com.aston.trainee.service.dto;

import java.util.ArrayList;
import java.util.List;

public record NewsDtoRequest(
        String title,
        String content,
        String author,
        List<String> tags
) {

    public NewsDtoRequest {
        if (tags == null) {
            tags = new ArrayList<>();
        }
    }

    @Override
    public String toString() {
        return "NewsDtoRequest{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", author='" + author + '\'' +
                ", tags=" + tags +
                '}';
    }
}
