package com.antti.task.entity;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity 
@Table(indexes={@Index(name = "title_idx",  columnList="title")})
public class Item {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Column(length = 256, nullable = false)
    @Length(min = 2, max = 256)
    private String title;

    @Lob
    @Column(length = Integer.MAX_VALUE, nullable = true)
    private String description;

    @URL
    @Column(length = 1000, nullable = true)
    private String link;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    @Column(name = "published_date", nullable = false)
    private Date pubDate;
    
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "category_id", nullable=true, referencedColumnName = "ID")
    private Category category;

    @URL
    @Column(length = 1000, nullable = true)
    private String comments;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", nullable = false)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date", nullable = true)
    private Date updatedDate;
    
    public Long getId() {
        return id;
    }

    public Item setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Item setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Item setDescription(String description) {
        this.description = description;
        return this;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public Item setPubDate(Date publishedDate) {
        this.pubDate = publishedDate;
        return this;
    }

    public String getLink() {
        return link;
    }

    public Item setLink(String link) {
        this.link = link;
        return this;
    }

    public Date getCreatedDate() {
        return createdDate;
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
        return category;
    }

    public Item setCategory(Category category) {
        if (isCategorySameAsFormer(category)) {
            return this;
        }
        
        Category oldCategory = this.category;
        this.category = category;
        
        if (oldCategory != null) {
            oldCategory.removeItem(this);
        }
                
        if (category != null) {
            category.addItem(this);
        }
        return this;
    }
    
    private boolean isCategorySameAsFormer(Category newCategory) {
        return category == null? newCategory == null : category.equals(newCategory);
    }
    
    @PrePersist
    public void prePersist() {
        createdDate = new Date(Instant.now().toEpochMilli());
    }

    @PreUpdate
    public void preUpdate() {
        updatedDate = new Date(Instant.now().toEpochMilli());
    }
    
    @PreRemove
    public void preRemove() {
        if (getCategory() != null) {
            if (getCategory().getItems().contains(this)) {
                getCategory().getItems().remove(this);
            }
        }
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
    
    @Override
    public boolean equals(Object object) {
        if (object == this)
            return true;
        if ((object == null) || !(object instanceof Item))
            return false;
 
        final Item item = (Item)object;
 
        if (id != null && item.getId() != null) {
            return id.equals(item.getId());
        } else {
            if (title != null && item.getTitle() != null) {
                return title.equals(item.getTitle());
            }
        }
        
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        
        if (id != null) {
            hash = 67 * hash + Objects.hashCode(id);
        } else {
            hash = 31 * hash + (title == null ? 0 : title.hashCode());
            hash = 31 * hash + (link == null ? 0 : link.hashCode());
        }
        
        return hash;
    }
}
