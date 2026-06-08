package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.specification;

import az.kon.academy.aggragate.valueobject.RowStatus;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductSpecificationRoot;
import az.kon.academy.catalog.command.service.domain.core.command.specification.ProductSpecificationAssignCategoryCommand;
import az.kon.academy.catalog.command.service.domain.core.command.specification.ProductSpecificationChangeInformationCommand;
import az.kon.academy.catalog.command.service.domain.core.command.specification.ProductSpecificationCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.command.specification.ProductSpecificationDeleteCommand;
import az.kon.academy.catalog.command.service.domain.core.command.specification.ProductSpecificationRemoveCategoryAssignmentCommand;
import az.kon.academy.catalog.command.service.domain.core.exception.category.ProductCategoryDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.category.ProductCategoryDomainException;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductCategoryQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductSpecificationQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.ProductSpecificationId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.SpecificationDescription;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.SpecificationName;
import az.kon.academy.domain.core.SeDomainContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductSpecificationModerationDomainServiceImpl")
class ProductSpecificationModerationDomainServiceImplTest {

    @Mock
    private SeDomainContext context;

    @Mock
    private ProductSpecificationQueryOutboundPort specificationQuery;

    @Mock
    private ProductCategoryQueryOutboundPort categoryQuery;

    private ProductSpecificationModerationDomainServiceImpl service;

    private SpecificationName name;
    private SpecificationDescription description;
    private ProductSpecificationId specificationId;
    private ProductSpecificationCreateCommand createCommand;

    @BeforeEach
    void setUp() {
        service = new ProductSpecificationModerationDomainServiceImpl();

        name = new SpecificationName("Screen Size");
        description = new SpecificationDescription("Display diagonal size in inches");
        specificationId = ProductSpecificationId.from(UUID.randomUUID());

        createCommand = ProductSpecificationCreateCommand.builder()
                .name(name)
                .description(description)
                .build();

        lenient().when(context.getQueryPort(ProductSpecificationQueryOutboundPort.class))
                .thenReturn(specificationQuery);
        lenient().when(context.getQueryPort(ProductCategoryQueryOutboundPort.class))
                .thenReturn(categoryQuery);
    }

    private ProductSpecificationRoot freshSpec() {
        return ProductSpecificationRoot.initialize(createCommand);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // create
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("create()")
    class Create {

        @Test
        @DisplayName("Creates specification via initialize")
        void createsSpecification() {
            var result = service.create(context, createCommand);

            assertThat(result.getRootID()).isNotNull();
            assertThat(result.getName()).isEqualTo(name);
            assertThat(result.getDescription()).isEqualTo(description);
            assertThat(result.getCategories()).isEmpty();
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // changeInformation
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("changeInformation()")
    class ChangeInformation {

        private ProductSpecificationChangeInformationCommand changeCommand;

        @BeforeEach
        void setUp() {
            changeCommand = ProductSpecificationChangeInformationCommand.builder()
                    .productSpecificationId(specificationId)
                    .name(new SpecificationName("RAM Size"))
                    .description(new SpecificationDescription("Random access memory in gigabytes"))
                    .build();
        }

        @Test
        @DisplayName("Fetches specification and delegates to changeInformation")
        void fetchesAndDelegates() {
            var spec = freshSpec();
            when(specificationQuery.fetchById(specificationId)).thenReturn(spec);

            var result = service.changeInformation(context, changeCommand);

            assertThat(result.getName().value()).isEqualTo("RAM Size");
        }

        @Test
        @DisplayName("Throws when specification is not found")
        void throwsWhenNotFound() {
            when(specificationQuery.fetchById(specificationId))
                    .thenThrow(new ProductCategoryDomainException(
                            ProductCategoryDomainErrorCodes.ENTITY_NOT_FOUND,
                            List.of(specificationId.toString())));

            assertThatThrownBy(() -> service.changeInformation(context, changeCommand))
                    .isInstanceOf(ProductCategoryDomainException.class);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // delete
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("delete()")
    class Delete {

        private ProductSpecificationDeleteCommand deleteCommand;

        @BeforeEach
        void setUp() {
            deleteCommand = ProductSpecificationDeleteCommand.builder()
                    .specificationId(specificationId)
                    .build();
        }

        @Test
        @DisplayName("Fetches specification and delegates to delete")
        void fetchesAndDelegates() {
            var spec = freshSpec();
            when(specificationQuery.fetchById(specificationId)).thenReturn(spec);

            var result = service.delete(context, deleteCommand);

            assertThat(result.getRowStatus()).isEqualTo(RowStatus.DELETED);
        }

        @Test
        @DisplayName("Throws when specification is not found")
        void throwsWhenNotFound() {
            when(specificationQuery.fetchById(specificationId))
                    .thenThrow(new ProductCategoryDomainException(
                            ProductCategoryDomainErrorCodes.ENTITY_NOT_FOUND,
                            List.of(specificationId.toString())));

            assertThatThrownBy(() -> service.delete(context, deleteCommand))
                    .isInstanceOf(ProductCategoryDomainException.class);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // assignCategory
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("assignCategory()")
    class AssignCategory {

        private ProductCategoryId categoryId;
        private ProductSpecificationAssignCategoryCommand assignCommand;

        @BeforeEach
        void setUp() {
            categoryId = ProductCategoryId.from(UUID.randomUUID());
            assignCommand = ProductSpecificationAssignCategoryCommand.builder()
                    .productSpecificationId(specificationId)
                    .categoryId(categoryId)
                    .isRequired(true)
                    .build();
        }

        @Test
        @DisplayName("Checks category exists, fetches spec and delegates to assignCategory")
        void checksCategoryExistsFetchesAndDelegates() {
            var spec = freshSpec();
            when(specificationQuery.fetchById(specificationId)).thenReturn(spec);

            var result = service.assignCategory(context, assignCommand);

            assertThat(result.getCategories()).hasSize(1);
            assertThat(result.getCategories().iterator().next().getCategoryId()).isEqualTo(categoryId);
        }

        @Test
        @DisplayName("Throws when category is not found")
        void throwsWhenCategoryNotFound() {
            doThrow(new ProductCategoryDomainException(ProductCategoryDomainErrorCodes.ENTITY_NOT_FOUND,
                    List.of(categoryId.toString())))
                    .when(categoryQuery).checkExitsById(categoryId);

            assertThatThrownBy(() -> service.assignCategory(context, assignCommand))
                    .isInstanceOf(ProductCategoryDomainException.class);
        }

        @Test
        @DisplayName("Throws when specification is not found")
        void throwsWhenSpecificationNotFound() {
            when(specificationQuery.fetchById(specificationId))
                    .thenThrow(new ProductCategoryDomainException(
                            ProductCategoryDomainErrorCodes.ENTITY_NOT_FOUND,
                            List.of(specificationId.toString())));

            assertThatThrownBy(() -> service.assignCategory(context, assignCommand))
                    .isInstanceOf(ProductCategoryDomainException.class);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // removeCategoryAssignment
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("removeCategoryAssignment()")
    class RemoveCategoryAssignment {

        private ProductCategoryId categoryId;
        private ProductSpecificationRemoveCategoryAssignmentCommand removeCommand;

        @BeforeEach
        void setUp() {
            categoryId = ProductCategoryId.from(UUID.randomUUID());
            removeCommand = ProductSpecificationRemoveCategoryAssignmentCommand.builder()
                    .productSpecificationId(specificationId)
                    .categoryId(categoryId)
                    .build();
        }

        @Test
        @DisplayName("Fetches spec and delegates to removeCategoryAssignment")
        void fetchesAndDelegates() {
            var assignCmd = ProductSpecificationAssignCategoryCommand.builder()
                    .productSpecificationId(specificationId)
                    .categoryId(categoryId)
                    .isRequired(true)
                    .build();
            var spec = freshSpec().assignCategory(assignCmd);
            when(specificationQuery.fetchById(specificationId)).thenReturn(spec);

            var result = service.removeCategoryAssignment(context, removeCommand);

            assertThat(result.getCategories()).isEmpty();
        }

        @Test
        @DisplayName("Throws when specification is not found")
        void throwsWhenSpecificationNotFound() {
            when(specificationQuery.fetchById(specificationId))
                    .thenThrow(new ProductCategoryDomainException(
                            ProductCategoryDomainErrorCodes.ENTITY_NOT_FOUND,
                            List.of(specificationId.toString())));

            assertThatThrownBy(() -> service.removeCategoryAssignment(context, removeCommand))
                    .isInstanceOf(ProductCategoryDomainException.class);
        }
    }
}
