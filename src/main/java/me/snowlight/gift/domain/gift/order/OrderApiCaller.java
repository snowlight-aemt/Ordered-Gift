package me.snowlight.gift.domain.gift.order;

public interface OrderApiCaller {
    public String registerGiftOrder(OrderApiCommand.RegisterOrder orderCommand);
}
