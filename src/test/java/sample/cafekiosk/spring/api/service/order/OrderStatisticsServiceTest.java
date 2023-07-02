package sample.cafekiosk.spring.api.service.order;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.IntegrationTestSupport;
import sample.cafekiosk.spring.client.mail.MailSendClient;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class OrderStatisticsServiceTest extends IntegrationTestSupport {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private OrderStatisticsService orderStatisticsService;

    @Autowired
    private MailSendHistoryRepository mailSendHistoryRepository;

    @MockBean
    private MailSendClient mailSendClient;

    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        mailSendHistoryRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("결제완료 주문들을 조회하여 매출 통계 메일을 전송한다.")
    void sendOrderStatisticsMail() {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 3, 5, 00, 0);

        Product product1 = createProduct(Product.ProductType.HANDMADE, "001", 1000);
        Product product2 = createProduct(Product.ProductType.HANDMADE, "002", 3000);
        Product product3 = createProduct(Product.ProductType.HANDMADE, "003", 5000);
        List<Product> products = List.of(product1, product2, product3);
        productRepository.saveAll(List.of(product1, product2, product3));

        Order order1 = createPaymentCompletedOrder(LocalDateTime.of(2023,3,4,23,59,59), products);
        Order order2 = createPaymentCompletedOrder(now, products);
        Order order3 = createPaymentCompletedOrder(LocalDateTime.of(2023,3,5,23,59,59), products);
        Order order4 = createPaymentCompletedOrder(LocalDateTime.of(2023,3,6,0,0), products);

        // stubbing
        Mockito.when(mailSendClient.sendEmail(any(String.class), any(String.class), any(String.class), any(String.class)))
                .thenReturn(true);

        // when
        boolean result = orderStatisticsService.sendOrderStatisticsMail(LocalDate.of(2023, 3, 5), "test@test.com");

        // then
        assertThat(result).isTrue();

        List<MailSendHistory> histories = mailSendHistoryRepository.findAll();
        assertThat(histories).hasSize(1)
                .extracting("content")
                .contains("총 매출 합계는 12000원입니다.");
    }

    private Order createPaymentCompletedOrder(LocalDateTime now, List<Product> products) {
        Order order = Order.builder()
                .products(products)
                .orderStatus(Order.OrderStatus.PAYMENT_COMPLETED)
                .registeredDateTime(now)
                .build();
        return orderRepository.save(order);
    }

    private Product createProduct(Product.ProductType type, String productNumber, int price) {
        return Product.builder()
                .type(type)
                .productNumber(productNumber)
                .price(price)
                .sellingStatus(Product.ProductSellingStatus.SELLING)
                .name("메뉴 이름")
                .build();
    }
}