package az.kon.academy.avro.plugin.conversion;

import org.apache.avro.Conversion;
import org.apache.avro.LogicalType;
import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class OffsetDateTimeConversion extends Conversion<OffsetDateTime> {

    @Override
    public Class<OffsetDateTime> getConvertedType() {
        return OffsetDateTime.class;
    }

    @Override
    public String getLogicalTypeName() {
        return "timestamp-millis";
    }

    @Override
    public Schema getRecommendedSchema() {
        return LogicalTypes.timestampMillis().addToSchema(Schema.create(Schema.Type.LONG));
    }

    @Override
    public OffsetDateTime fromLong(Long millis, Schema schema, LogicalType type) {
        if (millis == null) return null;
        return OffsetDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneOffset.UTC);
    }

    @Override
    public Long toLong(OffsetDateTime value, Schema schema, LogicalType type) {
        if (value == null) return null;
        return value.toInstant().toEpochMilli();
    }
}
