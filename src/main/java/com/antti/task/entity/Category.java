package com.antti.task.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.validator.constraints.URL;

@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"category_domain"})})
public class Category {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(length = 256)
    private String name;
    
    @URL
    @NotNull
    @Column(name = "category_domain", length = 256)
    private String domain;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Item> items;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return items;
    }

    public Category setItems(List<Item> items) {
        this.items = items;
        return this;
    }
}
