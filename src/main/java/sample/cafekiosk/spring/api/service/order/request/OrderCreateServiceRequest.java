package sample.cafekiosk.spring.api.service.order.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderCreateServiceRequest {

    private List<String> productNumber;

    @Builder
    private OrderCreateServiceRequest(List<String> productNumber) {
        this.productNumber = productNumber;
    }
}
