package me.snowlight.gift.interfaces.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.snowlight.gift.application.gift.GiftFacade;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GiftSqsMessageListener {
    private final GiftFacade giftFacade;
    private final ObjectMapper objectMapper;

    @SqsListener(value = "order-payComplete.fifo")
    public void readMessage(String message) {
        try {
            GiftPaymentCompleteMessage payload = objectMapper.readValue(message, GiftPaymentCompleteMessage.class);
            giftFacade.completePayment(payload.getOrderToken());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
