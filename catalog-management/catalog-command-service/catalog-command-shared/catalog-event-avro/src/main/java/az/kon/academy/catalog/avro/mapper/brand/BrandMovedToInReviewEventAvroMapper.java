package az.kon.academy.catalog.avro.mapper.brand;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.brand.BrandMovedToInReviewAvroModel;
import az.kon.academy.catalog.event.brand.BrandMovedToInReviewEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface BrandMovedToInReviewEventAvroMapper {

    BrandMovedToInReviewEventAvroMapper INSTANCE = Mappers.getMapper(BrandMovedToInReviewEventAvroMapper.class);

    BrandMovedToInReviewAvroModel toAvro(BrandMovedToInReviewEvent event);

    BrandMovedToInReviewEvent toEvent(BrandMovedToInReviewAvroModel avroModel);
}
