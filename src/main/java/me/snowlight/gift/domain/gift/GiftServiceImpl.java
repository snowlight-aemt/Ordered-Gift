package me.snowlight.gift.domain.gift;

import lombok.RequiredArgsConstructor;
import me.snowlight.gift.domain.gift.order.OrderApiCaller;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GiftServiceImpl implements GiftService {
    private final GiftReader giftReader;
    private final GiftStore giftStore;
    private final OrderApiCaller orderApiCaller;
    private final GiftCommandMapper giftCommandMapper;

    public GiftInfo.Main registerOrder(GiftCommand.RegisterOrder command) {
        String orderToken = orderApiCaller.registerGiftOrder(giftCommandMapper.of(command));
        Gift gift = this.giftStore.store(command.toEntity(orderToken));
        return new GiftInfo.Main(gift);
    }

    @Override
    public void completePayment(String orderToken) {
        Gift gift = this.giftReader.getGiftByOrderToken(orderToken);
        gift.completePayment();
    }
}
