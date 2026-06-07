package az.kon.academy.catalog.command.service.domain.core.aggregate;

import az.kon.academy.aggragate.EventSourcedAggregateRoot;
import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductStockCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductStockDecreaseCommand;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductStockIncreaseCommand;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductStockDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductStockDomainException;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductStockId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantId;
import az.kon.academy.catalog.event.product.stock.ProductStockCreatedEvent;
import az.kon.academy.catalog.event.product.stock.ProductStockDecreasedEvent;
import az.kon.academy.catalog.event.product.stock.ProductStockEvent;
import az.kon.academy.catalog.event.product.stock.ProductStockIncreasedEvent;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder(toBuilder = true)
public class ProductStockAggregateRoot extends EventSourcedAggregateRoot<ProductStockAggregateRoot, ProductStockId, ProductStockEvent> {

    @Getter private final ProductVariantId variantId;
    @Getter private Integer quantity;

    public static ProductStockAggregateRoot initialize(ProductStockCreateCommand command) {
        var stock = ProductStockAggregateRoot.builder()
                .id(ProductStockId.random())
                .variantId(command.getVariantId())
                .quantity(command.getQuantity())
                .build();

        stock.addEvent(ProductStockCreatedEvent.of(
                stock.getRootID().value().toString(),
                stock.getModificationTs().toOffsetDateTime(),
                stock.getVariantId().value(),
                stock.getQuantity()
        ));
        return stock;
    }

    public ProductStockAggregateRoot increase(ProductStockIncreaseCommand command) {
        var newQuantity = this.quantity + command.getQuantity();

        var stock = this.toBuilder()
                .quantity(newQuantity)
                .modificationTs(SeDateTime.now())
                .build();

        stock.addEvent(ProductStockIncreasedEvent.of(
                stock.getRootID().value().toString(),
                stock.getModificationTs().toOffsetDateTime(),
                command.getQuantity(),
                stock.getQuantity()
        ));
        return stock;
    }

    public ProductStockAggregateRoot decrease(ProductStockDecreaseCommand command) {
        if (this.quantity < command.getQuantity()) {
            throw new ProductStockDomainException(
                    ProductStockDomainErrorCodes.INSUFFICIENT_STOCK,
                    List.of(this.getRootID().toString())
            );
        }

        var newQuantity = this.quantity - command.getQuantity();

        var stock = this.toBuilder()
                .quantity(newQuantity)
                .modificationTs(SeDateTime.now())
                .build();

        stock.addEvent(ProductStockDecreasedEvent.of(
                stock.getRootID().value().toString(),
                stock.getModificationTs().toOffsetDateTime(),
                command.getQuantity(),
                stock.getQuantity()
        ));
        return stock;
    }

    @Override
    protected ProductStockAggregateRoot applyEvent(ProductStockEvent event) {
        return switch (event) {
            case ProductStockCreatedEvent e -> apply(e);
            case ProductStockIncreasedEvent e -> apply(e);
            case ProductStockDecreasedEvent e -> apply(e);
        };
    }

    private ProductStockAggregateRoot apply(ProductStockCreatedEvent e) {
        return null;
    }

    private ProductStockAggregateRoot apply(ProductStockIncreasedEvent e) {
        return null;
    }

    private ProductStockAggregateRoot apply(ProductStockDecreasedEvent e) {
        return null;
    }
}