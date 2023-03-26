package me.snowlight.gift.infrastructure.gift.order;

import lombok.RequiredArgsConstructor;
import me.snowlight.gift.common.response.CommonResponse;
import me.snowlight.gift.domain.gift.order.OrderApiCaller;
import me.snowlight.gift.domain.gift.order.OrderApiCommand;
import me.snowlight.gift.infrastructure.retrofit.RetrofitUtils;
import org.springframework.stereotype.Component;
import retrofit2.Call;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderApiCallerImpl implements OrderApiCaller {
    private final RetrofitOrderApi retrofitOrderApi;

    @Override
    public String registerGiftOrder(OrderApiCommand.RegisterOrder orderCommand) {
        Call<CommonResponse<RetrofitOrderApiResponse.Register>> call = this.retrofitOrderApi.registerOrder(orderCommand);

        Optional<CommonResponse<RetrofitOrderApiResponse.Register>> registerCommonResponse = RetrofitUtils.responseSync(call);
        return registerCommonResponse
                .map(CommonResponse::getData)
                .map(RetrofitOrderApiResponse.Register::getOrderToken)
                .orElseThrow(RuntimeException::new);
    }
}
