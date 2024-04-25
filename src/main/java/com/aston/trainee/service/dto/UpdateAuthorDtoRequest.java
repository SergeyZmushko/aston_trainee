package com.aston.trainee.service.dto;

public record UpdateAuthorDtoRequest(
        Long id,
        String name
) {
    @Override
    public String toString() {
        return "UpdateAuthorDtoRequest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
