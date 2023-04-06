package me.snowlight.gift.domain.gift;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.javax.validation.plugin.JavaxValidationPlugin;
import me.snowlight.gift.domain.gift.order.ItemInfo;
import me.snowlight.gift.domain.gift.order.OrderApiCaller;
import me.snowlight.gift.domain.gift.order.OrderApiCommand;
import me.snowlight.gift.util.TestSpecificLanguage;
import net.jqwik.api.Arbitraries;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GiftServiceImplTest {
    @Autowired
    private GiftService giftService;
    @Autowired
    private GiftReader giftReader;

    @Autowired
    TestSpecificLanguage testSpecificLanguage;
    private final static FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
                                            .objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
                                            .plugin(new JavaxValidationPlugin())
                                            .build();

    @DisplayName("상품 등록")
    @Test
    void test() throws Exception {
        String partnerToken = testSpecificLanguage.requestRegisterPartner();
        String itemToken = testSpecificLanguage.requestRegisterItem(partnerToken);
        testSpecificLanguage.requestChangedItemStatusOnSales(itemToken);
        ItemInfo.Main itemInfo = testSpecificLanguage.requestRetrieveItem(itemToken);

        assertThat(itemInfo.getItemToken()).isEqualTo(itemToken);
        assertThat(itemInfo.getStatus()).isEqualTo(ItemInfo.Status.ON_SALES);
    }

    @DisplayName("선물 등록")
    @Test
    void sut_register_gift() throws Exception {
        String partnerToken = testSpecificLanguage.requestRegisterPartner();
        String itemToken = testSpecificLanguage.requestRegisterItem(partnerToken);
        testSpecificLanguage.requestChangedItemStatusOnSales(itemToken);
        ItemInfo.Main itemInfo = testSpecificLanguage.requestRetrieveItem(itemToken);

        Long userId = 11111L;
        String receiverName = "name";
        String receiverPhone = "000-000-0000";
        Gift.PushType pushType = Gift.PushType.KAKAO;
        String giftMessage = "message";

        List<GiftCommand.RegisterOrderItemOptionGroup> registerOrderItemOptionGroups = Lists.newArrayList();
        for (ItemInfo.ItemOptionGroupInfo itemOptionGroupInfo: itemInfo.getItemOptionGroupList()) {
            List<GiftCommand.RegisterOrderItemOption> optionInfo = new ArrayList<>();
            for (ItemInfo.ItemOptionInfo option: itemOptionGroupInfo.getItemOptionList()) {
                optionInfo.add(GiftCommand.RegisterOrderItemOption.builder()
                        .ordering(option.getOrdering())
                        .itemOptionName(option.getItemOptionName())
                        .itemOptionPrice(option.getItemOptionPrice())
                        .build());
            }

            registerOrderItemOptionGroups.add(GiftCommand.RegisterOrderItemOptionGroup.builder()
                    .itemOptionGroupName(itemOptionGroupInfo.getItemOptionGroupName())
                    .ordering(itemOptionGroupInfo.getOrdering())
                    .orderItemOptions(optionInfo)
                    .build());
        }

        GiftCommand.RegisterOrder command = GiftCommand.RegisterOrder.builder()
                .buyerUserId(userId)
                .payMethod("KAKAO_PAY")
                .giftReceiverName(receiverName)
                .giftReceiverPhone(receiverPhone)
                .giftMessage(giftMessage)
                .pushType(pushType)
                .orderItems(List.of(GiftCommand.RegisterOrderItem.builder()
                        .orderCount("1")
                        .itemName(itemInfo.getItemName())
                        .itemPrice(itemInfo.getItemPrice().toString())
                        .itemToken(itemInfo.getItemToken())
                        .orderItemOptionGroups(registerOrderItemOptionGroups)
                        .build()))
                .build();

        GiftInfo.Main sut = giftService.registerOrder(command);

        assertThat(sut.getOrderToken()).isNotEmpty();
    }

    @MockBean
    public OrderApiCaller orderApiCaller;

    @Test
    void requestPaymentProcessing() {
        Mockito.when(orderApiCaller.registerGiftOrder(ArgumentMatchers.any(OrderApiCommand.RegisterOrder.class)))
                .thenReturn("test_111111111111111111111111");
        GiftCommand.RegisterOrder registerOrder = fixtureMonkey.giveMeBuilder(GiftCommand.RegisterOrder.class)
                .set("pushType", Gift.PushType.KAKAO)
                .set("giftReceiverName", Arbitraries.strings().ofLength(5))
                .set("giftReceiverPhone", Arbitraries.strings().ofLength(5))
                .set("giftMessage", Arbitraries.strings().ofLength(5))
                .set("buyerUserId", Arbitraries.longs().between(10, 20))
                .sample();
        GiftInfo.Main giftInfo = this.giftService.registerOrder(registerOrder);
        this.giftService.requestPaymentProcessing(giftInfo.getGiftToken());

        Gift sut = this.giftReader.getGiftByGiftToken(giftInfo.getGiftToken());

        Assertions.assertThat(sut.getStatus()).isEqualTo(Gift.Status.IN_PAYMENT);
    }
}