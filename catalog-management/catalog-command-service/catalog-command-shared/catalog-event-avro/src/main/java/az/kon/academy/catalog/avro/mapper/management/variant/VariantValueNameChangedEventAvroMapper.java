package az.kon.academy.catalog.avro.mapper.management.variant;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.management.variant.VariantValueNameChangedAvroModel;
import az.kon.academy.catalog.event.management.variant.VariantValueNameChangedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface VariantValueNameChangedEventAvroMapper {

    VariantValueNameChangedEventAvroMapper INSTANCE = Mappers.getMapper(VariantValueNameChangedEventAvroMapper.class);

    VariantValueNameChangedAvroModel toAvro(VariantValueNameChangedEvent event);

    VariantValueNameChangedEvent toEvent(VariantValueNameChangedAvroModel avroModel);
}
