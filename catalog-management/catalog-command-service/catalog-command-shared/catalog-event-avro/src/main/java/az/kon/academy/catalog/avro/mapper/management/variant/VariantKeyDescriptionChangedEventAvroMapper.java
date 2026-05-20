package az.kon.academy.catalog.avro.mapper.management.variant;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.management.variant.VariantKeyDescriptionChangedAvroModel;
import az.kon.academy.catalog.event.management.variant.VariantKeyDescriptionChangedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface VariantKeyDescriptionChangedEventAvroMapper {

    VariantKeyDescriptionChangedEventAvroMapper INSTANCE = Mappers.getMapper(VariantKeyDescriptionChangedEventAvroMapper.class);

    VariantKeyDescriptionChangedAvroModel toAvro(VariantKeyDescriptionChangedEvent event);

    VariantKeyDescriptionChangedEvent toEvent(VariantKeyDescriptionChangedAvroModel avroModel);
}
