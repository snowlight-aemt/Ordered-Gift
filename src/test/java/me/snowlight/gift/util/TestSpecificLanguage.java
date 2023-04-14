package me.snowlight.gift.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.snowlight.gift.common.response.CommonResponse;
import me.snowlight.gift.domain.gift.order.ItemDto;
import me.snowlight.gift.domain.gift.order.ItemInfo;
import me.snowlight.gift.domain.gift.order.PartnerDto;
import me.snowlight.gift.interfaces.api.gift.GiftDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TestSpecificLanguage {
    @Value("${gift.order.base-url}")
    public String orderUrl;
    public static final String ITEM_REQUEST_URL = "/api/v1/items";
    public static final String PARTNER_REQUEST_URL = "/api/v1/partners";

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;


    public ItemInfo.Main requestRetrieveItem(String itemToken) throws JsonProcessingException {
        ResponseEntity<String> itemRetrieveRequest = restTemplate.getForEntity(orderUrl + ITEM_REQUEST_URL + "/" + itemToken, String.class);

        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(CommonResponse.class, ItemInfo.Main.class);
        CommonResponse<ItemInfo.Main> itemRetrieveResponse = objectMapper.readValue(itemRetrieveRequest.getBody(), javaType);

        return itemRetrieveResponse.getData();
    }

    public String requestRegisterItem(String partnerToken) throws JsonProcessingException {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        ItemDto.Item item = new ItemDto.Item();

        item.setPartnerToken(partnerToken);
        HttpEntity<String> itemEntity = new HttpEntity<>(this.objectMapper.writeValueAsString(item), header);
        ResponseEntity<String> itemRegisterRequest = restTemplate.exchange(orderUrl + ITEM_REQUEST_URL,
                HttpMethod.POST,
                itemEntity,
                String.class);
        CommonResponse<Map<String, String>> itemRegisterResponse = objectMapper.readValue(itemRegisterRequest.getBody(),
                CommonResponse.class);
        return itemRegisterResponse.getData().get("itemToken");
    }

    public String requestRegisterPartner() throws JsonProcessingException {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        PartnerDto.RegisterPartner partner = new PartnerDto.RegisterPartner();
        HttpEntity<String> partnerEntity = new HttpEntity<>(this.objectMapper.writeValueAsString(partner), header);
        ResponseEntity<String> partnerRegisterResponseEntity = restTemplate.exchange(orderUrl + PARTNER_REQUEST_URL,
                HttpMethod.POST,
                partnerEntity,
                String.class);
        CommonResponse<Map<String, String>> partnerResponse = objectMapper.readValue(partnerRegisterResponseEntity.getBody(), CommonResponse.class);
        return partnerResponse.getData().get("partnerToken");
    }

    public void requestChangedItemStatusOnSales(String itemToken) throws JsonProcessingException {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        ItemDto.ChangeStatusItemRequest statusItemRequest = new ItemDto.ChangeStatusItemRequest();
        statusItemRequest.setItemToken(itemToken);

        HttpEntity<String> partnerEntity = new HttpEntity<>(this.objectMapper.writeValueAsString(statusItemRequest), header);

        restTemplate.exchange(orderUrl + ITEM_REQUEST_URL + "/change-on-sales", HttpMethod.POST, partnerEntity, String.class);
    }

    public CommonResponse<GiftDto.RegisterResponse> requestRegisterGiftOrder(GiftDto.RegisterGift registerGift) throws JsonProcessingException {
        String registerURL = "/api/v1/gifts";
        ResponseEntity<String> registerResponse = restTemplate.postForEntity(registerURL, registerGift, String.class);
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(
                CommonResponse.class,
                GiftDto.RegisterResponse.class);
        CommonResponse<GiftDto.RegisterResponse> response =
                objectMapper.readValue(registerResponse.getBody(), javaType);
        return response;
    }

    public CommonResponse requestPaymentProcessing(String giftToken) throws JsonProcessingException {
        String registerURL = "/api/v1/gifts/" + giftToken + "/payment-processing";
        ResponseEntity<String> registerResponse = restTemplate.postForEntity(registerURL, null, String.class);
        return objectMapper.readValue(registerResponse.getBody(), CommonResponse.class);
    }
}
