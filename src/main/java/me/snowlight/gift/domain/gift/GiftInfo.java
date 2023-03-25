package me.snowlight.gift.domain.gift;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

public class GiftInfo {

    @Getter
    @Builder
    @ToString
    public static class Main {
        private final String orderToken;
        private final String giftToken;
        private final Gift.PushType pushType;
        private final String giftReceiverName;
        private final String giftReceiverPhone;
        private final String giftMessage;
    }
}
