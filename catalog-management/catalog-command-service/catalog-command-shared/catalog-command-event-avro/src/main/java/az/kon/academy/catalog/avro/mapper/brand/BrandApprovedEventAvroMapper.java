package az.kon.academy.catalog.avro.mapper.brand;

import az.kon.academy.catalog.avro.mapper.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.brand.BrandApprovedAvroModel;
import az.kon.academy.catalog.event.brand.BrandApprovedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(config = AvroMapStructConfig.class)
public interface BrandApprovedEventAvroMapper {
    BrandApprovedEventAvroMapper INSTANCE = Mappers.getMapper(BrandApprovedEventAvroMapper.class);

    BrandApprovedAvroModel toAvro(BrandApprovedEvent event);

    BrandApprovedEvent toEvent(BrandApprovedAvroModel avroModel);
}
