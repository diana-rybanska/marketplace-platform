package com.marketplace.catalogservice.adapters.out.persistence.entity;

import com.marketplace.catalogservice.domain.enums.ListingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("listing")
public class ListingEntity {

    @Id
    private UUID id;
    private String title;
    private String description;
    private BigDecimal price;
    private String currency;
    private String category;
    private ListingStatus status;
    private List<String> tags;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}

