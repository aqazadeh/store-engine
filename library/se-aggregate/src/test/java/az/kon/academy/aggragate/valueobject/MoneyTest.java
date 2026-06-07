package az.kon.academy.aggragate.valueobject;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void zeroConstant() {
        assertEquals(BigDecimal.ZERO, Money.ZERO.value());
    }

    @Test
    void ofCreatesMoney() {
        Money m = Money.of(new BigDecimal("100.00"));
        assertEquals(new BigDecimal("100.00"), m.value());
    }

    @Test
    void add() {
        Money a = Money.of(new BigDecimal("10.00"));
        Money b = Money.of(new BigDecimal("15.50"));
        assertEquals(new BigDecimal("25.50"), a.add(b).value());
    }

    @Test
    void subtract() {
        Money a = Money.of(new BigDecimal("20.00"));
        Money b = Money.of(new BigDecimal("7.25"));
        assertEquals(new BigDecimal("12.75"), a.subtract(b).value());
    }

    @Test
    void multiplyBigDecimal() {
        Money a = Money.of(new BigDecimal("10.00"));
        Money result = a.multiply(new BigDecimal("3.3"));
        assertEquals(new BigDecimal("33.00"), result.value());
    }

    @Test
    void multiplyBigDecimalRoundsHalfUp() {
        Money a = Money.of(new BigDecimal("10.00"));
        Money result = a.multiply(new BigDecimal("0.333"));
        assertEquals(new BigDecimal("3.33"), result.value());
    }

    @Test
    void multiplyBigInteger() {
        Money a = Money.of(new BigDecimal("5.00"));
        assertEquals(new BigDecimal("15.00"), a.multiply(BigInteger.valueOf(3)).value());
    }

    @Test
    void multiplyQuantity() {
        Money a = Money.of(new BigDecimal("5.00"));
        assertEquals(new BigDecimal("15.00"), a.multiply(Quantity.of(3)).value());
    }

    @Test
    void divideBigDecimal() {
        Money a = Money.of(new BigDecimal("10.00"));
        Money result = a.divide(new BigDecimal("3"));
        assertEquals(new BigDecimal("3.33"), result.value());
    }

    @Test
    void divideBigInteger() {
        Money a = Money.of(new BigDecimal("10.00"));
        assertEquals(new BigDecimal("5.00"), a.divide(BigInteger.valueOf(2)).value());
    }

    @Test
    void divideQuantity() {
        Money a = Money.of(new BigDecimal("10.00"));
        assertEquals(new BigDecimal("5.00"), a.divide(Quantity.of(2)).value());
    }

    @Test
    void isGreaterThan() {
        Money a = Money.of(new BigDecimal("20.00"));
        Money b = Money.of(new BigDecimal("10.00"));
        assertTrue(a.isGreaterThan(b));
        assertFalse(b.isGreaterThan(a));
        assertFalse(a.isGreaterThan(Money.of(new BigDecimal("20.00"))));
    }

    @Test
    void isLessThan() {
        Money a = Money.of(new BigDecimal("5.00"));
        Money b = Money.of(new BigDecimal("10.00"));
        assertTrue(a.isLessThan(b));
        assertFalse(b.isLessThan(a));
        assertFalse(a.isLessThan(Money.of(new BigDecimal("5.00"))));
    }

    @Test
    void isGreaterThanOrEqualTo() {
        Money a = Money.of(new BigDecimal("20.00"));
        Money b = Money.of(new BigDecimal("10.00"));
        assertTrue(a.isGreaterThanOrEqualTo(b));
        assertTrue(a.isGreaterThanOrEqualTo(Money.of(new BigDecimal("20.00"))));
        assertFalse(b.isGreaterThanOrEqualTo(a));
    }

    @Test
    void isLessOrEqualThanZero() {
        assertTrue(Money.ZERO.isLessOrEqualThanZero());
        assertTrue(Money.of(new BigDecimal("-1.00")).isLessOrEqualThanZero());
        assertFalse(Money.of(new BigDecimal("1.00")).isLessOrEqualThanZero());
    }

    @Test
    void isLessThanZero() {
        assertTrue(Money.of(new BigDecimal("-1.00")).isLessThanZero());
        assertFalse(Money.ZERO.isLessThanZero());
        assertFalse(Money.of(new BigDecimal("1.00")).isLessThanZero());
    }

    @Test
    void isEqualTo() {
        assertTrue(Money.of(new BigDecimal("10.00")).isEqualTo(Money.of(new BigDecimal("10.00"))));
        assertFalse(Money.of(new BigDecimal("10.00")).isEqualTo(Money.of(new BigDecimal("20.00"))));
    }

    @Test
    void min() {
        Money a = Money.of(new BigDecimal("5.00"));
        Money b = Money.of(new BigDecimal("10.00"));
        assertEquals(new BigDecimal("5.00"), a.min(b).value());
        assertEquals(new BigDecimal("5.00"), b.min(a).value());
    }

    @Test
    void max() {
        Money a = Money.of(new BigDecimal("5.00"));
        Money b = Money.of(new BigDecimal("10.00"));
        assertEquals(new BigDecimal("10.00"), a.max(b).value());
        assertEquals(new BigDecimal("10.00"), b.max(a).value());
    }

    @Test
    void immutability() {
        Money a = Money.of(new BigDecimal("10.00"));
        Money b = a.add(Money.of(new BigDecimal("5.00")));
        assertEquals(new BigDecimal("10.00"), a.value());
        assertEquals(new BigDecimal("15.00"), b.value());
    }

    @Test
    void equalsAndHashCode() {
        Money a = Money.of(new BigDecimal("10.00"));
        Money b = Money.of(new BigDecimal("10.00"));
        Money c = Money.of(new BigDecimal("20.00"));

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(a, null);
        assertNotEquals(a, new Object());
    }

    @Test
    void toStringReturnsValue() {
        assertEquals("10.00", Money.of(new BigDecimal("10.00")).toString());
    }
}
