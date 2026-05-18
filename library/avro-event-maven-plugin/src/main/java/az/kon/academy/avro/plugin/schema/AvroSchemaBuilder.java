package az.kon.academy.avro.plugin.schema;

import az.kon.academy.avro.plugin.conversion.OffsetDateTimeConversion;
import org.apache.avro.Conversion;
import org.apache.avro.Conversions;
import org.apache.avro.Schema;
import org.apache.avro.reflect.ReflectData;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AvroSchemaBuilder {

    private final ReflectData reflectData;

    public AvroSchemaBuilder(List<Conversion<?>> extraConversions) {
        this.reflectData = new ReflectData();
        this.reflectData.addLogicalTypeConversion(new OffsetDateTimeConversion());
        this.reflectData.addLogicalTypeConversion(new Conversions.UUIDConversion());
        extraConversions.forEach(this.reflectData::addLogicalTypeConversion);
    }

    public Optional<Schema> buildSchema(Class<?> eventClass, String eventBasePackage) {
        String pkg = eventClass.getPackageName();
        if (!pkg.startsWith(eventBasePackage)) return Optional.empty();
        if (!eventClass.getSimpleName().endsWith("Event")) return Optional.empty();

        Schema reflected = reflectData.getSchema(eventClass);

        String avroName = eventClass.getSimpleName().replace("Event", "AvroModel");
        String avroNamespace = deriveAvroNamespace(pkg, eventBasePackage);

        List<Schema.Field> fields = reflected.getFields().stream()
                .map(f -> new Schema.Field(f.name(), f.schema(), f.doc(), f.defaultVal()))
                .collect(Collectors.toList());

        return Optional.of(Schema.createRecord(avroName, null, avroNamespace, false, fields));
    }

    private String deriveAvroNamespace(String eventPackage, String eventBasePackage) {
        int lastDot = eventBasePackage.lastIndexOf('.');
        String catalogBase = eventBasePackage.substring(0, lastDot);
        String suffix = eventPackage.substring(eventBasePackage.length());
        return catalogBase + ".avro.model" + suffix;
    }
}
