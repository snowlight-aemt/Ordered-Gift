package me.snowlight.gift.domain.gift;

import lombok.RequiredArgsConstructor;
import me.snowlight.gift.domain.gift.order.OrderApiCaller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GiftServiceImpl implements GiftService {
    private final GiftReader giftReader;
    private final GiftStore giftStore;
    private final OrderApiCaller orderApiCaller;
    private final GiftCommandMapper giftCommandMapper;

    @Transactional
    @Override
    public GiftInfo.Main registerOrder(GiftCommand.RegisterOrder command) {
        String orderToken = orderApiCaller.registerGiftOrder(giftCommandMapper.of(command));
        Gift gift = this.giftStore.store(command.toEntity(orderToken));
        return new GiftInfo.Main(gift);
    }

    @Transactional
    @Override
    public void completePayment(String orderToken) {
        Gift gift = this.giftReader.getGiftByOrderToken(orderToken);
        gift.completePayment();
    }

    @Transactional
    @Override
    public void requestPaymentProcessing(String giftToken) {
        Gift gift = this.giftReader.getGiftByGiftToken(giftToken);
        gift.inPayment();
    }

    @Transactional(readOnly = true)
    @Override
    public GiftInfo.Main retrieveOrder(String giftToken) {
        Gift gift = this.giftReader.getGiftByGiftToken(giftToken);
        return new GiftInfo.Main(gift);
    }

    @Transactional
    @Override
    public void acceptGift(GiftCommand.Accept command) {
        String giftToken = command.getGiftToken();
        Gift gift = this.giftReader.getGiftByGiftToken(giftToken);
        gift.accept(command);

        this.orderApiCaller.updateReceiverInfo(gift.getOrderToken(), command);
    }
}
