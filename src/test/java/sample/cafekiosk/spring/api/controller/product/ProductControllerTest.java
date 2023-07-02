package sample.cafekiosk.spring.api.controller.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import sample.cafekiosk.spring.ControllerTestSupport;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.ProductService;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class ProductControllerTest extends ControllerTestSupport {

    @Test
    @DisplayName("신규 상품을 등록한다.")
    void createProduct() {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(Product.ProductType.HANDMADE)
                .sellingStatus(Product.ProductSellingStatus.SELLING)
                .name("아메리카노")
                .price(4000)
                .build();

        // when // then
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new")
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("신규 상품을 등록할 때 상품 타입은 필수값이다.")
    void createProductWithoutType() {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
//                .type(Product.ProductType.HANDMADE)
                .sellingStatus(Product.ProductSellingStatus.SELLING)
                .name("아메리카노")
                .price(4000)
                .build();

        // when // then
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new")
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.message").value("상품 타입은 필수입니다."))
                    .andExpect(jsonPath("$.data").isEmpty())
            ;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("판매 상품을 조회한다")
    void getSellingProducts() {
        // given
        List<ProductResponse> result = List.of();
        when(productService.getSellingProducts()).thenReturn(result);

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/selling")
                    )
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value("OK"))
                    .andExpect(jsonPath("$.data").isArray())
            ;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}