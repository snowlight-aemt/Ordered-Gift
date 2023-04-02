package me.snowlight.gift.application.gift;

import lombok.RequiredArgsConstructor;
import me.snowlight.gift.domain.gift.GiftCommand;
import me.snowlight.gift.domain.gift.GiftInfo;
import me.snowlight.gift.domain.gift.GiftService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GiftFacade {
    private final GiftService giftService;

    public GiftInfo.Main registerOrder(GiftCommand.RegisterOrder command) {
        return this.giftService.registerOrder(command);
    }

    public void completePayment(String orderToken) {
        this.giftService.completePayment(orderToken);
    }
}
