package me.snowlight.gift.domain.gift.order;

import me.snowlight.gift.domain.gift.GiftCommand;

public interface OrderApiCaller {
    public String registerGiftOrder(OrderApiCommand.RegisterOrder orderCommand);

    public void updateReceiverInfo(String orderToken, GiftCommand.Accept command);
}
