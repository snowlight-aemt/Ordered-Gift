package me.snowlight.gift.interfaces.api.gift;

import me.snowlight.gift.domain.gift.GiftCommand;
import me.snowlight.gift.domain.gift.GiftInfo;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface GiftDtoMapper {
    GiftCommand.RegisterOrder of(GiftDto.RegisterGift request);
    GiftCommand.RegisterOrderItem of (GiftDto.RegisterOrderItem request);
    GiftCommand.RegisterOrderItemOptionGroup of (GiftDto.RegisterOrderItemOptionGroup request);
    GiftCommand.RegisterOrderItemOption of(GiftDto.RegisterOrderItemOption request);
    GiftCommand.Accept of(String giftToken, GiftDto.AcceptGiftReq request);
    GiftDto.Main of (GiftInfo.Main request);
}
