package az.kon.academy.catalog.avro.mapper.management.variant;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.management.variant.VariantKeyNameChangedAvroModel;
import az.kon.academy.catalog.event.management.variant.VariantKeyNameChangedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface VariantKeyNameChangedEventAvroMapper {

    VariantKeyNameChangedEventAvroMapper INSTANCE = Mappers.getMapper(VariantKeyNameChangedEventAvroMapper.class);

    VariantKeyNameChangedAvroModel toAvro(VariantKeyNameChangedEvent event);

    VariantKeyNameChangedEvent toEvent(VariantKeyNameChangedAvroModel avroModel);
}
