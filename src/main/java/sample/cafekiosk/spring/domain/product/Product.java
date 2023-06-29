package sample.cafekiosk.spring.domain.product;

import lombok.*;
import sample.cafekiosk.spring.domain.BaseEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productNumber;

    @Enumerated(EnumType.STRING)
    private ProductType type;

    @Enumerated(EnumType.STRING)
    private ProductSellingStatus sellingStatus;

    private String name;

    private int price;

    @Builder
    private Product(Long id, String productNumber, ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
        this.productNumber = productNumber;
        this.type = type;
        this.sellingStatus = sellingStatus;
        this.name = name;
        this.price = price;
    }


    @Getter
    @RequiredArgsConstructor
    public enum ProductType {

        HANDMADE("제조 음료")
        , BOTTLE("병 음료")
        , BAKERY("베이커리")
        ;

        private final String description;

        public static boolean containsStockType(ProductType type) {
            return List.of(ProductType.BOTTLE, ProductType.BAKERY).contains(type);
        }
    }

    @Getter
    @RequiredArgsConstructor
    public enum ProductSellingStatus {
        SELLING("판매중")
        , HOLD("판매보류")
        , STOP_SELLING("판매중지")
        ;
        private final String description;

        public static List<ProductSellingStatus> forDisplay() {
            return List.of(SELLING, HOLD);
        }
    }
}
