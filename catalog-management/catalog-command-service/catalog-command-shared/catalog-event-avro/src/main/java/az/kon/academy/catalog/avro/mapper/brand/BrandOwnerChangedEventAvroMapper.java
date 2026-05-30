package az.kon.academy.catalog.avro.mapper.brand;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.brand.BrandOwnerChangedAvroModel;
import az.kon.academy.catalog.event.brand.BrandOwnerChangedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface BrandOwnerChangedEventAvroMapper {

    BrandOwnerChangedEventAvroMapper INSTANCE = Mappers.getMapper(BrandOwnerChangedEventAvroMapper.class);

    BrandOwnerChangedAvroModel toAvro(BrandOwnerChangedEvent event);

    BrandOwnerChangedEvent toEvent(BrandOwnerChangedAvroModel avroModel);
}
