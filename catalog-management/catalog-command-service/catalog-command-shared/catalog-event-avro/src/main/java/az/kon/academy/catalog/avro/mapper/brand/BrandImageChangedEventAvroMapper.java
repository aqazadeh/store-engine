package az.kon.academy.catalog.avro.mapper.brand;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.brand.BrandImageChangedAvroModel;
import az.kon.academy.catalog.event.brand.BrandImageChangedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface BrandImageChangedEventAvroMapper {

    BrandImageChangedEventAvroMapper INSTANCE = Mappers.getMapper(BrandImageChangedEventAvroMapper.class);

    BrandImageChangedAvroModel toAvro(BrandImageChangedEvent event);

    BrandImageChangedEvent toEvent(BrandImageChangedAvroModel avroModel);
}
