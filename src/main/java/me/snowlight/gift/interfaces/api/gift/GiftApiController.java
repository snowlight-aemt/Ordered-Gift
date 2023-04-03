package me.snowlight.gift.interfaces.api.gift;

import lombok.RequiredArgsConstructor;
import me.snowlight.gift.application.gift.GiftFacade;
import me.snowlight.gift.common.response.CommonResponse;
import me.snowlight.gift.domain.gift.GiftCommand;
import me.snowlight.gift.domain.gift.GiftInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/gifts")
@RequiredArgsConstructor
public class GiftApiController {
    private final GiftFacade giftFacade;
    private final GiftDtoMapper giftDtoMapper;

    @PostMapping
    public CommonResponse registerGift(@RequestBody @Valid GiftDto.RegisterGift giftDto) {
        GiftCommand.RegisterOrder command = this.giftDtoMapper.of(giftDto);
        GiftInfo.Main giftInfo = giftFacade.registerOrder(command);

        return CommonResponse.success(new GiftDto.RegisterResponse(giftInfo));
    }
}
