package me.snowlight.gift.domain.gift;

public interface GiftService {
    public GiftInfo.Main registerOrder(GiftCommand.RegisterOrder command);

    void completePayment(String orderToken);
}
