package me.snowlight.gift.interfaces.api.gift;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.snowlight.gift.domain.gift.Gift;
import me.snowlight.gift.domain.gift.GiftInfo;

import javax.validation.constraints.*;
import java.util.List;

public class GiftDto {
    @Getter
    @Builder
    public static class RegisterGift {
        @Min(0)
        @NotNull
        private final Long buyerUserId;
        @NotEmpty
        private final String giftReceiverName;
        @NotEmpty
        private final String giftReceiverPhone;
        @NotEmpty
        private final String giftMessage;
        @NotNull
        private final Gift.PushType pushType;
        private final String payMethod;
        @Size(min = 1)
        private final List<RegisterOrderItem> orderItems;
    }

    @Getter
    @Builder
    public static class RegisterOrderItem {
        @NotNull
        private final Integer orderCount;
        @NotEmpty
        private final String itemToken;
        @NotEmpty
        private final String itemName;
        @Min(0)
        private final Long itemPrice;

        private final List<RegisterOrderItemOptionGroup> orderItemOptionGroups;
    }

    @Getter
    @Builder
    public static class RegisterOrderItemOptionGroup {
        @Min(1)
        @Max(2)
        private final Integer ordering;
        @NotEmpty
        private final String itemOptionGroupName;
        @Size(min = 1)
        private final List<RegisterOrderItemOption> orderItemOptions;
    }

    @Getter
    @Builder
    public static class RegisterOrderItemOption {
        @Min(1)
        @Max(2)
        private final Integer ordering;
        @NotEmpty
        private final String itemOptionName;
        @Min(0)
        private final Long itemOptionPrice;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RegisterResponse {
        private String orderToken;
        private String giftToken;

        public RegisterResponse(GiftInfo.Main giftInfo) {
            this.orderToken = giftInfo.getOrderToken();
            this.giftToken = giftInfo.getGiftToken();
        }
    }

    public class Payment {
    }
}
