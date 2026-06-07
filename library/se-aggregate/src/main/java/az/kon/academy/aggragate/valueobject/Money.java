package az.kon.academy.aggragate.valueobject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * An immutable value object representing a monetary amount.
 * <p>
 * Wraps a {@link BigDecimal} with scale-2 rounding and provides
 * arithmetic operations (add, subtract, multiply, divide) and
 * comparison methods. All operations return new instances.
 */
public final class Money {
    public static final Money ZERO = new Money(BigDecimal.ZERO);
    private final BigDecimal value;

    private Money(BigDecimal value) {
        this.value = value;
    }

    /**
     * Returns the sum of this amount and the given amount.
     *
     * @param other the amount to add
     * @return a new {@code Money} representing the sum
     */
    public Money add(Money other) {
        return new Money(this.value.add(other.value));
    }

    /**
     * Returns the difference between this amount and the given amount.
     *
     * @param other the amount to subtract
     * @return a new {@code Money} representing the difference
     */
    public Money subtract(Money other) {
        return new Money(this.value.subtract(other.value));
    }

    /**
     * Returns the product of this amount and the given multiplier,
     * rounded to 2 decimal places via {@link RoundingMode#HALF_UP}.
     *
     * @param multiplier the multiplier
     * @return a new {@code Money} representing the product
     */
    public Money multiply(BigDecimal multiplier) {
        return new Money(this.value.multiply(multiplier).setScale(2, RoundingMode.HALF_UP));
    }

    /**
     * Returns the product of this amount and the given multiplier.
     *
     * @param multiplier the multiplier
     * @return a new {@code Money} representing the product
     */
    public Money multiply(BigInteger multiplier) {
        return this.multiply(BigDecimal.valueOf(multiplier.longValue()));
    }

    /**
     * Returns the product of this amount and the given quantity.
     *
     * @param quantity the quantity to multiply by
     * @return a new {@code Money} representing the product
     */
    public Money multiply(Quantity quantity) {
        return this.multiply(quantity.value());
    }

    /**
     * Returns the quotient of this amount divided by the given divisor,
     * rounded to 2 decimal places via {@link RoundingMode#HALF_UP}.
     *
     * @param divisor the divisor
     * @return a new {@code Money} representing the quotient
     */
    public Money divide(BigDecimal divisor) {
        return new Money(this.value.divide(divisor, 2, RoundingMode.HALF_UP));
    }

    /**
     * Returns the quotient of this amount divided by the given divisor.
     *
     * @param divisor the divisor
     * @return a new {@code Money} representing the quotient
     */
    public Money divide(BigInteger divisor) {
        return this.divide(BigDecimal.valueOf(divisor.longValue()));
    }

    /**
     * Returns the quotient of this amount divided by the given quantity.
     *
     * @param quantity the quantity to divide by
     * @return a new {@code Money} representing the quotient
     */
    public Money divide(Quantity quantity) {
        return this.divide(quantity.value());
    }

    /**
     * Checks whether this amount is strictly greater than the given amount.
     *
     * @param money the amount to compare to
     * @return {@code true} if this amount is greater
     */
    public Boolean isGreaterThan(Money money) {
        return value.compareTo(money.value) > 0;
    }

    /**
     * Checks whether this amount is strictly less than the given amount.
     *
     * @param money the amount to compare to
     * @return {@code true} if this amount is less
     */
    public Boolean isLessThan(Money money) {
        return value.compareTo(money.value) < 0;
    }

    /**
     * Checks whether this amount is greater than or equal to the given amount.
     *
     * @param money the amount to compare to
     * @return {@code true} if this amount is greater or equal
     */
    public Boolean isGreaterThanOrEqualTo(Money money) {
        return value.compareTo(money.value) >= 0;
    }

    /**
     * Checks whether this amount is less than or equal to zero.
     *
     * @return {@code true} if this amount &lt;= 0
     */
    public Boolean isLessOrEqualThanZero() {
        return this.isLessThan(ZERO) || this.isEqualTo(ZERO);
    }

    /**
     * Checks whether this amount is strictly less than zero.
     *
     * @return {@code true} if this amount &lt; 0
     */
    public Boolean isLessThanZero() {
        return this.isLessThan(ZERO);
    }

    /**
     * Checks whether this amount equals the given amount.
     *
     * @param money the amount to compare to
     * @return {@code true} if the amounts are equal
     */
    public Boolean isEqualTo(Money money) {
        return value.compareTo(money.value) == 0;
    }

    /**
     * Returns the smaller of this amount and the given amount.
     *
     * @param money the amount to compare to
     * @return the minimum amount
     */
    public Money min(Money money) {
        return this.isLessThan(money) ? this : money;
    }

    /**
     * Returns the larger of this amount and the given amount.
     *
     * @param money the amount to compare to
     * @return the maximum amount
     */
    public Money max(Money money) {
        return this.isGreaterThan(money) ? this : money;
    }

    /**
     * Returns the underlying monetary value.
     *
     * @return the {@link BigDecimal} amount
     */
    public BigDecimal value() {
        return this.value;
    }

    /**
     * Creates a new {@code Money} from a {@link BigDecimal} amount.
     *
     * @param amount the monetary amount
     * @return a new {@code Money} instance
     */
    public static Money of(BigDecimal amount) {
        return new Money(amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money that = (Money) o;
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
