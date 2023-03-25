package me.snowlight.gift.domain.gift;

public interface GiftService {
    public GiftInfo.Main registerGift(GiftCommand.RegisterGift command);
}
