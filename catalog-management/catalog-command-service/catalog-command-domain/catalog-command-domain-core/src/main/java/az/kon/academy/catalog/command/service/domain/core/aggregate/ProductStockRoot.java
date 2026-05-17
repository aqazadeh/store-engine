package az.kon.academy.catalog.command.service.domain.core.aggregate;

import az.kon.academy.aggragate.AggregateRoot;
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
import az.kon.academy.catalog.event.product.stock.ProductStockIncreasedEvent;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder(toBuilder = true)
public class ProductStockRoot extends AggregateRoot<ProductStockRoot, ProductStockId> {

    @Getter private final ProductVariantId variantId;
    @Getter private Integer quantity;

    public static ProductStockRoot initialize(ProductStockCreateCommand command) {
        var stock = ProductStockRoot.builder()
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

    public ProductStockRoot increase(ProductStockIncreaseCommand command) {
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

    public ProductStockRoot decrease(ProductStockDecreaseCommand command) {
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
}