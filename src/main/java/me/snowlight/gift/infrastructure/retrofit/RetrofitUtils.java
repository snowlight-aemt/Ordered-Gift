package me.snowlight.gift.infrastructure.retrofit;

import lombok.extern.slf4j.Slf4j;
import me.snowlight.gift.common.response.CommonResponse;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
public class RetrofitUtils {
    public <T extends CommonResponse> Optional<T> responseSync(Call<T> call) {
        try {
            Response<T> execute = call.execute();
            if (execute.isSuccessful()) {
                return Optional.ofNullable(execute.body());
            } else {
                log.error("requestSync errorBody = {}", execute.errorBody());
                throw new RuntimeException("retrofit execute response error");
            }
        } catch (IOException e) {
            log.error("", e);
            throw new RuntimeException("retrofit execute IOException");
        }
    }
}
