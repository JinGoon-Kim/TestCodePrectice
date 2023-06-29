package sample.cafekiosk.spring.api.service.product;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ProductServiceTest {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAllInBatch();
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