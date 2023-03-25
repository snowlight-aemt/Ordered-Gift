package me.snowlight.gift.domain.gift;

import me.snowlight.gift.common.util.TokenGenerator;
import me.snowlight.gift.infrastructure.gift.GiftRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GiftServiceImplTest {
    @Autowired
    private GiftService giftService;
    @Autowired
    private GiftRepository giftRepository;

    @DisplayName("선물 등록")
    @Test
    void sut_register_gift() {
        String orderToken = TokenGenerator.randomCharacterWithPrefix(Gift.GIFT_PREFIX);
        Long userId = 1234L;
        String receiverName = "name";
        String receiverPhone = "000-000-0000";
        Gift.PushType pushType = Gift.PushType.KAKAO;
        String giftMessage = "message";

        GiftCommand.RegisterGift command = GiftCommand.RegisterGift.builder()
                .buyerUserId(userId)
                .giftReceiverName(receiverName)
                .giftReceiverPhone(receiverPhone)
                .giftMessage(giftMessage)
                .pushType(pushType)
                .orderToken(orderToken)
                .build();

        GiftInfo.Main sut = giftService.registerGift(command);

        Assertions.assertThat(sut.getGiftReceiverName()).isEqualTo(receiverName);
        Assertions.assertThat(sut.getGiftReceiverPhone()).isEqualTo(receiverPhone);
        Assertions.assertThat(sut.getGiftMessage()).isEqualTo(giftMessage);
        Assertions.assertThat(sut.getPushType()).isEqualTo(pushType);
        Assertions.assertThat(sut.getOrderToken()).isEqualTo(orderToken);
    }
}