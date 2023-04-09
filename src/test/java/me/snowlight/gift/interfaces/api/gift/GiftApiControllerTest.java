package me.snowlight.gift.interfaces.api.gift;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BuilderArbitraryIntrospector;
import com.navercorp.fixturemonkey.javax.validation.plugin.JavaxValidationPlugin;
import me.snowlight.gift.common.response.CommonResponse;
import me.snowlight.gift.domain.gift.Gift;
import me.snowlight.gift.domain.gift.order.ItemInfo;
import me.snowlight.gift.util.TestSpecificLanguage;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

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
    private TestRestTemplate restTemplate;
    @Autowired
    private ItemInfoMapper itemInfoMapper;
    @Autowired
    private ObjectMapper objectMapper;

    private final static FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
            .objectIntrospector(BuilderArbitraryIntrospector.INSTANCE)
            .plugin(new JavaxValidationPlugin())
            .build();

    @DisplayName("선물 주문 상태를 결재 중으로 변경")
    @Test
    void paymentProcessing() throws Exception {
        String partnerToken = testSpecificLanguage.requestRegisterPartner();
        String itemToken = testSpecificLanguage.requestRegisterItem(partnerToken);
        testSpecificLanguage.requestChangedItemStatusOnSales(itemToken);

        ItemInfo.Main itemInfo = testSpecificLanguage.requestRetrieveItem(itemToken);
        GiftDto.RegisterGift registerGift = getRegisterGift(itemInfo);

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

    @DisplayName("선물 주문 조회")
    @Test
    void retrieveOrder() throws Exception {
        String partnerToken = testSpecificLanguage.requestRegisterPartner();
        String itemToken = testSpecificLanguage.requestRegisterItem(partnerToken);
        testSpecificLanguage.requestChangedItemStatusOnSales(itemToken);

        ItemInfo.Main itemInfo = testSpecificLanguage.requestRetrieveItem(itemToken);
        GiftDto.RegisterGift registerGift = getRegisterGift(itemInfo);

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

    private GiftDto.RegisterGift getRegisterGift(ItemInfo.Main itemInfo) {
        GiftDto.RegisterOrderItem orderItem = fixtureMonkey
                .giveMeBuilder(this.itemInfoMapper.of(itemInfo))
                .set("orderCount", Arbitraries.integers().between(1, 250))
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