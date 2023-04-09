package me.snowlight.gift.infrastructure.gift.order;

import me.snowlight.gift.common.response.CommonResponse;
import me.snowlight.gift.domain.gift.GiftCommand;
import me.snowlight.gift.domain.gift.order.OrderApiCommand;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitOrderApi {
    @POST("api/v1/gift-orders/init")
    Call<CommonResponse<RetrofitOrderApiResponse.Register>> registerOrder(@Body OrderApiCommand.RegisterOrder request);

    @POST("api/v1/gift-orders/{orderToken}/update-receiver-info")
    Call<Void> updateReceiverInfo(@Path("orderToken") String orderToken, @Body GiftCommand.Accept request);
}
