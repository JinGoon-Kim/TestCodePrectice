package sample.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

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

    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    @CsvSource({"HANDMADE,false", "BAKERY,true", "BOTTLE,true"})
    @ParameterizedTest
    void getType(Product.ProductType productType, boolean expected) {
        // given
        // when
        boolean result = Product.ProductType.containsStockType(productType);

        // then
        assertThat(result).isEqualTo(expected);
    }

    private static Stream<Arguments> provideProductTypesForCheckingStockType() {
        return Stream.of(
                Arguments.of(Product.ProductType.HANDMADE, false),
                Arguments.of(Product.ProductType.BAKERY, true),
                Arguments.of(Product.ProductType.BOTTLE, true)
        );
    }

    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    @MethodSource("provideProductTypesForCheckingStockType")
    @ParameterizedTest
    void getType2(Product.ProductType productType, boolean expected) {
        // given
        // when
        boolean result = Product.ProductType.containsStockType(productType);

        // then
        assertThat(result).isEqualTo(expected);
    }


}