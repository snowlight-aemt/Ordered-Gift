package me.snowlight.gift.domain.gift;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.snowlight.gift.common.util.TokenGenerator;
import me.snowlight.gift.domain.gift.order.ItemInfo;
import me.snowlight.gift.util.TestSpecificLanguage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GiftServiceImplTest {
    @Autowired
    private GiftService giftService;

    @Autowired
    TestSpecificLanguage testSpecificLanguage;

    @DisplayName("상품 조회")
    @Test
    void test() throws JsonProcessingException {
        String partnerToken = testSpecificLanguage.requestRegisterPartner();
        String itemToken = testSpecificLanguage.requestRegisterItem(partnerToken);
        ItemInfo.Main itemInfo = testSpecificLanguage.requestRetrieveItem(itemToken);
        System.out.println(itemInfo);
    }

    @DisplayName("선물 등록")
    @Test
    void sut_register_gift() {
        String orderToken = TokenGenerator.randomCharacterWithPrefix(Gift.GIFT_PREFIX);
        Long userId = 1234L;
        String receiverName = "name";
        String receiverPhone = "000-000-0000";
        Gift.PushType pushType = Gift.PushType.KAKAO;
        String giftMessage = "message";

        GiftCommand.RegisterOrder command = GiftCommand.RegisterOrder.builder()
                .buyerUserId(userId)
                .giftReceiverName(receiverName)
                .giftReceiverPhone(receiverPhone)
                .giftMessage(giftMessage)
                .pushType(pushType)
                .build();

        GiftInfo.Main sut = giftService.registerOrder(command);

        assertThat(sut.getGiftReceiverName()).isEqualTo(receiverName);
        assertThat(sut.getGiftReceiverPhone()).isEqualTo(receiverPhone);
        assertThat(sut.getGiftMessage()).isEqualTo(giftMessage);
        assertThat(sut.getPushType()).isEqualTo(pushType);
        assertThat(sut.getOrderToken()).isEqualTo(orderToken);
    }
}