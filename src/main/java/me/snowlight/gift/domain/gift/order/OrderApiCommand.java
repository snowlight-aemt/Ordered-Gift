package me.snowlight.gift.domain.gift.order;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

public class OrderApiCommand {

    @Getter
    @Builder
    @ToString
    public static class RegisterOrder {
        private final Long buyerUserId;
        private final String payMethod;
        private final List<RegisterOrderItem> orderItems;
    }

    @Getter
    @Builder
    @ToString
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
    public static class RegisterOrderItemOptionGroup {
        private final Integer ordering;
        private final String itemOptionGroupName;
        private final List<RegisterOrderItemOption> orderItemOptions;
    }

    @Getter
    @Builder
    @ToString
    public static class RegisterOrderItemOption {
        private final Integer ordering;
        private final String itemOptionName;
        private final Long itemOptionPrice;
    }
}

