package az.kon.academy.catalog.command.service.domain.core.vo;

import java.util.Objects;

public final class Barcode {

    private final String value;

    private Barcode(String value) {
        this.value = value;
    }

    public static Barcode of(String value) {
        return new Barcode(value);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Barcode that = (Barcode) o;
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