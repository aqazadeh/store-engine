package az.kon.academy.catalog.avro.mapper.brand;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.brand.BrandInformationChangedAvroModel;
import az.kon.academy.catalog.event.brand.BrandInformationChangedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface BrandInformationChangedEventAvroMapper {

    BrandInformationChangedEventAvroMapper INSTANCE = Mappers.getMapper(BrandInformationChangedEventAvroMapper.class);

    BrandInformationChangedAvroModel toAvro(BrandInformationChangedEvent event);

    BrandInformationChangedEvent toEvent(BrandInformationChangedAvroModel avroModel);
}
