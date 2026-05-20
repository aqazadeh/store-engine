package az.kon.academy.catalog.avro.mapper.management.variant;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.management.variant.VariantValueCreatedAvroModel;
import az.kon.academy.catalog.event.management.variant.VariantValueCreatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface VariantValueCreatedEventAvroMapper {

    VariantValueCreatedEventAvroMapper INSTANCE = Mappers.getMapper(VariantValueCreatedEventAvroMapper.class);

    VariantValueCreatedAvroModel toAvro(VariantValueCreatedEvent event);

    VariantValueCreatedEvent toEvent(VariantValueCreatedAvroModel avroModel);
}
