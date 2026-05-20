package az.kon.academy.catalog.avro.mapper.brand;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.brand.BrandMovedToDraftAvroModel;
import az.kon.academy.catalog.event.brand.BrandMovedToDraftEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface BrandMovedToDraftEventAvroMapper {

    BrandMovedToDraftEventAvroMapper INSTANCE = Mappers.getMapper(BrandMovedToDraftEventAvroMapper.class);

    BrandMovedToDraftAvroModel toAvro(BrandMovedToDraftEvent event);

    BrandMovedToDraftEvent toEvent(BrandMovedToDraftAvroModel avroModel);
}
