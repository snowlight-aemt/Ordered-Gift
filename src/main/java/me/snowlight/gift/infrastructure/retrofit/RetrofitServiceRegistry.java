package me.snowlight.gift.infrastructure.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.snowlight.gift.infrastructure.gift.order.RetrofitOrderApi;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

@Configuration
public class RetrofitServiceRegistry {
    @Value("${gift.order.base-url}")
    private String baseUrl;

    @Bean
    public RetrofitOrderApi retrofitOrderApi() {
        Retrofit retrofit = initRetrofit(baseUrl);
        return retrofit.create(RetrofitOrderApi.class);
    }

    private static final OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS);

    private static final Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    @NotNull
    private static Retrofit initRetrofit(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();
    }
}
