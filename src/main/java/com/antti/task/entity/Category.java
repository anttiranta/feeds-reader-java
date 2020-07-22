package com.antti.task.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"category_domain"})}, 
        indexes={@Index(name="name_idx", columnList="name", unique=false)})
public class Category {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 256)
    @Length(min = 2, max = 256)
    private String name;
    
    @URL
    @Column(name = "category_domain", length = 256, nullable=false)
    @Length(min = 2, max = 256)
    private String domain;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Item> items = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public Category setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Category setName(String name) {
        this.name = name;
        return this;
    }

    public String getDomain() {
        return domain;
    }

    public Category setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    public List<Item> getItems() {
        return new ArrayList<>(items);
    }

    public Category setItems(List<Item> items) {
        this.items = items;
        return this;
    }
    
    public void addItem(Item item) {
        if (item != null) {
            if (items.contains(item)) {
                return;
            } else {
                items.add(item);
            }
            item.setCategory(this);
        }
    }
    
    public void removeItem(Item item) {
        if (!items.contains(item)) {
            return;
        }
        
        items.remove(item);
        item.setCategory(null);
    }
    
    @PreRemove
    public void preRemove() {
        if (!items.isEmpty()) {
            for(Item item : items) {
                item.setCategory(null);
                items.remove(item);
            }
        }
    }
    
    @Override
    public String toString() {
        return "id: " + getId() + ", " + 
            "name: " + getName() + ", " + 
            "domain: " + getDomain();
    }
    
    @Override
    public boolean equals(Object object) {
        if (object == this)
            return true;
        if ((object == null) || !(object instanceof Category))
            return false;
 
        final Category category = (Category)object;
 
        if (id != null && category.getId() != null) {
            return id.equals(category.getId());
        } else {
            if (domain != null && category.getDomain() != null) {
                return domain.equals(category.getDomain());
            }
        }
        
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        
        if (id != null) {
            hash = 83 * hash + Objects.hashCode(id);
        } else {
            hash = 31 * hash + (domain == null ? 0 : domain.hashCode());
        }
        
        return hash;
    }
}
