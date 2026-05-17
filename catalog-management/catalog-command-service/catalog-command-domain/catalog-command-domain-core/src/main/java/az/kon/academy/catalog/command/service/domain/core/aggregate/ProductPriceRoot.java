package az.kon.academy.catalog.command.service.domain.core.aggregate;

import az.kon.academy.aggragate.AggregateRoot;
import az.kon.academy.aggragate.valueobject.Money;
import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductPriceCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductPriceUpdateCommand;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductPriceId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantId;
import az.kon.academy.catalog.event.product.price.ProductPriceCreatedEvent;
import az.kon.academy.catalog.event.product.price.ProductPriceUpdatedEvent;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class ProductPriceRoot extends AggregateRoot<ProductPriceRoot, ProductPriceId> {

    @Getter private final ProductVariantId variantId;
    @Getter private Money minPrice;
    @Getter private Money maxPrice;

    public static ProductPriceRoot initialize(ProductPriceCreateCommand command) {
        var price = ProductPriceRoot.builder()
                .id(ProductPriceId.random())
                .variantId(command.getVariantId())
                .minPrice(command.getMinPrice())
                .maxPrice(command.getMaxPrice())
                .build();

        price.addEvent(ProductPriceCreatedEvent.of(
                price.getRootID().value().toString(),
                price.getModificationTs().toOffsetDateTime(),
                price.getVariantId().value(),
                price.getMinPrice().toString(),
                price.getMaxPrice().toString()
        ));
        return price;
    }

    public ProductPriceRoot update(ProductPriceUpdateCommand command) {
        var price = this.toBuilder()
                .minPrice(command.getMinPrice())
                .maxPrice(command.getMaxPrice())
                .modificationTs(SeDateTime.now())
                .build();

        price.addEvent(ProductPriceUpdatedEvent.of(
                price.getRootID().value().toString(),
                price.getModificationTs().toOffsetDateTime(),
                price.getMinPrice().toString(),
                price.getMaxPrice().toString()
        ));
        return price;
    }
}