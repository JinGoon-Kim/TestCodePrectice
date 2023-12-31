package sample.cafekiosk.spring.api.service.product;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.IntegrationTestSupport;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
class ProductServiceTest extends IntegrationTestSupport {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAllInBatch();
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("beforeAll");
    }

    @BeforeEach
    void setUp() {
        System.out.println("before method");

        // 각 테스트 입장에서 봤을 때 : 아예 몰라도 테스트 내용을 이해하는 데에 문제가 없는가?
        // 수정해도 모든 테스트에 영향을 주지 않는가?
    }

    @Test
    @DisplayName("신규 상품을 등록한다. 상품번호는 가장 최근 상품의 상품번호에서 1증가한 값이다.")
    void createProduct() {
        // given
        Product product = createProduct("001", Product.ProductType.HANDMADE, Product.ProductSellingStatus.SELLING, "아메리카노", 4000);

        productRepository.save(product);

        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(Product.ProductType.HANDMADE)
                .sellingStatus(Product.ProductSellingStatus.SELLING)
                .name("카푸치노")
                .price(5000)
                .build();

        // when
        ProductResponse productResponse = productService.createProduct(request.toServiceRequest());

        // then
        assertThat(productResponse)
                .extracting("productNumber", "type", "sellingStatus", "name", "price")
                .contains("002", Product.ProductType.HANDMADE, Product.ProductSellingStatus.SELLING, "카푸치노", 5000);

        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(2)
                .extracting("productNumber", "type", "sellingStatus", "name", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", Product.ProductType.HANDMADE, Product.ProductSellingStatus.SELLING, "아메리카노", 4000)
                        , tuple("002", Product.ProductType.HANDMADE, Product.ProductSellingStatus.SELLING, "카푸치노", 5000)
                );
    }

    @Test
    @DisplayName("신규 상품을 등록할때 상품이 하나도 없는 경우 상품번호는 1이다.")
    void createProductWhenProductsIsEmpty() {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(Product.ProductType.HANDMADE)
                .sellingStatus(Product.ProductSellingStatus.SELLING)
                .name("카푸치노")
                .price(5000)
                .build();

        // when
        ProductResponse productResponse = productService.createProduct(request.toServiceRequest());

        // then
        assertThat(productResponse)
                .extracting("productNumber", "type", "sellingStatus", "name", "price")
                .contains("001", Product.ProductType.HANDMADE, Product.ProductSellingStatus.SELLING, "카푸치노", 5000);

        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(1)
                .extracting("productNumber", "type", "sellingStatus", "name", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", Product.ProductType.HANDMADE, Product.ProductSellingStatus.SELLING, "카푸치노", 5000)
                );
    }

    private Product createProduct(String productNumber, Product.ProductType type, Product.ProductSellingStatus sellingStatus, String name, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(type)
                .sellingStatus(sellingStatus)
                .name(name)
                .price(price)
                .build();
    }
}