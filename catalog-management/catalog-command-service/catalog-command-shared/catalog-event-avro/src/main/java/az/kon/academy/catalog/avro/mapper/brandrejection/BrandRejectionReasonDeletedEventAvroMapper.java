package az.kon.academy.catalog.avro.mapper.brandrejection;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.brandrejection.BrandRejectionReasonDeletedAvroModel;
import az.kon.academy.catalog.event.brandrejection.BrandRejectionReasonDeletedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface BrandRejectionReasonDeletedEventAvroMapper {

    BrandRejectionReasonDeletedEventAvroMapper INSTANCE = Mappers.getMapper(BrandRejectionReasonDeletedEventAvroMapper.class);

    BrandRejectionReasonDeletedAvroModel toAvro(BrandRejectionReasonDeletedEvent event);

    BrandRejectionReasonDeletedEvent toEvent(BrandRejectionReasonDeletedAvroModel avroModel);
}
