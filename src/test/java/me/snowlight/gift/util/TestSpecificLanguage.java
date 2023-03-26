package me.snowlight.gift.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.snowlight.gift.common.response.CommonResponse;
import me.snowlight.gift.domain.gift.order.ItemDto;
import me.snowlight.gift.domain.gift.order.ItemInfo;
import me.snowlight.gift.domain.gift.order.PartnerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TestSpecificLanguage {
    public static final String ITEM_REQUEST_URL = "http://localhost:8080/api/v1/items";
    public static final String PARTNER_REQUEST_URL = "http://localhost:8080/api/v1/partners";

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;


    public ItemInfo.Main requestRetrieveItem(String itemToken) throws JsonProcessingException {
        ResponseEntity<String> itemRetrieveRequest = restTemplate.getForEntity(ITEM_REQUEST_URL + "/" + itemToken, String.class);

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
        ResponseEntity<String> itemRegisterRequest = restTemplate.exchange(ITEM_REQUEST_URL,
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
        ResponseEntity<String> partnerRegisterResponseEntity = restTemplate.exchange(PARTNER_REQUEST_URL,
                HttpMethod.POST,
                partnerEntity,
                String.class);
        CommonResponse<Map<String, String>> partnerResponse = objectMapper.readValue(partnerRegisterResponseEntity.getBody(), CommonResponse.class);
        return partnerResponse.getData().get("partnerToken");
    }
}
