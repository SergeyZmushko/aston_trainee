package com.aston.trainee.repository.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class News {
    private Long id;
    private String title;
    private String content;
    private Author author;
    private List<Tag> tags;

    public News(){

    }

    public News(
            Long id,
            String title,
            String content,
            Author author,
            List<Tag> tags
    ) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.tags = tags;
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

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<Tag> getTags() {
        if (tags == null){
            tags = new ArrayList<>();
        }
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        News news = (News) o;
        return Objects.equals(id, news.id) && Objects.equals(title, news.title) && Objects.equals(content, news.content)
                && Objects.equals(author, news.author) && Objects.equals(tags, news.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, author, tags);
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", author=" + author +
                ", tags=" + tags +
                '}';
    }
}





