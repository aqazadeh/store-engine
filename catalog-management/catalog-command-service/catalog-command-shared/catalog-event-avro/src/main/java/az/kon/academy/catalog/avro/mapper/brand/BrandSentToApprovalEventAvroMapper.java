package az.kon.academy.catalog.avro.mapper.brand;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.brand.BrandSentToApprovalAvroModel;
import az.kon.academy.catalog.event.brand.BrandSentToApprovalEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface BrandSentToApprovalEventAvroMapper {

    BrandSentToApprovalEventAvroMapper INSTANCE = Mappers.getMapper(BrandSentToApprovalEventAvroMapper.class);

    BrandSentToApprovalAvroModel toAvro(BrandSentToApprovalEvent event);

    BrandSentToApprovalEvent toEvent(BrandSentToApprovalAvroModel avroModel);
}
