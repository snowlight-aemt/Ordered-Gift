package me.snowlight.gift.interfaces.message;

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

    @SqsListener(value = "order-payComplete.fifo")
    public void readMessage(GiftPaymentCompleteMessage message) {
        String orderToken = message.getOrderToken();
        giftFacade.completePayment(orderToken);
    }
}
