package az.kon.academy.catalog.command.service.application.service.configuration;

import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brand.BrandManagementDomainServiceImpl;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brand.BrandModerationDomainService;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brand.BrandManagementDomainService;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brand.BrandModerationDomainServiceImpl;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brandrejection.BrandRejectionManagementService;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brandrejection.BrandRejectionManagementServiceImpl;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brandrejection.BrandRejectionModerationDomainService;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brandrejection.BrandRejectionModerationDomainServiceImpl;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.category.ProductCategoryModerationDomainService;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.category.ProductCategoryModerationDomainServiceImpl;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.ProductDomainService;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.ProductDomainServiceImpl;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.specification.ProductSpecificationDomainService;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.specification.ProductSpecificationDomainServiceImpl;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.variant.VariantDomainService;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.variant.VariantDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainServiceRegistry {

    @Bean
    public BrandModerationDomainService brandModerationDomainService() {
        return new BrandModerationDomainServiceImpl();
    }

    @Bean
    public BrandManagementDomainService brandManagementDomainService() {
        return new BrandManagementDomainServiceImpl();
    }

    @Bean
    public BrandRejectionModerationDomainService brandRejectionModerationDomainService() {
        return new BrandRejectionModerationDomainServiceImpl();
    }

    @Bean
    public BrandRejectionManagementService brandRejectionManagementService(){
        return new BrandRejectionManagementServiceImpl();
    }

    @Bean
    public ProductCategoryModerationDomainService productCategoryDomainService() {
        return new ProductCategoryModerationDomainServiceImpl();
    }

    @Bean
    public ProductSpecificationDomainService productSpecificationDomainService() {
        return new ProductSpecificationDomainServiceImpl();
    }

    @Bean
    public ProductDomainService productDomainService() {
        return new ProductDomainServiceImpl();
    }

    @Bean
    public VariantDomainService variantDomainService() {
        return new VariantDomainServiceImpl();
    }

}
