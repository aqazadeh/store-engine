package az.kon.academy.aggragate.valueobject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Objects;

public final class Money {
    public static final Money ZERO = new Money(BigDecimal.ZERO);
    private final BigDecimal value;

    private Money(BigDecimal value) {
        this.value = value;
    }

    public Money add(Money other) {
        return new Money(this.value.add(other.value));
    }

    public Money subtract(Money other) {
        return new Money(this.value.subtract(other.value));
    }

    public Money multiply(BigDecimal multiplier) {
        return new Money(this.value.multiply(multiplier).setScale(2, RoundingMode.HALF_UP));
    }

    public Money multiply(BigInteger multiplier) {
        return this.multiply(BigDecimal.valueOf(multiplier.longValue()));
    }

    public Money multiply(Quantity quantity) {
        return this.multiply(quantity.value());
    }

    public Money divide(BigDecimal divisor) {
        return new Money(this.value.divide(divisor, 2, RoundingMode.HALF_UP));
    }

    public Money divide(BigInteger divisor) {
        return this.divide(BigDecimal.valueOf(divisor.longValue()));
    }

    public Money divide(Quantity quantity) {
        return this.divide(quantity.value());
    }

    public Boolean isGreaterThan(Money money) {
        return value.compareTo(money.value) > 0;
    }

    public Boolean isLessThan(Money money) {
        return value.compareTo(money.value) < 0;
    }

    public Boolean isGreaterThanOrEqualTo(Money money) {
        return value.compareTo(money.value) >= 0;
    }

    public Boolean isLessOrEqualThanZero() {
        return this.isLessThan(ZERO) || this.isEqualTo(ZERO);
    }

    public Boolean isLessThanZero() {
        return this.isLessThan(ZERO);
    }

    public Boolean isEqualTo(Money money) {
        return value.compareTo(money.value) == 0;
    }

    public Money min(Money money) {
        return this.isLessThan(money) ? this : money;
    }

    public Money max(Money money) {
        return this.isGreaterThan(money) ? this : money;
    }

    public BigDecimal value() {
        return this.value;
    }

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
