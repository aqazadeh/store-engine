package az.kon.academy.catalog.command.service.domain.core.aggregate;

import az.kon.academy.aggragate.EventSourcedAggregateRoot;
import az.kon.academy.aggragate.valueobject.Quantity;
import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.catalog.command.service.domain.core.command.productstock.ProductStockCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productstock.ProductStockDecreaseCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productstock.ProductStockIncreaseCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productstock.ProductStockReleaseCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productstock.ProductStockReserveCommand;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductStockDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductStockDomainException;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductStockId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantId;
import az.kon.academy.catalog.event.product.stock.ProductStockCreatedEvent;
import az.kon.academy.catalog.event.product.stock.ProductStockDecreasedEvent;
import az.kon.academy.catalog.event.product.stock.ProductStockEvent;
import az.kon.academy.catalog.event.product.stock.ProductStockIncreasedEvent;
import az.kon.academy.catalog.event.product.stock.ProductStockReleasedEvent;
import az.kon.academy.catalog.event.product.stock.ProductStockReservedEvent;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder(toBuilder = true)
public class ProductStockAggregateRoot extends EventSourcedAggregateRoot<ProductStockAggregateRoot, ProductStockId, ProductStockEvent> {

    @Getter private final ProductVariantId variantId;
    @Getter private Quantity quantity;
    @Getter private Quantity reservedQuantity;

    public static ProductStockAggregateRoot initialize(ProductStockCreateCommand command) {
        var stock = ProductStockAggregateRoot.builder()
                .id(ProductStockId.random())
                .variantId(command.getVariantId())
                .quantity(command.getQuantity())
                .reservedQuantity(Quantity.ZERO)
                .build();

        stock.addEvent(ProductStockCreatedEvent.of(
                stock.getRootID().value().toString(),
                stock.getModificationTs().toOffsetDateTime(),
                stock.getVariantId().value(),
                stock.getQuantity().intValue()
        ));
        return stock;
    }

    public ProductStockAggregateRoot increase(ProductStockIncreaseCommand command) {
        var newQuantity = this.quantity.add(command.getQuantity());

        var stock = this.toBuilder()
                .quantity(newQuantity)
                .modificationTs(SeDateTime.now())
                .build();

        stock.addEvent(ProductStockIncreasedEvent.of(
                stock.getRootID().value().toString(),
                stock.getModificationTs().toOffsetDateTime(),
                command.getQuantity().intValue(),
                stock.getQuantity().intValue()
        ));
        return stock;
    }

    public ProductStockAggregateRoot decrease(ProductStockDecreaseCommand command) {
        if (this.quantity.isLessThan(command.getQuantity())) {
            throw new ProductStockDomainException(
                    ProductStockDomainErrorCodes.INSUFFICIENT_STOCK,
                    List.of(this.getRootID().toString())
            );
        }

        var newQuantity = this.quantity.subtract(command.getQuantity());

        var stock = this.toBuilder()
                .quantity(newQuantity)
                .modificationTs(SeDateTime.now())
                .build();

        stock.addEvent(ProductStockDecreasedEvent.of(
                stock.getRootID().value().toString(),
                stock.getModificationTs().toOffsetDateTime(),
                command.getQuantity().intValue(),
                stock.getQuantity().intValue()
        ));
        return stock;
    }

    public Quantity availableQuantity() {
        return this.quantity.subtract(this.reservedQuantity);
    }

    public ProductStockAggregateRoot reserve(ProductStockReserveCommand command) {
        if (availableQuantity().isLessThan(command.getQuantity())) {
            throw new ProductStockDomainException(
                    ProductStockDomainErrorCodes.INSUFFICIENT_AVAILABLE_STOCK,
                    List.of(this.getRootID().toString())
            );
        }

        var newReserved = this.reservedQuantity.add(command.getQuantity());

        var stock = this.toBuilder()
                .reservedQuantity(newReserved)
                .modificationTs(SeDateTime.now())
                .build();

        stock.addEvent(ProductStockReservedEvent.of(
                stock.getRootID().value().toString(),
                stock.getModificationTs().toOffsetDateTime(),
                command.getQuantity().intValue(),
                stock.getReservedQuantity().intValue()
        ));
        return stock;
    }

    public ProductStockAggregateRoot release(ProductStockReleaseCommand command) {
        if (this.reservedQuantity.isLessThan(command.getQuantity())) {
            throw new ProductStockDomainException(
                    ProductStockDomainErrorCodes.INSUFFICIENT_AVAILABLE_STOCK,
                    List.of(this.getRootID().toString())
            );
        }

        var newReserved = this.reservedQuantity.subtract(command.getQuantity());

        var stock = this.toBuilder()
                .reservedQuantity(newReserved)
                .modificationTs(SeDateTime.now())
                .build();

        stock.addEvent(ProductStockReleasedEvent.of(
                stock.getRootID().value().toString(),
                stock.getModificationTs().toOffsetDateTime(),
                command.getQuantity().intValue(),
                stock.getReservedQuantity().intValue()
        ));
        return stock;
    }

    @Override
    protected ProductStockAggregateRoot applyEvent(ProductStockEvent event) {
        return switch (event) {
            case ProductStockCreatedEvent e -> apply(e);
            case ProductStockIncreasedEvent e -> apply(e);
            case ProductStockDecreasedEvent e -> apply(e);
            case ProductStockReservedEvent e -> apply(e);
            case ProductStockReleasedEvent e -> apply(e);
        };
    }

    private ProductStockAggregateRoot apply(ProductStockCreatedEvent e) {
        return this.toBuilder()
                .variantId(ProductVariantId.from(e.getVariantId()))
                .quantity(Quantity.of(e.getQuantity()))
                .reservedQuantity(Quantity.ZERO)
                .build();
    }

    private ProductStockAggregateRoot apply(ProductStockIncreasedEvent e) {
        return this.toBuilder()
                .quantity(Quantity.of(e.getNewQuantity()))
                .build();
    }

    private ProductStockAggregateRoot apply(ProductStockDecreasedEvent e) {
        return this.toBuilder()
                .quantity(Quantity.of(e.getNewQuantity()))
                .build();
    }

    private ProductStockAggregateRoot apply(ProductStockReservedEvent e) {
        return this.toBuilder()
                .reservedQuantity(Quantity.of(e.getNewReservedQuantity()))
                .build();
    }

    private ProductStockAggregateRoot apply(ProductStockReleasedEvent e) {
        return this.toBuilder()
                .reservedQuantity(Quantity.of(e.getNewReservedQuantity()))
                .build();
    }
}
