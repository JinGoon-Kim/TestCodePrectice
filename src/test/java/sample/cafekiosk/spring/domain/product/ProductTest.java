package sample.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class ProductTest {

    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    @Test
    void containsStockType() {
        // given
        Product.ProductType givenType = Product.ProductType.HANDMADE;

        // when
        boolean result = Product.ProductType.containsStockType(givenType);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    @Test
    void containsStockType2() {
        // given
        Product.ProductType givenType = Product.ProductType.BAKERY;

        // when
        boolean result = Product.ProductType.containsStockType(givenType);

        // then
        assertThat(result).isTrue();
    }

}