package me.snowlight.gift.interfaces.api.gift;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BuilderArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;
import me.snowlight.gift.application.gift.GiftFacade;
import me.snowlight.gift.common.response.CommonResponse;
import me.snowlight.gift.domain.gift.Gift;
import me.snowlight.gift.domain.gift.GiftReader;
import me.snowlight.gift.domain.gift.order.ItemInfo;
import me.snowlight.gift.domain.gift.order.OrderDto;
import me.snowlight.gift.util.TestSpecificLanguage;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.with;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class GiftApiControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private TestSpecificLanguage testSpecificLanguage;
    @Autowired
    private ItemInfoMapper itemInfoMapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private GiftFacade giftFacade;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private GiftReader giftReader;

    private final static FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
            .objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
            .objectIntrospector(BuilderArbitraryIntrospector.INSTANCE)
            .plugin(new JakartaValidationPlugin())
            .build();

    @DisplayName("선물 주문 상태를 결재 중으로 변경")
    @Test
    void paymentProcessing() throws Exception {
        String partnerToken = testSpecificLanguage.requestRegisterPartner();
        String itemToken = testSpecificLanguage.requestRegisterItem(partnerToken);
        testSpecificLanguage.requestChangedItemStatusOnSales(itemToken);

        ItemInfo.Main itemInfo = testSpecificLanguage.requestRetrieveItem(itemToken);
        GiftDto.RegisterGift registerGift = convertToRegisterGift(itemInfo);

        CommonResponse<GiftDto.RegisterResponse> response = testSpecificLanguage.requestRegisterGiftOrder(registerGift);

        // Act
        String giftToken = response.getData().getGiftToken();
        String paymentProcessingURL = "/api/v1/gifts/" + giftToken + "/payment-processing";
        mvc.perform(post(paymentProcessingURL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("SUCCESS"))
                .andExpect(jsonPath("$.data").value("OK"));
    }

    @DisplayName("선물 주문 수락 ")
    @Test
    void accept() throws Exception {
        // 파트너 등록
        String partnerToken = testSpecificLanguage.requestRegisterPartner();

        // 상품 등록
        String itemToken = testSpecificLanguage.requestRegisterItem(partnerToken);
        testSpecificLanguage.requestChangedItemStatusOnSales(itemToken);

        // 주문 등록
        ItemInfo.Main itemInfo = testSpecificLanguage.requestRetrieveItem(itemToken);
        GiftDto.RegisterGift registerGift = convertToRegisterGift(itemInfo);
        CommonResponse<GiftDto.RegisterResponse> response = testSpecificLanguage.requestRegisterGiftOrder(registerGift);
        String giftToken = response.getData().getGiftToken();
        String orderToken = response.getData().getOrderToken();

        // 결제 중 상태 변경
        testSpecificLanguage.requestPaymentProcessing(giftToken);

        // 결제 진행 (PG 응답 처리)
        CommonResponse<OrderDto.Main> responseOrderMain = testSpecificLanguage.requestRetrieveOrder(orderToken);
        testSpecificLanguage.requestPaymentOrder(responseOrderMain.getData().getOrderToken(),
                                                responseOrderMain.getData().getTotalAmount(),
                                                responseOrderMain.getData().getPayMethod());

        with().conditionEvaluationListener(condition -> System.out.println())
                .await()
                .atMost(1_000, TimeUnit.MILLISECONDS)
                .pollInterval(Duration.ofMillis(100))
                .until(() -> giftReader.getGiftByGiftToken(giftToken).getStatus() == Gift.Status.ORDER_COMPLETE);


        // Act
        String paymentProcessingURL = "/api/v1/gifts/" + giftToken + "/accept-gift";
        GiftDto.AcceptGiftReq acceptGiftReq = this.fixtureMonkey.giveMeOne(GiftDto.AcceptGiftReq.class);
        mvc.perform(post(paymentProcessingURL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(acceptGiftReq)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("SUCCESS"))
                .andExpect(jsonPath("$.data").value("OK"));
    }

    @DisplayName("선물 주문 조회")
    @Test
    void retrieveOrder() throws Exception {
        String partnerToken = testSpecificLanguage.requestRegisterPartner();
        String itemToken = testSpecificLanguage.requestRegisterItem(partnerToken);
        testSpecificLanguage.requestChangedItemStatusOnSales(itemToken);

        ItemInfo.Main itemInfo = testSpecificLanguage.requestRetrieveItem(itemToken);
        GiftDto.RegisterGift registerGift = convertToRegisterGift(itemInfo);

        CommonResponse<GiftDto.RegisterResponse> response = testSpecificLanguage.requestRegisterGiftOrder(registerGift);

        // ACT
        String giftToken = response.getData().getGiftToken();
        String paymentProcessingURL = "/api/v1/gifts/" + giftToken;
        mvc.perform(get(paymentProcessingURL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("SUCCESS"))
                .andExpect(jsonPath("$.data.orderToken").value(response.getData().getOrderToken()))
                .andExpect(jsonPath("$.data.giftToken").value(response.getData().getGiftToken()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errorCode").isEmpty());

    }

    private GiftDto.RegisterGift convertToRegisterGift(ItemInfo.Main itemInfo) {
        GiftDto.RegisterOrderItem orderItem = fixtureMonkey
                .giveMeBuilder(this.itemInfoMapper.of(itemInfo))
                .set("orderCount", Arbitraries.integers().between(1, 127))
                .sample();
        GiftDto.RegisterGift registerGift = GiftDto.RegisterGift.builder()
                .buyerUserId(11L)
                .giftReceiverName("name")
                .giftReceiverPhone("000-0000-0000")
                .giftMessage("message")
                .pushType(Gift.PushType.KAKAO)
                .payMethod("KAKAO_PAY")
                .orderItems(List.of(orderItem))
                .build();
        return registerGift;
    }
}