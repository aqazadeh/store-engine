package az.kon.academy.aggragate.valueobject;

import java.math.BigInteger;
import java.util.Objects;

public final class Quantity {

    public static final Quantity ZERO = new Quantity(BigInteger.ZERO);

    public static final Quantity ONE = new Quantity(BigInteger.ONE);

    public static final Quantity TEN = new Quantity(BigInteger.TEN);

    private final BigInteger value;

    private Quantity(BigInteger value) {
        this.value = value;
    }

    public Quantity add(BigInteger value) {
        return new Quantity(this.value.add(value));
    }

    public Quantity add(Quantity quantity) {
        return new Quantity(this.value.add(quantity.value));
    }

    public Quantity subtract(BigInteger value) {
        return new Quantity(this.value.subtract(value));
    }

    public Quantity subtract(Quantity quantity) {
        return new Quantity(this.value.subtract(quantity.value));
    }

    public Quantity multiply(BigInteger value) {
        return new Quantity(this.value.multiply(value));
    }

    public Quantity divide(BigInteger value) {
        return new Quantity(this.value.divide(value));
    }

    public Quantity divide(int value) {
        return new Quantity(this.value.divide(BigInteger.valueOf(value)));
    }

    public Quantity divide(Quantity quantity) {
        return new Quantity(this.value.divide(quantity.value));
    }

    public Quantity multiply(int value) {
        return new Quantity(this.value.multiply(BigInteger.valueOf(value)));
    }

    public Quantity multiply(Quantity quantity) {
        return new Quantity(this.value.multiply(quantity.value));
    }

    public Boolean isGreaterThan(Quantity quantity) {
        return value.compareTo(quantity.value) > 0;
    }

    public Boolean isZero() {
        return this.isEqualTo(ZERO);
    }

    public Boolean isLessThan(Quantity quantity) {
        return value.compareTo(quantity.value) < 0;
    }

    public Boolean isGreaterThanOrEqualTo(Quantity quantity) {
        return value.compareTo(quantity.value) >= 0;
    }

    public Boolean isLessThanOrEqualTo(Quantity quantity) {
        return value.compareTo(quantity.value) <= 0;
    }

    public Boolean isLessOrEqualThanZero() {
        return this.isLessThan(Quantity.ZERO) || this.isEqualTo(Quantity.ZERO);
    }

    public Boolean isEqualTo(Quantity quantity) {
        return value.compareTo(quantity.value) == 0;
    }

    public Integer intValue() {
        return value.intValue();
    }

    public BigInteger value() {
        return value;
    }

    public static Quantity of(BigInteger value) {
        return new Quantity(value);
    }

    public static Quantity of(String value) {
        return new Quantity(new BigInteger(value));
    }

    public static Quantity of(long value) {
        return new Quantity(BigInteger.valueOf(value));
    }

    public static Quantity of(int value) {
        return new Quantity(BigInteger.valueOf(value));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quantity that = (Quantity) o;
        return Objects.equals(value, that.value());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
