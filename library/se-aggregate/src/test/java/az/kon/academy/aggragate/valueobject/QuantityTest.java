package az.kon.academy.aggragate.valueobject;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class QuantityTest {

    @Test
    void zeroConstant() {
        assertEquals(BigInteger.ZERO, Quantity.ZERO.value());
    }

    @Test
    void oneConstant() {
        assertEquals(BigInteger.ONE, Quantity.ONE.value());
    }

    @Test
    void tenConstant() {
        assertEquals(BigInteger.TEN, Quantity.TEN.value());
    }

    @Test
    void ofBigInteger() {
        assertEquals(BigInteger.valueOf(42), Quantity.of(BigInteger.valueOf(42)).value());
    }

    @Test
    void ofString() {
        assertEquals(BigInteger.valueOf(99), Quantity.of("99").value());
    }

    @Test
    void ofLong() {
        assertEquals(BigInteger.valueOf(100L), Quantity.of(100L).value());
    }

    @Test
    void ofInt() {
        assertEquals(BigInteger.valueOf(50), Quantity.of(50).value());
    }

    @Test
    void addBigInteger() {
        Quantity q = Quantity.of(10);
        assertEquals(BigInteger.valueOf(25), q.add(BigInteger.valueOf(15)).value());
    }

    @Test
    void addQuantity() {
        Quantity a = Quantity.of(10);
        Quantity b = Quantity.of(15);
        assertEquals(BigInteger.valueOf(25), a.add(b).value());
    }

    @Test
    void subtractBigInteger() {
        Quantity q = Quantity.of(20);
        assertEquals(BigInteger.valueOf(5), q.subtract(BigInteger.valueOf(15)).value());
    }

    @Test
    void subtractQuantity() {
        Quantity a = Quantity.of(20);
        Quantity b = Quantity.of(15);
        assertEquals(BigInteger.valueOf(5), a.subtract(b).value());
    }

    @Test
    void multiplyBigInteger() {
        Quantity q = Quantity.of(6);
        assertEquals(BigInteger.valueOf(42), q.multiply(BigInteger.valueOf(7)).value());
    }

    @Test
    void multiplyInt() {
        Quantity q = Quantity.of(6);
        assertEquals(BigInteger.valueOf(42), q.multiply(7).value());
    }

    @Test
    void multiplyQuantity() {
        Quantity a = Quantity.of(6);
        Quantity b = Quantity.of(7);
        assertEquals(BigInteger.valueOf(42), a.multiply(b).value());
    }

    @Test
    void divideBigInteger() {
        Quantity q = Quantity.of(42);
        assertEquals(BigInteger.valueOf(7), q.divide(BigInteger.valueOf(6)).value());
    }

    @Test
    void divideInt() {
        Quantity q = Quantity.of(42);
        assertEquals(BigInteger.valueOf(7), q.divide(6).value());
    }

    @Test
    void divideQuantity() {
        Quantity a = Quantity.of(42);
        Quantity b = Quantity.of(6);
        assertEquals(BigInteger.valueOf(7), a.divide(b).value());
    }

    @Test
    void isGreaterThan() {
        assertTrue(Quantity.of(10).isGreaterThan(Quantity.of(5)));
        assertFalse(Quantity.of(5).isGreaterThan(Quantity.of(10)));
        assertFalse(Quantity.of(5).isGreaterThan(Quantity.of(5)));
    }

    @Test
    void isZero() {
        assertTrue(Quantity.ZERO.isZero());
        assertFalse(Quantity.ONE.isZero());
        assertFalse(Quantity.TEN.isZero());
    }

    @Test
    void isLessThan() {
        assertTrue(Quantity.of(5).isLessThan(Quantity.of(10)));
        assertFalse(Quantity.of(10).isLessThan(Quantity.of(5)));
        assertFalse(Quantity.of(5).isLessThan(Quantity.of(5)));
    }

    @Test
    void isGreaterThanOrEqualTo() {
        assertTrue(Quantity.of(10).isGreaterThanOrEqualTo(Quantity.of(5)));
        assertTrue(Quantity.of(10).isGreaterThanOrEqualTo(Quantity.of(10)));
        assertFalse(Quantity.of(5).isGreaterThanOrEqualTo(Quantity.of(10)));
    }

    @Test
    void isLessThanOrEqualTo() {
        assertTrue(Quantity.of(5).isLessThanOrEqualTo(Quantity.of(10)));
        assertTrue(Quantity.of(5).isLessThanOrEqualTo(Quantity.of(5)));
        assertFalse(Quantity.of(10).isLessThanOrEqualTo(Quantity.of(5)));
    }

    @Test
    void isLessOrEqualThanZero() {
        assertTrue(Quantity.ZERO.isLessOrEqualThanZero());
        assertFalse(Quantity.ONE.isLessOrEqualThanZero());
    }

    @Test
    void isEqualTo() {
        assertTrue(Quantity.of(10).isEqualTo(Quantity.of(10)));
        assertFalse(Quantity.of(10).isEqualTo(Quantity.of(5)));
    }

    @Test
    void intValue() {
        assertEquals(42, Quantity.of(42).intValue());
    }

    @Test
    void immutability() {
        Quantity a = Quantity.of(10);
        Quantity b = a.add(BigInteger.valueOf(5));
        assertEquals(BigInteger.valueOf(10), a.value());
        assertEquals(BigInteger.valueOf(15), b.value());
    }

    @Test
    void equalsAndHashCode() {
        Quantity a = Quantity.of(10);
        Quantity b = Quantity.of(10);
        Quantity c = Quantity.of(20);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(a, null);
        assertNotEquals(a, new Object());
    }

    @Test
    void toStringReturnsValue() {
        assertEquals("10", Quantity.of(10).toString());
    }
}
