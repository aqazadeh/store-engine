package az.kon.academy.catalog.command.service.domain.core.vo.product;

public enum ProductVariantStatus {
    DRAFT,
    ACTIVE,
    INACTIVE,
    OUT_OF_STOCK,
    DISCONTINUED,
    ARCHIVED;

    public Boolean isDraft() {
        return this == DRAFT;
    }

    public Boolean isActive() {
        return this == ACTIVE;
    }

    public Boolean isInactive() {
        return this == INACTIVE;
    }

    public Boolean isOutOfStock() {
        return this == OUT_OF_STOCK;
    }

    public Boolean isDiscontinued() {
        return this == DISCONTINUED;
    }

    public Boolean isArchived() {
        return this == ARCHIVED;
    }
}
