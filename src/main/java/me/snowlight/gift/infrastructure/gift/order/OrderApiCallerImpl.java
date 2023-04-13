package me.snowlight.gift.infrastructure.gift.order;

import lombok.RequiredArgsConstructor;
import me.snowlight.gift.common.response.CommonResponse;
import me.snowlight.gift.domain.gift.GiftCommand;
import me.snowlight.gift.domain.gift.order.OrderApiCaller;
import me.snowlight.gift.domain.gift.order.OrderApiCommand;
import me.snowlight.gift.infrastructure.retrofit.RetrofitUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderApiCallerImpl implements OrderApiCaller {
    private final RetrofitOrderApi retrofitOrderApi;
    private final RetrofitUtils retrofitUtils;

    @Override
    public String registerGiftOrder(OrderApiCommand.RegisterOrder orderCommand) {
        var call = this.retrofitOrderApi.registerOrder(orderCommand);
        var registerCommonResponse = this.retrofitUtils.responseSync(call);

        return registerCommonResponse
                .map(CommonResponse::getData)
                .map(RetrofitOrderApiResponse.Register::getOrderToken)
                .orElseThrow(RuntimeException::new);
    }

    @Override
    public void updateReceiverInfo(String orderToken, GiftCommand.Accept command) {
        var call = retrofitOrderApi.updateReceiverInfo(orderToken, command);
        retrofitUtils.responseVoid(call);
    }
}
