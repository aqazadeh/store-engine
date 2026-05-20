package az.kon.academy.avro.plugin.mapper;

public class MapperSpec {

    public final String mapperPackage;
    public final String mapperName;
    public final String source;

    public MapperSpec(String mapperPackage, String mapperName, String source) {
        this.mapperPackage = mapperPackage;
        this.mapperName = mapperName;
        this.source = source;
    }
}
