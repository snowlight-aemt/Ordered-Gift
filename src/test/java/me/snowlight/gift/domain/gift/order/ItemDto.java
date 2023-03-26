package me.snowlight.gift.domain.gift.order;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;

public class ItemDto {

    public static class ItemTest {
        String partnerToken = RandomStringUtils.randomAlphanumeric(5);
        String itemName = RandomStringUtils.randomAlphanumeric(5);
        Long itemPrice = RandomUtils.nextLong(100000L, 200000000L);
        List<ItemOptionGroup> itemOptionGroups = List.of(new ItemOptionGroup());

        public void setPartnerToken(String partnerToken) {
            this.partnerToken = partnerToken;
        }

        public String getPartnerToken() {
            return partnerToken;
        }

        public String getItemName() {
            return itemName;
        }

        public Long getItemPrice() {
            return itemPrice;
        }

        public List<ItemOptionGroup> getItemOptionGroups() {
            return itemOptionGroups;
        }
    }

    public static class ItemOptionGroup {

        String ordering = "1";
        String itemOptionGroupName = "사이즈";
        List<ItemOption> itemOptionList = List.of(new ItemOption());

        public String getOrdering() {
            return ordering;
        }

        public String getItemOptionGroupName() {
            return itemOptionGroupName;
        }

        public List<ItemOption> getItemOptionList() {
            return itemOptionList;
        }
    }

    public static class ItemOption {
        Integer ordering = 1;
        String itemOptionName = "SMALL";
        Long itemOptionPrice = 100000L;

        public Integer getOrdering() {
            return ordering;
        }

        public String getItemOptionName() {
            return itemOptionName;
        }

        public Long getItemOptionPrice() {
            return itemOptionPrice;
        }
    }
}
