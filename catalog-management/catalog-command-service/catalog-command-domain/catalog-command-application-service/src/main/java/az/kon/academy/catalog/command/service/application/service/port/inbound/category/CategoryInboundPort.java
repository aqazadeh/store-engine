package az.kon.academy.catalog.command.service.application.service.port.inbound.category;

import az.kon.academy.catalog.command.service.application.service.dto.request.category.*;

public interface CategoryInboundPort {

    void create(ProductCategoryCreateRequest request);

    void activate(ProductCategoryActivateRequest request);

    void archive(ProductCategoryArchiveRequest request);

    void delete(ProductCategoryDeleteRequest request);

    void changeImage(ProductCategoryChangeImageRequest request);

    void changeInformation(ProductCategoryChangeInformationRequest request);

    void changeParent(ProductCategoryChangeParentRequest request);
}
