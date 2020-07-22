package com.antti.task.dto;

import java.util.Date;

public class ItemDto {
    
    private Long id;
    private String title;
    private String description;
    private String link;
    private Date pubDate;
    private CategoryDto category;
    private String comments;
    private Date createdDate;
    private Date updatedDate;

    public Long getId() {
        return id;
    }

    public ItemDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ItemDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ItemDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getLink() {
        return link;
    }

    public ItemDto setLink(String link) {
        this.link = link;
        return this;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public ItemDto setPubDate(Date pubDate) {
        this.pubDate = pubDate;
        return this;
    }

    public CategoryDto getCategory() {
        return category;
    }

    public ItemDto setCategory(CategoryDto category) {
        this.category = category;
        return this;
    }

    public String getComments() {
        return comments;
    }

    public ItemDto setComments(String comments) {
        this.comments = comments;
        return this;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public ItemDto setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public ItemDto setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
        return this;
    }
    
    @Override
    public String toString() {
        return "id: " + getId() + ", " + 
            "title: " + getTitle() + ", " + 
            "description: " + getDescription() + ", " + 
            "link: " + getLink() + ", " + 
            "category: " + (getCategory() != null ? getCategory().toString() : "") 
                + ", " +
            "comments: " + getComments() + ", " + 
            "pubDate: " + (getPubDate() != null ? getPubDate().toString() : "");
    }
}
