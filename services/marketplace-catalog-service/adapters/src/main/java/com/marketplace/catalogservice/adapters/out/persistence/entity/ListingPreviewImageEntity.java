package com.marketplace.catalogservice.adapters.out.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = @PersistenceCreator)
@Table("listing_preview_image")
public class ListingPreviewImageEntity {

    @Id
    private UUID id;
    @Column("listing_id")
    private UUID listingId;
    private String url;
    private Integer position;
}
