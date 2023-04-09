package me.snowlight.gift.domain.gift;

import lombok.*;

import javax.validation.constraints.Size;
import java.util.List;

@NoArgsConstructor
public class GiftCommand {
    @Getter
    @Builder
    @ToString
    @AllArgsConstructor
    public static class RegisterOrder {
        private final Long buyerUserId;
        private final String giftReceiverName;
        private final String giftReceiverPhone;
        private final String giftMessage;
        private final Gift.PushType pushType;
        private final String payMethod;
        @Size(min = 1)
        private final List<RegisterOrderItem> orderItems;

        public Gift toEntity(String orderToken) {
            return Gift.builder()
                    .buyerUserId(this.buyerUserId)
                    .orderToken(orderToken)
                    .giftReceiverName(this.giftReceiverName)
                    .giftReceiverPhone(this.giftReceiverPhone)
                    .giftMessage(this.giftMessage)
                    .pushType(this.pushType)
                    .build();
        }
    }

    @Getter
    @Builder
    @ToString
    @AllArgsConstructor
    public static class RegisterOrderItem {
        private final String orderCount;
        private final String itemToken;
        private final String itemName;
        private final String itemPrice;
        private final List<RegisterOrderItemOptionGroup> orderItemOptionGroups;
    }

    @Getter
    @Builder
    @ToString
    @AllArgsConstructor
    public static class RegisterOrderItemOptionGroup {
        private final Integer ordering;
        private final String itemOptionGroupName;
        private final List<RegisterOrderItemOption> orderItemOptions;
    }

    @Getter
    @Builder
    @ToString
    @AllArgsConstructor
    public static class RegisterOrderItemOption {
        private final Integer ordering;
        private final String itemOptionName;
        private final Long itemOptionPrice;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Accept {
        private String giftToken;
        private String receiverName;
        private String receiverPhone;
        private String receiverZipcode;
        private String receiverAddress1;
        private String receiverAddress2;
        private String etcMessage;
    }
}
