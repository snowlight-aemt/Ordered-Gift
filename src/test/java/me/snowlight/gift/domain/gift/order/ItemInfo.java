package me.snowlight.gift.domain.gift.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

public class ItemInfo {

    @Getter
    public static class Main {
        private String itemToken;
        private Long partnerId;
        private String itemName;
        private Long itemPrice;

        private Status status;
        private List<ItemOptionGroupInfo> itemOptionGroupList;
    }

    @Getter
    public static class ItemOptionGroupInfo {
        private String itemOptionGroupName;
        private Integer ordering;

        private List<ItemOptionInfo> itemOptionList;
    }

    @Getter
    public static class ItemOptionInfo {
        private Integer ordering;
        private String itemOptionName;
        private Long itemOptionPrice;
    }

    @Getter
    @AllArgsConstructor
    public enum Status {
        PREPARE("판매준비중"),
        ON_SALES("판매중"),
        END_OF_SALES("판매완료");

        private final String description;
    }
}
