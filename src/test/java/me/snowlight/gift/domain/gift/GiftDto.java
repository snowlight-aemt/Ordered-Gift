package me.snowlight.gift.domain.gift;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class GiftDto {

    @Getter
    @Builder
    public static class PaymentRequest {
        @NotBlank(message = "orderToken 는 필수값입니다")
        private String orderToken;
        @NotNull(message = "payMethod 는 필수값입니다")
        private String payMethod;
        @NotNull(message = "amount 는 필수값입니다")
        private Long amount;
        @NotBlank(message = "orderDescription 는 필수값입니다")
        private String orderDescription;
    }
}
