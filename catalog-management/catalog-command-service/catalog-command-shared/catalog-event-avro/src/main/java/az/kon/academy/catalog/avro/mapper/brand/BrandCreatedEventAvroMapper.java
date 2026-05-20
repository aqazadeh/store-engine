package az.kon.academy.catalog.avro.mapper.brand;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.brand.BrandCreatedAvroModel;
import az.kon.academy.catalog.event.brand.BrandCreatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface BrandCreatedEventAvroMapper {

    BrandCreatedEventAvroMapper INSTANCE = Mappers.getMapper(BrandCreatedEventAvroMapper.class);

    BrandCreatedAvroModel toAvro(BrandCreatedEvent event);

    BrandCreatedEvent toEvent(BrandCreatedAvroModel avroModel);
}
