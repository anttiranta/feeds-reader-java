package com.antti.task.entity;

import java.time.Instant;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Item {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @NotNull
    @Column(length = 256, nullable = false)
    private String title;

    @Lob
    @Column(length = Integer.MAX_VALUE)
    private String description;

    @URL
    @Column(length = 1000)
    private String link;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    @Column(name = "published_date")
    private Date pubDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "category_id")
    private Category category;

    @URL
    @Column(length = 1000)
    private String comments;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", nullable = false)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date")
    private Date updatedDate;

    public Long getId() {
        return this.id;
    }

    public Item setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public Item setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public Item setDescription(String description) {
        this.description = description;
        return this;
    }

    public Date getPubDate() {
        return this.pubDate;
    }

    public Item setPubDate(Date publishedDate) {
        this.pubDate = publishedDate;
        return this;
    }

    public String getLink() {
        return this.link;
    }

    public Item setLink(String link) {
        this.link = link;
        return this;
    }

    public Date getCreatedDate() {
        return this.createdDate;
    }

    public Item setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public Item setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
        return this;
    }

    public String getComments() {
        return comments;
    }

    public Item setComments(String comments) {
        this.comments = comments;
        return this;
    }

    public Category getCategory() {
        return this.category;
    }

    public Item setCategory(Category category) {
        this.category = category;
        return this;
    }

    @PrePersist
    public void prePersist() {
        this.createdDate = new Date(Instant.now().toEpochMilli());
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedDate = new Date(Instant.now().toEpochMilli());
    }
}
