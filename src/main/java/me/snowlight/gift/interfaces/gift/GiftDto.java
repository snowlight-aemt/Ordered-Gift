package me.snowlight.gift.interfaces.gift;

import lombok.Builder;
import lombok.Getter;
import me.snowlight.gift.domain.gift.Gift;
import me.snowlight.gift.domain.gift.GiftInfo;

import java.util.List;

public class GiftDto {
    @Getter
    @Builder
    public static class RegisterGift {
        private final Long buyerUserId;
        private final String giftReceiverName;
        private final String giftReceiverPhone;
        private final String giftMessage;
        private final Gift.PushType pushType;
        private final String payMethod;
        private final List<RegisterOrderItem> orderItems;
    }

    @Getter
    @Builder
    public static class RegisterOrderItem {
        private final String orderCount;
        private final String itemToken;
        private final String itemName;
        private final String itemPrice;
        private final List<RegisterOrderItemOptionGroup> orderItemOptionGroups;
    }

    @Getter
    @Builder
    public static class RegisterOrderItemOptionGroup {
        private final Integer ordering;
        private final String itemOptionGroupName;
        private final List<RegisterOrderItemOption> orderItemOptions;
    }

    @Getter
    @Builder
    public static class RegisterOrderItemOption {
        private final Integer ordering;
        private final String itemOptionName;
        private final Long itemOptionPrice;
    }

    @Getter
    public static class RegisterResponse {
        private final String orderToken;
        private final String giftToken;

        public RegisterResponse(GiftInfo.Main giftInfo) {
            this.orderToken = giftInfo.getOrderToken();
            this.giftToken = giftInfo.getGiftToken();
        }
    }
}
