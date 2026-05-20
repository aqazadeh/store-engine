package az.kon.academy.catalog.avro.mapper.brand;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.brand.BrandCreatedGlobalAvroModel;
import az.kon.academy.catalog.event.brand.BrandCreatedGlobalEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface BrandCreatedGlobalEventAvroMapper {

    BrandCreatedGlobalEventAvroMapper INSTANCE = Mappers.getMapper(BrandCreatedGlobalEventAvroMapper.class);

    BrandCreatedGlobalAvroModel toAvro(BrandCreatedGlobalEvent event);

    BrandCreatedGlobalEvent toEvent(BrandCreatedGlobalAvroModel avroModel);
}
