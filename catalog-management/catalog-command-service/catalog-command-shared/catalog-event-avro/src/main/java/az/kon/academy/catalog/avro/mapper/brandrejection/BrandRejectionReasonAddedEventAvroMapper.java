package az.kon.academy.catalog.avro.mapper.brandrejection;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.brandrejection.BrandRejectionReasonAddedAvroModel;
import az.kon.academy.catalog.event.brandrejection.BrandRejectionReasonAddedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface BrandRejectionReasonAddedEventAvroMapper {

    BrandRejectionReasonAddedEventAvroMapper INSTANCE = Mappers.getMapper(BrandRejectionReasonAddedEventAvroMapper.class);

    BrandRejectionReasonAddedAvroModel toAvro(BrandRejectionReasonAddedEvent event);

    BrandRejectionReasonAddedEvent toEvent(BrandRejectionReasonAddedAvroModel avroModel);
}
