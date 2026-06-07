package az.kon.academy.aggragate.valueobject;

import java.math.BigInteger;
import java.util.Objects;

/**
 * An immutable value object representing a quantity.
 * <p>
 * Wraps a {@link BigInteger} and provides arithmetic operations
 * (add, subtract, multiply, divide) and comparison methods.
 * All operations return new instances. Convenience constants
 * {@link #ZERO}, {@link #ONE}, and {@link #TEN} are provided.
 */
public final class Quantity {

    public static final Quantity ZERO = new Quantity(BigInteger.ZERO);

    public static final Quantity ONE = new Quantity(BigInteger.ONE);

    public static final Quantity TEN = new Quantity(BigInteger.TEN);

    private final BigInteger value;

    private Quantity(BigInteger value) {
        this.value = value;
    }

    /**
     * Returns the sum of this quantity and the given value.
     *
     * @param value the value to add
     * @return a new {@code Quantity} representing the sum
     */
    public Quantity add(BigInteger value) {
        return new Quantity(this.value.add(value));
    }

    /**
     * Returns the sum of this quantity and the given quantity.
     *
     * @param quantity the quantity to add
     * @return a new {@code Quantity} representing the sum
     */
    public Quantity add(Quantity quantity) {
        return new Quantity(this.value.add(quantity.value));
    }

    /**
     * Returns the difference between this quantity and the given value.
     *
     * @param value the value to subtract
     * @return a new {@code Quantity} representing the difference
     */
    public Quantity subtract(BigInteger value) {
        return new Quantity(this.value.subtract(value));
    }

    /**
     * Returns the difference between this quantity and the given quantity.
     *
     * @param quantity the quantity to subtract
     * @return a new {@code Quantity} representing the difference
     */
    public Quantity subtract(Quantity quantity) {
        return new Quantity(this.value.subtract(quantity.value));
    }

    /**
     * Returns the product of this quantity and the given value.
     *
     * @param value the multiplier
     * @return a new {@code Quantity} representing the product
     */
    public Quantity multiply(BigInteger value) {
        return new Quantity(this.value.multiply(value));
    }

    /**
     * Returns the quotient of this quantity divided by the given value.
     *
     * @param value the divisor
     * @return a new {@code Quantity} representing the quotient
     */
    public Quantity divide(BigInteger value) {
        return new Quantity(this.value.divide(value));
    }

    /**
     * Returns the quotient of this quantity divided by the given value.
     *
     * @param value the divisor
     * @return a new {@code Quantity} representing the quotient
     */
    public Quantity divide(int value) {
        return new Quantity(this.value.divide(BigInteger.valueOf(value)));
    }

    /**
     * Returns the quotient of this quantity divided by the given quantity.
     *
     * @param quantity the quantity to divide by
     * @return a new {@code Quantity} representing the quotient
     */
    public Quantity divide(Quantity quantity) {
        return new Quantity(this.value.divide(quantity.value));
    }

    /**
     * Returns the product of this quantity and the given value.
     *
     * @param value the multiplier
     * @return a new {@code Quantity} representing the product
     */
    public Quantity multiply(int value) {
        return new Quantity(this.value.multiply(BigInteger.valueOf(value)));
    }

    /**
     * Returns the product of this quantity and the given quantity.
     *
     * @param quantity the quantity to multiply by
     * @return a new {@code Quantity} representing the product
     */
    public Quantity multiply(Quantity quantity) {
        return new Quantity(this.value.multiply(quantity.value));
    }

    /**
     * Checks whether this quantity is strictly greater than the given quantity.
     *
     * @param quantity the quantity to compare to
     * @return {@code true} if this quantity is greater
     */
    public Boolean isGreaterThan(Quantity quantity) {
        return value.compareTo(quantity.value) > 0;
    }

    /**
     * Checks whether this quantity equals zero.
     *
     * @return {@code true} if this quantity is zero
     */
    public Boolean isZero() {
        return this.isEqualTo(ZERO);
    }

    /**
     * Checks whether this quantity is strictly less than the given quantity.
     *
     * @param quantity the quantity to compare to
     * @return {@code true} if this quantity is less
     */
    public Boolean isLessThan(Quantity quantity) {
        return value.compareTo(quantity.value) < 0;
    }

    /**
     * Checks whether this quantity is greater than or equal to the given quantity.
     *
     * @param quantity the quantity to compare to
     * @return {@code true} if this quantity is greater or equal
     */
    public Boolean isGreaterThanOrEqualTo(Quantity quantity) {
        return value.compareTo(quantity.value) >= 0;
    }

    /**
     * Checks whether this quantity is less than or equal to the given quantity.
     *
     * @param quantity the quantity to compare to
     * @return {@code true} if this quantity is less or equal
     */
    public Boolean isLessThanOrEqualTo(Quantity quantity) {
        return value.compareTo(quantity.value) <= 0;
    }

    /**
     * Checks whether this quantity is less than or equal to zero.
     *
     * @return {@code true} if this quantity &lt;= 0
     */
    public Boolean isLessOrEqualThanZero() {
        return this.isLessThan(Quantity.ZERO) || this.isEqualTo(Quantity.ZERO);
    }

    /**
     * Checks whether this quantity equals the given quantity.
     *
     * @param quantity the quantity to compare to
     * @return {@code true} if the quantities are equal
     */
    public Boolean isEqualTo(Quantity quantity) {
        return value.compareTo(quantity.value) == 0;
    }

    /**
     * Returns this quantity as an {@code int}.
     *
     * @return the integer value
     */
    public Integer intValue() {
        return value.intValue();
    }

    /**
     * Returns the underlying numeric value.
     *
     * @return the {@link BigInteger} value
     */
    public BigInteger value() {
        return value;
    }

    /**
     * Creates a new {@code Quantity} from a {@link BigInteger} value.
     *
     * @param value the numeric value
     * @return a new {@code Quantity} instance
     */
    public static Quantity of(BigInteger value) {
        return new Quantity(value);
    }

    /**
     * Creates a new {@code Quantity} from a string representation.
     *
     * @param value the numeric string
     * @return a new {@code Quantity} instance
     */
    public static Quantity of(String value) {
        return new Quantity(new BigInteger(value));
    }

    /**
     * Creates a new {@code Quantity} from a {@code long} value.
     *
     * @param value the numeric value
     * @return a new {@code Quantity} instance
     */
    public static Quantity of(long value) {
        return new Quantity(BigInteger.valueOf(value));
    }

    /**
     * Creates a new {@code Quantity} from an {@code int} value.
     *
     * @param value the numeric value
     * @return a new {@code Quantity} instance
     */
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
