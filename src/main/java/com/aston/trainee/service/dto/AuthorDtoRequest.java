package com.aston.trainee.service.dto;

public record AuthorDtoRequest(
        String name) {

    @Override
    public String toString() {
        return "AuthorDtoRequest{" +
                "name='" + name + '\'' +
                '}';
    }
}
