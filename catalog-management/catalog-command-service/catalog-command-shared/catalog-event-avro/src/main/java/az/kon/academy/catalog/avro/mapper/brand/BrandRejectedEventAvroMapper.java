package az.kon.academy.catalog.avro.mapper.brand;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.brand.BrandRejectedAvroModel;
import az.kon.academy.catalog.event.brand.BrandRejectedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface BrandRejectedEventAvroMapper {

    BrandRejectedEventAvroMapper INSTANCE = Mappers.getMapper(BrandRejectedEventAvroMapper.class);

    BrandRejectedAvroModel toAvro(BrandRejectedEvent event);

    BrandRejectedEvent toEvent(BrandRejectedAvroModel avroModel);
}
