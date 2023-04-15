package me.snowlight.gift.interfaces.api.gift;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.snowlight.gift.application.gift.GiftFacade;
import me.snowlight.gift.common.response.CommonResponse;
import me.snowlight.gift.domain.gift.GiftCommand;
import me.snowlight.gift.domain.gift.GiftInfo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/gifts")
@RequiredArgsConstructor
public class GiftApiController {
    private final GiftFacade giftFacade;
    private final GiftDtoMapper giftDtoMapper;

    @GetMapping("{giftToken}")
    public CommonResponse retrieveOrder(@PathVariable String giftToken) {
        GiftInfo.Main giftInfo = giftFacade.retrieveOrder(giftToken);
        GiftDto.Main response = giftDtoMapper.of(giftInfo);
        return CommonResponse.success(response);
    }

    @PostMapping
    public CommonResponse registerGift(@RequestBody @Valid GiftDto.RegisterGift giftDto) {
        GiftCommand.RegisterOrder command = this.giftDtoMapper.of(giftDto);
        GiftInfo.Main giftInfo = giftFacade.registerOrder(command);

        GiftDto.RegisterResponse response = new GiftDto.RegisterResponse(giftInfo);
        return CommonResponse.success(response);
    }

    @PostMapping("/{giftToken}/payment-processing")
    public CommonResponse paymentProcessing(@PathVariable String giftToken) {
        this.giftFacade.requestPaymentProcessing(giftToken);

        return CommonResponse.success("OK");
    }

    @PostMapping("/{giftToken}/accept-gift")
    public CommonResponse acceptGift(@PathVariable String giftToken,
                                     @RequestBody @Valid GiftDto.AcceptGiftReq accept) {
        GiftCommand.Accept command =  this.giftDtoMapper.of(giftToken, accept);
        this.giftFacade.requestAcceptGift(command);
        return CommonResponse.success("OK");
    }
}
