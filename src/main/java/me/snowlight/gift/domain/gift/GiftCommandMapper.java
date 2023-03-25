package me.snowlight.gift.domain.gift;

import me.snowlight.gift.domain.gift.order.OrderApiCommand;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface GiftCommandMapper {
    OrderApiCommand.RegisterOrder of(GiftCommand.RegisterOrder request);
    OrderApiCommand.RegisterOrderItem of(GiftCommand.RegisterOrderItem request);
    OrderApiCommand.RegisterOrderItemOptionGroup of(GiftCommand.RegisterOrderItemOptionGroup request);
    OrderApiCommand.RegisterOrderItemOption of(GiftCommand.RegisterOrderItemOption request);
}