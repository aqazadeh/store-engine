package az.kon.academy.catalog.avro.mapper.brand;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.brand.BrandToGlobalChangedAvroModel;
import az.kon.academy.catalog.event.brand.BrandToGlobalChangedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface BrandToGlobalChangedEventAvroMapper {

    BrandToGlobalChangedEventAvroMapper INSTANCE = Mappers.getMapper(BrandToGlobalChangedEventAvroMapper.class);

    BrandToGlobalChangedAvroModel toAvro(BrandToGlobalChangedEvent event);

    BrandToGlobalChangedEvent toEvent(BrandToGlobalChangedAvroModel avroModel);
}
