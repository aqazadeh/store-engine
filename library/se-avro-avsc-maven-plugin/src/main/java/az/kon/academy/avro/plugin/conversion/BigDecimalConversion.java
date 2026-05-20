package az.kon.academy.avro.plugin.conversion;

import org.apache.avro.Conversions;
import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;

public class BigDecimalConversion extends Conversions.DecimalConversion {

    private static final int DEFAULT_PRECISION = 38;
    private static final int DEFAULT_SCALE = 10;

    @Override
    public Schema getRecommendedSchema() {
        return LogicalTypes.decimal(DEFAULT_PRECISION, DEFAULT_SCALE)
                .addToSchema(Schema.create(Schema.Type.BYTES));
    }
}
