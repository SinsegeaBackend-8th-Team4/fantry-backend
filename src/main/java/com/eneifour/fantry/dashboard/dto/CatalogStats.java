package com.eneifour.fantry.dashboard.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CatalogStats {
    private final long totalArtists;
    private final long pendingArtists;
    private final long approvedArtists;
    private final long rejectedArtists;
    private final long totalAlbums;
    private final long totalGoodsCategories;
}
