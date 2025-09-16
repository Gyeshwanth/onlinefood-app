package com.yeshwanth.ps.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    private String id;
    private String name;
    private String description;
    private String category;
    private BigDecimal price;
    private String imageUrl;
}
