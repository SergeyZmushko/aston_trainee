package com.aston.trainee.service.impl;

import com.aston.trainee.repository.impl.TagRepository;
import com.aston.trainee.repository.model.Tag;
import com.aston.trainee.service.dto.*;
import com.aston.trainee.service.interfaces.TagModelMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock
    TagRepository tagRepository;
    @Spy
    TagModelMapper tagModelMapper;

    @InjectMocks
    TagService tagService;

    @Test
    void readAll() {
        Tag tag1 = new Tag(1L, "FUN");
        Tag tag2 = new Tag(2L, "CRIME");
        Tag tag3 = new Tag(3L, "HEALTH");

        when(tagRepository.readAll()).thenReturn(List.of(tag1, tag2, tag3));

        List<TagDtoResponse> result = tagService.readAll();

        assertEquals(3, result.size());
        assertEquals(tag1.getName(), result.get(0).name());
        assertEquals(tag2.getName(), result.get(1).name());
        assertEquals(tag3.getName(), result.get(2).name());
    }

    @Test
    void readById() {
        String tagName = "MONEY";
        Long tagId = 2L;
        when(tagRepository.readById(2L)).thenReturn(Optional.of(new Tag(tagId, tagName)));

        TagDtoResponse resultTag = tagService.readById(2L);
        assertEquals(tagName, resultTag.name());
        assertEquals(tagId, resultTag.id());
    }

    @Test
    void create() {
        TagDtoRequest tagRequest = new TagDtoRequest("FUN");

        Tag returnTag = new Tag(4L, tagRequest.name());
        Tag fromDtoTag = new Tag(null, tagRequest.name());

        when(tagRepository.create(fromDtoTag)).thenReturn(returnTag);

        TagDtoResponse resultTag = tagService.create(tagRequest);

        assertEquals(tagRequest.name(), resultTag.name());
        assertEquals(4L, resultTag.id());
    }

    @Test
    void update() {
        UpdateTagDtoRequest tag = new UpdateTagDtoRequest(4L, "MONEY");
        Tag returnTag = new Tag(4L, "MONEY");
        when(tagRepository.update(returnTag)).thenReturn(returnTag);

        TagDtoResponse resultTag = tagService.update(4L, tag);

        assertEquals(tag.name(), resultTag.name());
        assertEquals(4L, resultTag.id());
    }

    @Test
    void deleteById() {
        when(tagRepository.isExistedById(5L)).thenReturn(true);
        when(tagRepository.deleteById(5L)).thenReturn(true);

        assertTrue(tagService.deleteById(5L));
    }
}