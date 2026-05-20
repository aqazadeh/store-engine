package az.kon.academy.avro.plugin.conversion;

import org.apache.avro.Conversion;
import org.apache.avro.LogicalType;
import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;

import java.time.ZoneId;

public class ZoneIdConversion extends Conversion<ZoneId> {

    static {
        LogicalTypes.register("zoneId", schema -> new LogicalType("zoneId"));
    }

    @Override
    public Class<ZoneId> getConvertedType() {
        return ZoneId.class;
    }

    @Override
    public String getLogicalTypeName() {
        return "zoneId";
    }

    @Override
    public Schema getRecommendedSchema() {
        return new LogicalType("zoneId").addToSchema(Schema.create(Schema.Type.STRING));
    }

    @Override
    public ZoneId fromCharSequence(CharSequence value, Schema schema, LogicalType type) {
        if (value == null) return null;
        return ZoneId.of(value.toString());
    }

    @Override
    public CharSequence toCharSequence(ZoneId value, Schema schema, LogicalType type) {
        if (value == null) return null;
        return value.getId();
    }
}
