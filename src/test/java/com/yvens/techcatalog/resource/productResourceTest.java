package com.yvens.techcatalog.resource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yvens.techcatalog.DTO.ProductDto;
import com.yvens.techcatalog.Repository.Factory;
import com.yvens.techcatalog.Resource.ProductResource;
import com.yvens.techcatalog.Service.ProductService;
import com.yvens.techcatalog.Service.Exception.DataBaseException;
import com.yvens.techcatalog.Service.Exception.ResourceNotFoundException;

@WebMvcTest(ProductResource.class)
public class productResourceTest { 

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService service;

    @Autowired
    private ObjectMapper objectMapper;

    private PageImpl<ProductDto> page;
    private ProductDto productDto;
    private long existingId;
    private long nonExistingId;
    private long dependentId;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;
        productDto = Factory.createProductDto();
        page = new PageImpl<>(List.of(productDto));

        when(service.findAllPaged(any())).thenReturn(page);

        when(service.findById(existingId)).thenReturn(productDto);
        when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        when(service.update(any(), eq(existingId))).thenReturn(productDto);
        when(service.update(any(), eq(nonExistingId))).thenThrow(ResourceNotFoundException.class);

        doNothing().when(service).delete(existingId);
        doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
        doThrow(DataBaseException.class).when(service).delete(dependentId);

    }

    @Test
    public void deleteShouldDoNothingwhenIdExist() throws Exception {

        ResultActions result = mockMvc.perform(delete("/products/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNoContent());

    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesnotExist() throws Exception {
        ResultActions result = mockMvc.perform(delete("/products/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());

    }

    @Test
    public void deleteShouldThrowDataBaseExceptionWhenIdisDepedent() throws Exception {
        ResultActions result = mockMvc.perform(delete("/products/{id}", dependentId)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isBadRequest());

    }

    @Test
    public void updateShouldReturnProductDtoWhenIdExists() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(productDto);

        ResultActions result = mockMvc.perform(put("/products/{id}", existingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());

    }

    @Test
    public void updateShouldThrowNotFoundExceptionWhenIdDoesntExist() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(productDto);

        ResultActions result = mockMvc.perform(put("/products/{id}", nonExistingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());

    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        // Act
        ResultActions result = mockMvc.perform(get("/products")
                .accept(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isOk());

    }

    @Test
    public void findByIdShouldReturnProductDtoWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(get("/products/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());

    }

    @Test
    public void findByIdShouldThrowEntityNotFoundExceptionWhenIdDoesntExist() throws Exception {

        ResultActions result = mockMvc.perform(get("/products/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());

    }
}