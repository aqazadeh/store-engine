package az.kon.academy.avro.plugin.conversion;

import org.apache.avro.Conversion;
import org.apache.avro.LogicalType;
import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class BigIntegerConversion extends Conversion<BigInteger> {

    static {
        LogicalTypes.register("biginteger", schema -> new LogicalType("biginteger"));
    }

    @Override
    public Class<BigInteger> getConvertedType() {
        return BigInteger.class;
    }

    @Override
    public String getLogicalTypeName() {
        return "biginteger";
    }

    @Override
    public Schema getRecommendedSchema() {
        return new LogicalType("biginteger").addToSchema(Schema.create(Schema.Type.BYTES));
    }

    @Override
    public BigInteger fromBytes(ByteBuffer value, Schema schema, LogicalType type) {
        if (value == null) return null;
        byte[] bytes = new byte[value.remaining()];
        value.get(bytes);
        return new BigInteger(bytes);
    }

    @Override
    public ByteBuffer toBytes(BigInteger value, Schema schema, LogicalType type) {
        if (value == null) return null;
        return ByteBuffer.wrap(value.toByteArray());
    }
}
