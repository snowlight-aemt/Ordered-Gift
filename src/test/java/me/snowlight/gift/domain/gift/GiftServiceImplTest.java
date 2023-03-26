package me.snowlight.gift.domain.gift;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.snowlight.gift.common.response.CommonResponse;
import me.snowlight.gift.common.util.TokenGenerator;
import me.snowlight.gift.domain.gift.order.ItemDto;
import me.snowlight.gift.domain.gift.order.PartnerDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GiftServiceImplTest {
    public static final String ITEM_REQUEST_URL = "http://localhost:8080/api/v1/items";
    public static final String PARTNER_REQUEST_URL = "http://localhost:8080/api/v1/partners";
    @Autowired
    private GiftService giftService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("상품 조회")
    @Test
    void test() throws JsonProcessingException {
        String partnerToken = requestRegisterPartner();
        String itemToken = requestRegisterItem(partnerToken);
        requestRetrieveItem(itemToken);
    }

    private void requestRetrieveItem(String itemToken) throws JsonProcessingException {
        ResponseEntity<String> itemRetrieveRequest = restTemplate.getForEntity(ITEM_REQUEST_URL + "/" + itemToken, String.class);
        CommonResponse<Map<String, String>> itemRetrieveResponse = objectMapper.readValue(itemRetrieveRequest.getBody(), CommonResponse.class);


    }

    private String requestRegisterItem(String partnerToken) throws JsonProcessingException {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        ItemDto.ItemTest item = new ItemDto.ItemTest();
        item.setPartnerToken(partnerToken);
        HttpEntity<String> itemEntity = new HttpEntity<>(this.objectMapper.writeValueAsString(item), header);
        ResponseEntity<String> itemRegisterRequest = restTemplate.exchange(ITEM_REQUEST_URL,
                                                                                HttpMethod.POST,
                                                                                itemEntity,
                                                                                String.class);
        CommonResponse<Map<String, String>> itemRegisterResponse = objectMapper.readValue(itemRegisterRequest.getBody(),
                                                                                CommonResponse.class);
        return itemRegisterResponse.getData().get("itemToken");
    }

    private String requestRegisterPartner() throws JsonProcessingException {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        PartnerDto.RegisterPartner partner = new PartnerDto.RegisterPartner();
        HttpEntity<String> partnerEntity = new HttpEntity<>(this.objectMapper.writeValueAsString(partner), header);
        ResponseEntity<String> partnerRegisterResponseEntity = restTemplate.exchange(PARTNER_REQUEST_URL,
                                                                                HttpMethod.POST,
                                                                                partnerEntity,
                                                                                String.class);
        CommonResponse<Map<String, String>> partnerResponse = objectMapper.readValue(partnerRegisterResponseEntity.getBody(), CommonResponse.class);
        return partnerResponse.getData().get("partnerToken");
    }

    @DisplayName("선물 등록")
    @Test
    void sut_register_gift() {
        String orderToken = TokenGenerator.randomCharacterWithPrefix(Gift.GIFT_PREFIX);
        Long userId = 1234L;
        String receiverName = "name";
        String receiverPhone = "000-000-0000";
        Gift.PushType pushType = Gift.PushType.KAKAO;
        String giftMessage = "message";

        GiftCommand.RegisterOrder command = GiftCommand.RegisterOrder.builder()
                .buyerUserId(userId)
                .giftReceiverName(receiverName)
                .giftReceiverPhone(receiverPhone)
                .giftMessage(giftMessage)
                .pushType(pushType)
                .build();

        GiftInfo.Main sut = giftService.registerOrder(command);

        assertThat(sut.getGiftReceiverName()).isEqualTo(receiverName);
        assertThat(sut.getGiftReceiverPhone()).isEqualTo(receiverPhone);
        assertThat(sut.getGiftMessage()).isEqualTo(giftMessage);
        assertThat(sut.getPushType()).isEqualTo(pushType);
        assertThat(sut.getOrderToken()).isEqualTo(orderToken);
    }
}