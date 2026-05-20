package az.kon.academy.catalog.avro.mapper.management.variant;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.management.variant.VariantKeyCreatedAvroModel;
import az.kon.academy.catalog.event.management.variant.VariantKeyCreatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface VariantKeyCreatedEventAvroMapper {

    VariantKeyCreatedEventAvroMapper INSTANCE = Mappers.getMapper(VariantKeyCreatedEventAvroMapper.class);

    VariantKeyCreatedAvroModel toAvro(VariantKeyCreatedEvent event);

    VariantKeyCreatedEvent toEvent(VariantKeyCreatedAvroModel avroModel);
}
