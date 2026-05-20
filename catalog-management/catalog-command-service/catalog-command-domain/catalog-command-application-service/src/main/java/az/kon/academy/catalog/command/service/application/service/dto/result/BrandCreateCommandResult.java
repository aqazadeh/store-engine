package az.kon.academy.catalog.command.service.application.service.dto.result;

import az.kon.academy.catalog.command.service.domain.core.aggregate.BrandRoot;
import lombok.Getter;

@Getter
public class BrandCreateCommandResult {

    private BrandCreateCommandResult(BrandRoot brandRoot) {
    }

    public static BrandCreateCommandResult of(BrandRoot brandRoot) {
        return new BrandCreateCommandResult(brandRoot);
    }
}
