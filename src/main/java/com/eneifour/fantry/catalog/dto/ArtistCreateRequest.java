package com.eneifour.fantry.catalog.dto;

import com.eneifour.fantry.catalog.domain.Artist;
import com.eneifour.fantry.catalog.domain.ArtistStatus;
import com.eneifour.fantry.catalog.domain.GroupType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArtistCreateRequest {
    @NotBlank(message = "한글명은 필수입니다.")
    private String nameKo;
    @NotBlank(message = "영문명은 필수입니다.")
    private String nameEn;
    @NotNull(message = "그룹 구분은 필수입니다.")
    private GroupType groupType;

    public Artist toEntity() {
        return Artist.builder()
                .nameKo(this.nameKo)
                .nameEn(this.nameEn)
                .groupType(this.groupType)
                .status(ArtistStatus.APPROVED)
                .build();
    }
}
