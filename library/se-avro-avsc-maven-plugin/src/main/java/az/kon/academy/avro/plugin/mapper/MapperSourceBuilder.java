package az.kon.academy.avro.plugin.mapper;

import org.apache.avro.Schema;

public class MapperSourceBuilder {

    private final String componentModel;
    private final String mapStructConfigClass;
    private final String avroModelSuffix;

    public MapperSourceBuilder(String componentModel, String mapStructConfigClass, String avroModelSuffix) {
        this.componentModel = componentModel;
        this.mapStructConfigClass = mapStructConfigClass;
        this.avroModelSuffix = avroModelSuffix;
    }

    public MapperSpec build(Schema schema) {
        String modelName = schema.getName();
        String namespace = schema.getNamespace();

        String baseName = modelName.endsWith(avroModelSuffix)
                ? modelName.substring(0, modelName.length() - avroModelSuffix.length())
                : modelName;

        String eventName = baseName + "Event";
        String mapperName = baseName + "EventAvroMapper";
        String mapperPackage = namespace.replace(".avro.model", ".avro.mapper");
        String eventPackage = deriveEventPackage(namespace);

        String source = buildSource(mapperPackage, namespace, modelName, eventPackage, eventName, mapperName);
        return new MapperSpec(mapperPackage, mapperName, source);
    }

    private String deriveEventPackage(String avroModelNamespace) {
        int idx = avroModelNamespace.indexOf(".avro.model");
        String catalogBase = avroModelNamespace.substring(0, idx);
        String subPackageSuffix = avroModelNamespace.substring(idx + ".avro.model".length());
        return catalogBase + ".event" + subPackageSuffix;
    }

    private String buildSource(String mapperPackage, String modelNamespace, String modelName,
                               String eventPackage, String eventName, String mapperName) {
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(mapperPackage).append(";\n\n");

        if (hasConfigClass()) {
            sb.append("import ").append(mapStructConfigClass).append(";\n");
        }
        sb.append("import ").append(modelNamespace).append(".").append(modelName).append(";\n");
        sb.append("import ").append(eventPackage).append(".").append(eventName).append(";\n");
        sb.append("import org.mapstruct.Mapper;\n");
        if (isDefaultComponentModel()) {
            sb.append("import org.mapstruct.factory.Mappers;\n");
        }
        sb.append("\n");

        sb.append("@Mapper(componentModel = \"").append(componentModel).append("\"");
        if (hasConfigClass()) {
            String configSimpleName = mapStructConfigClass.substring(mapStructConfigClass.lastIndexOf('.') + 1);
            sb.append(", config = ").append(configSimpleName).append(".class");
        }
        sb.append(")\n");

        sb.append("public interface ").append(mapperName).append(" {\n\n");
        if (isDefaultComponentModel()) {
            sb.append("    ").append(mapperName)
              .append(" INSTANCE = Mappers.getMapper(").append(mapperName).append(".class);\n\n");
        }
        sb.append("    ").append(modelName).append(" toAvro(").append(eventName).append(" event);\n\n");
        sb.append("    ").append(eventName).append(" toEvent(").append(modelName).append(" avroModel);\n");
        sb.append("}\n");

        return sb.toString();
    }

    private boolean hasConfigClass() {
        return mapStructConfigClass != null && !mapStructConfigClass.isBlank();
    }

    private boolean isDefaultComponentModel() {
        return "default".equalsIgnoreCase(componentModel);
    }
}
