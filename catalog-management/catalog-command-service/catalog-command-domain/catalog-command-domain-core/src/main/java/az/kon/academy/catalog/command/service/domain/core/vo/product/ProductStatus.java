package az.kon.academy.catalog.command.service.domain.core.vo.product;

public enum ProductStatus {
    DRAFT,
    SENT_TO_APPROVAL,
    APPROVED,
    REJECTED,
    ARCHIVED;

    public Boolean isDraft() {
        return this == DRAFT;
    }

    public Boolean isSentToApproval() {
        return this == SENT_TO_APPROVAL;
    }

    public Boolean isApproved() {
        return this == APPROVED;
    }

    public Boolean isRejected() {
        return this == REJECTED;
    }

    public Boolean isArchived() {
        return this == ARCHIVED;
    }
}
