package az.kon.academy.catalog.avro.mapper.common;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class AvroTypeMappers {

    public Instant map(OffsetDateTime value) {
        return value == null ? null : value.toInstant();
    }

    public OffsetDateTime map(Instant value) {
        return value == null ? null : OffsetDateTime.ofInstant(value, ZoneOffset.UTC);
    }

    public String map(CharSequence value) {
        return value == null ? null : value.toString();
    }
}
