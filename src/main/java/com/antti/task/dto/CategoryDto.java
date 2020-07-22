package com.antti.task.dto;

public class CategoryDto {
    
     private Long id;
     private String name;
     private String domain;

    public Long getId() {
        return id;
    }

    public CategoryDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public CategoryDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getDomain() {
        return domain;
    }

    public CategoryDto setDomain(String domain) {
        this.domain = domain;
        return this;
    }
     
    @Override
    public String toString() {
        return "id: " + getId() + ", " + 
            "name: " + getName() + ", " + 
            "domain: " + getDomain();
    }
}
