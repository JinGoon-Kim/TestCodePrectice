package sample.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@ActiveProfiles("test")
//@SpringBootTest
@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("원하는 판매상태를 가진 상품을 조회한다.")
    @Test
    void findAllBySellingStatusIn() {
        // given
        Product product1 = createProduct("001", Product.ProductType.HANDMADE, Product.ProductSellingStatus.SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", Product.ProductType.HANDMADE, Product.ProductSellingStatus.HOLD, "카페라떼", 4500);
        Product product3 = createProduct("003", Product.ProductType.HANDMADE, Product.ProductSellingStatus.STOP_SELLING, "팥빙수", 7000);
        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        List<Product> products = productRepository.findAllBySellingStatusIn(List.of(Product.ProductSellingStatus.SELLING, Product.ProductSellingStatus.HOLD));

        // then
        assertThat(products).hasSize(2)
            .extracting("productNumber", "name", "sellingStatus")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", Product.ProductSellingStatus.SELLING),
                        tuple("002", "카페라떼", Product.ProductSellingStatus.HOLD)
                );
    }

    @DisplayName("상품번호 리스트로 상품들을 조회한다.")
    @Test
    void findAllByProductNumberIn() {
        // given
        Product product1 = createProduct("001", Product.ProductType.HANDMADE, Product.ProductSellingStatus.SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", Product.ProductType.HANDMADE, Product.ProductSellingStatus.HOLD, "카페라떼", 4500);
        Product product3 = createProduct("003", Product.ProductType.HANDMADE, Product.ProductSellingStatus.STOP_SELLING, "팥빙수", 7000);
        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "003"));

        // then
        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "sellingStatus")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", Product.ProductSellingStatus.SELLING),
                        tuple("003", "팥빙수", Product.ProductSellingStatus.STOP_SELLING)
                );
    }

    @Test
    @DisplayName("가장 마지막으로 저장한 상품의 상품번호를 읽어온다.")
    void findLatestProduct() {
        // given
        String targetProductNumber = "003";

        Product product1 = createProduct("001", Product.ProductType.HANDMADE, Product.ProductSellingStatus.SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", Product.ProductType.HANDMADE, Product.ProductSellingStatus.HOLD, "카페라떼", 4500);
        Product product3 = createProduct(targetProductNumber, Product.ProductType.HANDMADE, Product.ProductSellingStatus.STOP_SELLING, "팥빙수", 7000);
        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        String latestProductNumber = productRepository.findLatestProductNumber();

        // then
        assertThat(latestProductNumber).isEqualTo(targetProductNumber);
    }

    @Test
    @DisplayName("가장 마지막으로 저장한 상품의 상품번호를 읽어올 때, 상품이 하나도 없는 경우에는 null을 반환한다.")
    void findLatestProductNumberProductIsEmpty() {
        // given
        // when
        String latestProductNumber = productRepository.findLatestProductNumber();

        // then
        assertThat(latestProductNumber).isNull();
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