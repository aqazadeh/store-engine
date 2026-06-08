package az.kon.academy.avro.plugin.schema;

import az.kon.academy.avro.plugin.conversion.*;
import org.apache.avro.Conversions;
import org.apache.avro.Schema;
import org.apache.avro.data.TimeConversions;
import org.apache.avro.reflect.ReflectData;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AvroSchemaBuilder {

    private final ReflectData reflectData;
    private final String classSuffix;

    public AvroSchemaBuilder(String classSuffix) {
        this.classSuffix = classSuffix;
        this.reflectData = new ReflectData();

        // built-in Avro conversions
        this.reflectData.addLogicalTypeConversion(new Conversions.UUIDConversion());
        this.reflectData.addLogicalTypeConversion(new Conversions.BigDecimalConversion());
        this.reflectData.addLogicalTypeConversion(new Conversions.DurationConversion());

        this.reflectData.addLogicalTypeConversion(new TimeConversions.DateConversion());
        this.reflectData.addLogicalTypeConversion(new TimeConversions.TimeMillisConversion());
        this.reflectData.addLogicalTypeConversion(new TimeConversions.TimeMicrosConversion());
        this.reflectData.addLogicalTypeConversion(new TimeConversions.TimestampMillisConversion());
        this.reflectData.addLogicalTypeConversion(new TimeConversions.TimestampMicrosConversion());
        this.reflectData.addLogicalTypeConversion(new TimeConversions.TimestampNanosConversion());
        this.reflectData.addLogicalTypeConversion(new TimeConversions.LocalTimestampMillisConversion());
        this.reflectData.addLogicalTypeConversion(new TimeConversions.LocalTimestampMicrosConversion());
        this.reflectData.addLogicalTypeConversion(new TimeConversions.LocalTimestampNanosConversion());

        // custom conversions
        this.reflectData.addLogicalTypeConversion(new OffsetDateTimeConversion());
        this.reflectData.addLogicalTypeConversion(new BigIntegerConversion());
        this.reflectData.addLogicalTypeConversion(new UriConversion());
        this.reflectData.addLogicalTypeConversion(new ZoneIdConversion());
    }

    public Optional<Schema> buildSchema(Class<?> clazz, String eventBasePackage) {
        if (clazz.isSynthetic() || clazz.isAnonymousClass() || clazz.isLocalClass() || clazz.isMemberClass()) {
            return Optional.empty();
        }
        String pkg = clazz.getPackageName();
        if (!pkg.equals(eventBasePackage) && !pkg.startsWith(eventBasePackage + ".")) {
            return Optional.empty();
        }
        if (!clazz.getSimpleName().endsWith(classSuffix)) {
            return Optional.empty();
        }

        Schema reflected = reflectData.getSchema(clazz);

        String base = clazz.getSimpleName().substring(0, clazz.getSimpleName().length() - classSuffix.length());
        String avroName = base + "AvroModel";
        String avroNamespace = deriveAvroNamespace(pkg, eventBasePackage);

        List<Schema.Field> fields = reflected.getFields().stream()
                .map(f -> new Schema.Field(f.name(), f.schema(), f.doc(), f.defaultVal(), f.order()))
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
