package me.snowlight.gift.domain.gift;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GiftServiceImpl implements GiftService {
    private final GiftStore giftStore;

    public GiftInfo.Main registerGift(GiftCommand.RegisterGift command) {
        Gift gift = this.giftStore.store(command.toEntity());

        return GiftInfo.Main.builder()
                .orderToken(gift.getOrderToken())
                .giftToken(gift.getGiftToken())
                .pushType(gift.getPushType())
                .giftReceiverName(gift.getGiftReceiverName())
                .giftReceiverPhone(gift.getGiftReceiverPhone())
                .giftMessage(gift.getGiftMessage())
                .build();
    }
}
