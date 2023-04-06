package me.snowlight.gift.domain.gift;

public interface GiftReader {
    public Gift getGiftByOrderToken(String orderToken);

    public Gift getGiftByGiftToken(String giftToken);
}
