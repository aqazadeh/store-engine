package az.kon.academy.catalog.command.service.application.service.dto.request.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ProductCategoryArchiveRequest {
    private UUID categoryId;
}
