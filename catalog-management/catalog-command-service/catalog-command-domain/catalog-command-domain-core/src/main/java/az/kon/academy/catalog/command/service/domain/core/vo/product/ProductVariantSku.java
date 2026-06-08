package az.kon.academy.catalog.command.service.domain.core.vo.product;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public final class ProductVariantSku {

    private final String value;

    private ProductVariantSku(String value) {
        this.value = value;
    }

    public static ProductVariantSku of(String value) {
        return new ProductVariantSku(value);
    }

    public static ProductVariantSku random() {
        var ts = Long.toString(System.currentTimeMillis(), 36).toUpperCase();
        var rand = Integer.toString(ThreadLocalRandom.current().nextInt(36 * 36 * 36 * 36), 36).toUpperCase();
        return new ProductVariantSku("SKU-" + ts + "-" + rand);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductVariantSku that = (ProductVariantSku) o;
        return Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }

    @Override
    public String toString() {
        return value;
    }
}
