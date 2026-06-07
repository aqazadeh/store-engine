package az.kon.academy.catalog.avro.common;

import org.apache.avro.Conversions;
import org.apache.avro.LogicalTypes;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class AvroTypeMappers {
    Conversions.BigDecimalConversion conversion = new Conversions.BigDecimalConversion();

    public Instant map(OffsetDateTime value) {
        return value == null ? null : value.toInstant();
    }

    public OffsetDateTime map(Instant value) {
        return value == null ? null : OffsetDateTime.ofInstant(value, ZoneOffset.UTC);
    }

    public String map(CharSequence value) {
        return value == null ? null : value.toString();
    }

    public BigDecimal map(ByteBuffer buffer) {
        if (buffer == null) {
            return null;
        }

        return conversion.fromBytes(
                buffer,
                null,
                LogicalTypes.decimal(38, 10)
        );
    }

    public ByteBuffer map(BigDecimal value) {
        if (value == null) {
            return null;
        }

        return conversion.toBytes(
                value,
                null,
                LogicalTypes.decimal(38, 10)
        );
    }
}
