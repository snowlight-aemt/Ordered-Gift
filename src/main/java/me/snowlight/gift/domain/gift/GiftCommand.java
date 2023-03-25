package me.snowlight.gift.domain.gift;

import lombok.Builder;

public class GiftCommand {
    @Builder
    public static class RegisterGift {
        private Long buyerUserId;
        private String orderToken;
        private String giftReceiverName;
        private String giftReceiverPhone;
        private String giftMessage;
        private Gift.PushType pushType;

        public Gift toEntity() {
            return Gift.builder()
                    .buyerUserId(buyerUserId)
                    .orderToken(this.orderToken)
                    .giftReceiverName(this.giftReceiverName)
                    .giftReceiverPhone(this.giftReceiverPhone)
                    .giftMessage(this.giftMessage)
                    .pushType(this.pushType)
                    .build();
        }
    }
}
