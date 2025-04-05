package com.elboutique.backend.mapper;

import java.util.List;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Value;

import com.elboutique.backend.DTO.request.ProductRequest;
import com.elboutique.backend.DTO.response.ProductResponse;
import com.elboutique.backend.model.Product;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {

    @Value("${app.base-url}")
    protected String baseUrl;

    @Mapping(target = "image", source = "image", qualifiedByName = "withBaseUrl")
    public abstract ProductResponse toDto(Product product);

    @Mapping(target = "image", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    public abstract Product toEntity(ProductRequest product);

    @Mapping(target = "image", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateProduct(ProductRequest productRequest, @MappingTarget Product product);

    public abstract List<ProductResponse> toDtoList(List<Product> products);

    @Named("withBaseUrl")
    protected String getImageWithBaseUrl(String image) {
        return image != null ? baseUrl + image : null;
    }
}

