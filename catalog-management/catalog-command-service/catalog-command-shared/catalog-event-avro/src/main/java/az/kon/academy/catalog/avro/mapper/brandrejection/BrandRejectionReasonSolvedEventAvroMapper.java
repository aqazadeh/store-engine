package az.kon.academy.catalog.avro.mapper.brandrejection;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.brandrejection.BrandRejectionReasonSolvedAvroModel;
import az.kon.academy.catalog.event.brandrejection.BrandRejectionReasonSolvedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface BrandRejectionReasonSolvedEventAvroMapper {

    BrandRejectionReasonSolvedEventAvroMapper INSTANCE = Mappers.getMapper(BrandRejectionReasonSolvedEventAvroMapper.class);

    BrandRejectionReasonSolvedAvroModel toAvro(BrandRejectionReasonSolvedEvent event);

    BrandRejectionReasonSolvedEvent toEvent(BrandRejectionReasonSolvedAvroModel avroModel);
}
