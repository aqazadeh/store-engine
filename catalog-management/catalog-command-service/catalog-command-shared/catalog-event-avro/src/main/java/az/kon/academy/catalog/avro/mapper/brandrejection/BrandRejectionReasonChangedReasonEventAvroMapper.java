package az.kon.academy.catalog.avro.mapper.brandrejection;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.brandrejection.BrandRejectionReasonChangedReasonAvroModel;
import az.kon.academy.catalog.event.brandrejection.BrandRejectionReasonChangedReasonEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface BrandRejectionReasonChangedReasonEventAvroMapper {

    BrandRejectionReasonChangedReasonEventAvroMapper INSTANCE = Mappers.getMapper(BrandRejectionReasonChangedReasonEventAvroMapper.class);

    BrandRejectionReasonChangedReasonAvroModel toAvro(BrandRejectionReasonChangedReasonEvent event);

    BrandRejectionReasonChangedReasonEvent toEvent(BrandRejectionReasonChangedReasonAvroModel avroModel);
}
