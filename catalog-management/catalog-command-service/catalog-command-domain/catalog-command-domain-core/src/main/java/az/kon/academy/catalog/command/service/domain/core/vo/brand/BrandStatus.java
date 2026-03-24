package az.kon.academy.catalog.command.service.domain.core.vo.brand;

public enum BrandStatus {
    DRAFT,
    SENT_TO_APPROVAL,
    APPROVED,
    REJECTED;

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
}
