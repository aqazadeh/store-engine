package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.category;

import az.kon.academy.aggragate.valueobject.RowStatus;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductCategoryRoot;
import az.kon.academy.catalog.command.service.domain.core.command.category.ProductCategoryActivateCommand;
import az.kon.academy.catalog.command.service.domain.core.command.category.ProductCategoryArchiveCommand;
import az.kon.academy.catalog.command.service.domain.core.command.category.ProductCategoryChangeImageCommand;
import az.kon.academy.catalog.command.service.domain.core.command.category.ProductCategoryChangeInformationCommand;
import az.kon.academy.catalog.command.service.domain.core.command.category.ProductCategoryChangeParentCommand;
import az.kon.academy.catalog.command.service.domain.core.command.category.ProductCategoryCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.command.category.ProductCategoryDeleteCommand;
import az.kon.academy.catalog.command.service.domain.core.exception.category.ProductCategoryDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.category.ProductCategoryDomainException;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductCategoryQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductSpecificationQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryDescription;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryName;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryPath;
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
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductCategoryModerationDomainServiceImpl")
class ProductCategoryModerationDomainServiceImplTest {

    @Mock
    private SeDomainContext context;

    @Mock
    private ProductCategoryQueryOutboundPort categoryQuery;

    @Mock
    private ProductQueryOutboundPort productQuery;

    @Mock
    private ProductSpecificationQueryOutboundPort specificationQuery;

    private ProductCategoryModerationDomainServiceImpl service;

    private ProductCategoryName name;
    private ProductCategoryPath path;
    private ProductCategoryDescription description;
    private ProductCategoryId categoryId;
    private ProductCategoryCreateCommand createCommand;

    @BeforeEach
    void setUp() {
        service = new ProductCategoryModerationDomainServiceImpl();

        name = new ProductCategoryName("Electronics");
        path = new ProductCategoryPath("electronics");
        description = new ProductCategoryDescription("All kinds of electronic devices");
        categoryId = ProductCategoryId.from(UUID.randomUUID());

        createCommand = ProductCategoryCreateCommand.builder()
                .name(name)
                .path(path)
                .description(description)
                .build();

        lenient().when(context.getQueryPort(ProductCategoryQueryOutboundPort.class))
                .thenReturn(categoryQuery);
        lenient().when(context.getQueryPort(ProductQueryOutboundPort.class))
                .thenReturn(productQuery);
        lenient().when(context.getQueryPort(ProductSpecificationQueryOutboundPort.class))
                .thenReturn(specificationQuery);
    }

    private ProductCategoryRoot freshCategory() {
        return ProductCategoryRoot.initialize(createCommand);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // createCategory
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("createCategory()")
    class CreateCategory {

        @Test
        @DisplayName("Creates category via initialize")
        void createsCategory() {
            var result = service.createCategory(context, createCommand);

            assertThat(result.getRootID()).isNotNull();
            assertThat(result.getName()).isEqualTo(name);
            assertThat(result.getPath()).isEqualTo(path);
            assertThat(result.getDescription()).isEqualTo(description);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // changeInformation
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("changeInformation()")
    class ChangeInformation {

        private ProductCategoryChangeInformationCommand changeCommand;

        @BeforeEach
        void setUp() {
            changeCommand = ProductCategoryChangeInformationCommand.builder()
                    .productCategoryId(categoryId)
                    .name(new ProductCategoryName("Mobile Phones"))
                    .path(new ProductCategoryPath("mobile-phones"))
                    .description(new ProductCategoryDescription("Smartphones and mobile devices"))
                    .build();
        }

        @Test
        @DisplayName("Fetches category and delegates to changeInformation")
        void fetchesAndDelegates() {
            var category = freshCategory();
            when(categoryQuery.fetchById(categoryId)).thenReturn(category);

            var result = service.changeInformation(context, changeCommand);

            assertThat(result.getName().value()).isEqualTo("Mobile Phones");
        }

        @Test
        @DisplayName("Throws when category is not found")
        void throwsWhenNotFound() {
            when(categoryQuery.fetchById(categoryId))
                    .thenThrow(new ProductCategoryDomainException(
                            ProductCategoryDomainErrorCodes.ENTITY_NOT_FOUND,
                            List.of(categoryId.toString())));

            assertThatThrownBy(() -> service.changeInformation(context, changeCommand))
                    .isInstanceOf(ProductCategoryDomainException.class);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // changeImage
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("changeImage()")
    class ChangeImage {

        private ProductCategoryChangeImageCommand imageCommand;

        @BeforeEach
        void setUp() {
            imageCommand = ProductCategoryChangeImageCommand.builder()
                    .productCategoryId(categoryId)
                    .image("https://cdn.example.com/categories/electronics.png")
                    .build();
        }

        @Test
        @DisplayName("Fetches category and delegates to changeImage")
        void fetchesAndDelegates() {
            var category = freshCategory();
            when(categoryQuery.fetchById(categoryId)).thenReturn(category);

            var result = service.changeImage(context, imageCommand);

            assertThat(result.getImage()).isEqualTo("https://cdn.example.com/categories/electronics.png");
        }

        @Test
        @DisplayName("Throws when category is not found")
        void throwsWhenNotFound() {
            when(categoryQuery.fetchById(categoryId))
                    .thenThrow(new ProductCategoryDomainException(
                            ProductCategoryDomainErrorCodes.ENTITY_NOT_FOUND,
                            List.of(categoryId.toString())));

            assertThatThrownBy(() -> service.changeImage(context, imageCommand))
                    .isInstanceOf(ProductCategoryDomainException.class);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // changeParent
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("changeParent()")
    class ChangeParent {

        @Nested
        @DisplayName("When parentId is set")
        class WhenParentIdIsSet {

            private ProductCategoryChangeParentCommand changeParentCommand;
            private ProductCategoryId parentId;

            @BeforeEach
            void setUp() {
                parentId = ProductCategoryId.from(UUID.randomUUID());
                changeParentCommand = ProductCategoryChangeParentCommand.builder()
                        .productCategoryId(categoryId)
                        .parentId(parentId)
                        .build();
            }

            @Test
            @DisplayName("Fetches category, validates parent and delegates to changeParent")
            void fetchesBothAndDelegates() {
                var category = freshCategory();
                when(categoryQuery.fetchById(categoryId)).thenReturn(category);

                var result = service.changeParent(context, changeParentCommand);

                assertThat(result.getParent()).isEqualTo(parentId);
            }

            @Test
            @DisplayName("Throws when category is not found")
            void throwsWhenCategoryNotFound() {
                when(categoryQuery.fetchById(categoryId))
                        .thenThrow(new ProductCategoryDomainException(
                                ProductCategoryDomainErrorCodes.ENTITY_NOT_FOUND,
                                List.of(categoryId.toString())));

                assertThatThrownBy(() -> service.changeParent(context, changeParentCommand))
                        .isInstanceOf(ProductCategoryDomainException.class);
            }
        }

        @Nested
        @DisplayName("When parentId is null")
        class WhenParentIdIsNull {

            private ProductCategoryChangeParentCommand changeParentCommand;

            @BeforeEach
            void setUp() {
                changeParentCommand = ProductCategoryChangeParentCommand.builder()
                        .productCategoryId(categoryId)
                        .parentId(null)
                        .build();
            }

            @Test
            @DisplayName("Removes parent instead")
            void removesParent() {
                var parentId = ProductCategoryId.from(UUID.randomUUID());
                var category = freshCategory().changeParent(parentId);
                when(categoryQuery.fetchById(categoryId)).thenReturn(category);

                var result = service.changeParent(context, changeParentCommand);

                assertThat(result.getParent()).isNull();
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // archive
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("archive()")
    class Archive {

        private ProductCategoryArchiveCommand archiveCommand;

        @BeforeEach
        void setUp() {
            archiveCommand = ProductCategoryArchiveCommand.builder()
                    .productCategoryId(categoryId)
                    .build();
        }

        @Test
        @DisplayName("Fetches category and delegates to archive")
        void fetchesAndDelegates() {
            var category = freshCategory();
            when(categoryQuery.fetchById(categoryId)).thenReturn(category);

            var result = service.archive(context, archiveCommand);

            assertThat(result.getRowStatus()).isEqualTo(RowStatus.ARCHIVED);
        }

        @Test
        @DisplayName("Throws when category is not found")
        void throwsWhenNotFound() {
            when(categoryQuery.fetchById(categoryId))
                    .thenThrow(new ProductCategoryDomainException(
                            ProductCategoryDomainErrorCodes.ENTITY_NOT_FOUND,
                            List.of(categoryId.toString())));

            assertThatThrownBy(() -> service.archive(context, archiveCommand))
                    .isInstanceOf(ProductCategoryDomainException.class);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // activate
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("activate()")
    class Activate {

        private ProductCategoryActivateCommand activateCommand;

        @BeforeEach
        void setUp() {
            activateCommand = ProductCategoryActivateCommand.builder()
                    .productCategoryId(categoryId)
                    .build();
        }

        @Test
        @DisplayName("Fetches category and delegates to activate")
        void fetchesAndDelegates() {
            var category = freshCategory().archive();
            when(categoryQuery.fetchById(categoryId)).thenReturn(category);

            var result = service.activate(context, activateCommand);

            assertThat(result.getRowStatus()).isEqualTo(RowStatus.ACTIVE);
        }

        @Test
        @DisplayName("Throws when category is not found")
        void throwsWhenNotFound() {
            when(categoryQuery.fetchById(categoryId))
                    .thenThrow(new ProductCategoryDomainException(
                            ProductCategoryDomainErrorCodes.ENTITY_NOT_FOUND,
                            List.of(categoryId.toString())));

            assertThatThrownBy(() -> service.activate(context, activateCommand))
                    .isInstanceOf(ProductCategoryDomainException.class);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // delete
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("delete()")
    class Delete {

        private ProductCategoryDeleteCommand deleteCommand;

        @BeforeEach
        void setUp() {
            deleteCommand = ProductCategoryDeleteCommand.builder()
                    .productCategoryId(categoryId)
                    .build();
        }

        @Test
        @DisplayName("Deletes when no active products and no specification assignments")
        void deletesWhenNoActiveProductsOrAssignments() {
            var category = freshCategory();
            when(productQuery.exitsByCategoryId(categoryId)).thenReturn(false);
            when(specificationQuery.existsAssignmentByCategoryId(categoryId)).thenReturn(false);
            when(categoryQuery.fetchById(categoryId)).thenReturn(category);

            var result = service.delete(context, deleteCommand);

            assertThat(result.getRowStatus()).isEqualTo(RowStatus.ARCHIVED);
        }

        @Test
        @DisplayName("Throws when category has active products")
        void throwsWhenHasActiveProducts() {
            when(productQuery.exitsByCategoryId(categoryId)).thenReturn(true);

            assertThatThrownBy(() -> service.delete(context, deleteCommand))
                    .isInstanceOf(ProductCategoryDomainException.class)
                    .matches(ex -> ((ProductCategoryDomainException) ex).getCode()
                            .equals(ProductCategoryDomainErrorCodes.HAS_ACTIVE_PRODUCT));
        }

        @Test
        @DisplayName("Throws when category has active specification assignments")
        void throwsWhenHasActiveSpecificationAssignments() {
            when(productQuery.exitsByCategoryId(categoryId)).thenReturn(false);
            when(specificationQuery.existsAssignmentByCategoryId(categoryId)).thenReturn(true);

            assertThatThrownBy(() -> service.delete(context, deleteCommand))
                    .isInstanceOf(ProductCategoryDomainException.class)
                    .matches(ex -> ((ProductCategoryDomainException) ex).getCode()
                            .equals(ProductCategoryDomainErrorCodes.HAS_ACTIVE_SPECIFICATION_ASSIGNMENT));
        }

        @Test
        @DisplayName("Throws when category is not found")
        void throwsWhenCategoryNotFound() {
            when(productQuery.exitsByCategoryId(categoryId)).thenReturn(false);
            when(specificationQuery.existsAssignmentByCategoryId(categoryId)).thenReturn(false);
            when(categoryQuery.fetchById(categoryId))
                    .thenThrow(new ProductCategoryDomainException(
                            ProductCategoryDomainErrorCodes.ENTITY_NOT_FOUND,
                            List.of(categoryId.toString())));

            assertThatThrownBy(() -> service.delete(context, deleteCommand))
                    .isInstanceOf(ProductCategoryDomainException.class);
        }
    }
}
