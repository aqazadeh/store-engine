package az.kon.academy.catalog.command.service.domain.core.aggregate;

import az.kon.academy.aggragate.EventSourcedAggregateRoot;
import az.kon.academy.aggragate.valueobject.Money;
import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.catalog.command.service.domain.core.command.productprice.ProductPriceCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productprice.ProductPriceChangedCommand;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductPriceId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantId;
import az.kon.academy.catalog.event.product.price.ProductPriceCreatedEvent;
import az.kon.academy.catalog.event.product.price.ProductPriceEvent;
import az.kon.academy.catalog.event.product.price.ProductPriceUpdatedEvent;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder(toBuilder = true)
public class ProductPriceAggregateRoot extends EventSourcedAggregateRoot<ProductPriceAggregateRoot, ProductPriceId, ProductPriceEvent> {
    public static final Integer SNAPSHOT_THRESHOLD = 20;

    @Getter private final ProductVariantId variantId;
    @Getter private final Money minPrice;
    @Getter private final Money maxPrice;
    @Getter private final Money defaultPrice;
    @Getter private final Money actualPrice;
    @Getter private final Boolean autoPriceChangeEnabled;

    public static ProductPriceAggregateRoot from(ProductPriceId id, List<ProductPriceEvent> events) {
        var price = ProductPriceAggregateRoot.builder()
                .id(id)
                .build();

        return price.replay(events);
    }

    public static ProductPriceAggregateRoot initialize(ProductPriceCreateCommand command) {

        var productPrice = ProductPriceAggregateRoot.builder()
                .id(ProductPriceId.random())
                .variantId(command.getVariantId())
                .minPrice(command.getMinPrice())
                .maxPrice(command.getMaxPrice())
                .defaultPrice(command.getDefaultPrice())
                .actualPrice(command.getDefaultPrice())
                .autoPriceChangeEnabled(command.getAutoPriceUpdateEnabled())
                .build();

        var event = ProductPriceCreatedEvent.of(
                productPrice.getRootID().value().toString(),
                SeDateTime.now().toOffsetDateTime(),
                productPrice.getVariantId().value(),
                productPrice.getMinPrice().value(),
                productPrice.getMaxPrice().value(),
                productPrice.getDefaultPrice().value(),
                productPrice.getActualPrice().value(),
                productPrice.getAutoPriceChangeEnabled()
        );

        productPrice.addEvent(event);
        return productPrice;
    }

    @Override
    protected ProductPriceAggregateRoot applyEvent(ProductPriceEvent event) {
        return switch (event) {
            case ProductPriceCreatedEvent e -> apply(e);
            case ProductPriceUpdatedEvent e -> apply(e);
        };
    }

    public ProductPriceAggregateRoot changePrice(ProductPriceChangedCommand command) {

        var productPrice = this.toBuilder()

                .build();

        var event = ProductPriceUpdatedEvent.of(
                this.getRootID().value().toString(),
                SeDateTime.now().toOffsetDateTime(),
                command.getMinPrice().value(),
                command.getMaxPrice().value()
        );
        return this.apply(event);
    }

    private ProductPriceAggregateRoot apply(ProductPriceUpdatedEvent event) {
        var price = this.toBuilder()
                .minPrice(Money.of(event.getMinPrice()))
                .maxPrice(Money.of(event.getMaxPrice()))
                .build();

        price.addEvent(event);
        return price;
    }

    private ProductPriceAggregateRoot apply(ProductPriceCreatedEvent event) {
        var price = this.toBuilder()
                .variantId(ProductVariantId.from(event.getVariantId()))
                .minPrice(Money.of(event.getMinPrice()))
                .maxPrice(Money.of(event.getMaxPrice()))
                .defaultPrice(Money.of(event.getDefaultPrice()))
                .actualPrice(Money.of(event.getActualPrice()))
                .autoPriceChangeEnabled(event.getAutoPriceChangeEnabled())
                .build();
        price.addEvent(event);
        return price;
    }
}
