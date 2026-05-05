package com.yvens.techcatalog.DTO;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


import com.yvens.techcatalog.Entity.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import com.yvens.techcatalog.Entity.Category;

public class ProductDto {

    private Long id;
    @Size(min = 3, max = 60, message = "nome deve ter entre 3 e 60 caracteres")
    @NotBlank(message = "Campo requerido")
    private String name;
     @NotBlank(message = "Campo requerido")
    private String description;
    @Positive(message = "O valor deve ser positivo")
    private Double price;
    private String imgUrl;
    @PastOrPresent(message = "A data do produto não pode ser futura")
    private Instant date;

    private List<CategoryDto> categories = new ArrayList<>();

    public ProductDto() {
    }

    public ProductDto(Long id, String name, String description, Double price, String imgUrl, Instant date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
        this.date = date;
    }

    public ProductDto(Product entity) {
        id = entity.getId();
        name = entity.getName();
        description = entity.getDescription();
        price = entity.getPrice();
        imgUrl = entity.getImgUrl();
        date = entity.getDate();
    }

     public ProductDto(Product entity, Set<Category> categories) {
        this(entity); 
        categories.forEach(cat->this.categories.add(new CategoryDto(cat)));
    
    }

     public Long getId() {
         return id;
     }

     public void setId(Long id) {
         this.id = id;
     }

     public String getName() {
         return name;
     }

     public void setName(String name) {
         this.name = name;
     }

     public String getDescription() {
         return description;
     }

     public void setDescription(String description) {
         this.description = description;
     }

     public Double getPrice() {
         return price;
     }

     public void setPrice(Double price) {
         this.price = price;
     }

     public String getImgUrl() {
         return imgUrl;
     }

     public void setImgUrl(String imgUrl) {
         this.imgUrl = imgUrl;
     }

     public Instant getDate() {
         return date;
     }

     public void setDate(Instant date) {
         this.date = date;
     }

     public List<CategoryDto> getCategories() {
         return categories;
     }

   




}
