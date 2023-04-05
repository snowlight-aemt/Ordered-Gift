package me.snowlight.gift.interfaces.api.gift;

import me.snowlight.gift.domain.gift.order.ItemInfo;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ItemInfoMapper {
    @Mapping(target = "orderItemOptionGroups", source = "itemOptionGroupList")
    GiftDto.RegisterOrderItem of (ItemInfo.Main request);
    @Mapping(target = "orderItemOptions", source = "itemOptionList")
    GiftDto.RegisterOrderItemOptionGroup of (ItemInfo.ItemOptionGroupInfo request);
    GiftDto.RegisterOrderItemOption of (ItemInfo.ItemOptionInfo request);
}
