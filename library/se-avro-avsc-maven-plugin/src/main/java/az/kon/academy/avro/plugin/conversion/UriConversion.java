package az.kon.academy.avro.plugin.conversion;

import org.apache.avro.Conversion;
import org.apache.avro.LogicalType;
import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;

import java.net.URI;

public class UriConversion extends Conversion<URI> {

    static {
        LogicalTypes.register("uri", schema -> new LogicalType("uri"));
    }

    @Override
    public Class<URI> getConvertedType() {
        return URI.class;
    }

    @Override
    public String getLogicalTypeName() {
        return "uri";
    }

    @Override
    public Schema getRecommendedSchema() {
        return new LogicalType("uri").addToSchema(Schema.create(Schema.Type.STRING));
    }

    @Override
    public URI fromCharSequence(CharSequence value, Schema schema, LogicalType type) {
        if (value == null) return null;
        return URI.create(value.toString());
    }

    @Override
    public CharSequence toCharSequence(URI value, Schema schema, LogicalType type) {
        if (value == null) return null;
        return value.toString();
    }
}
