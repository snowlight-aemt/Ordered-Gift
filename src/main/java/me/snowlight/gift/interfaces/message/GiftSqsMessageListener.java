package me.snowlight.gift.interfaces.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.snowlight.gift.application.gift.GiftFacade;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GiftSqsMessageListener {
    private final GiftFacade giftFacade;

    @SqsListener(value = "order-payComplete.fifo", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void readMessage(GiftPaymentCompleteMessage message) {
        String orderToken = message.getOrderToken();
        giftFacade.completePayment(orderToken);
    }
}
