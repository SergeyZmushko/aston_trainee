package com.aston.trainee.service.dto;

import java.util.List;

public class NewsDtoResponse {

    private Long id;
    private String title;
    private String content;
    private AuthorDtoResponse authorDto;
    private List<TagDtoResponse> tagsDto;

    public NewsDtoResponse(Long id, String title, String content, AuthorDtoResponse authorDto, List<TagDtoResponse> tagsDto) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorDto = authorDto;
        this.tagsDto = tagsDto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public AuthorDtoResponse getAuthorDto() {
        return authorDto;
    }

    public void setAuthorDto(AuthorDtoResponse authorDto) {
        this.authorDto = authorDto;
    }

    public List<TagDtoResponse> getTagsDto() {
        return tagsDto;
    }

    public void setTagsDto(List<TagDtoResponse> tagsDto) {
        this.tagsDto = tagsDto;
    }
}
