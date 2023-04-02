package me.snowlight.gift.domain.gift;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.snowlight.gift.common.util.TokenGenerator;
import me.snowlight.gift.domain.AbstractEntity;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Getter
@Entity
@Table(name = "gifts")
@NoArgsConstructor
public class Gift extends AbstractEntity {
    public static final String GIFT_PREFIX = "gt_";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String giftToken;
    private String orderToken;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private PushType pushType;

    private String giftReceiverName;
    private String giftReceiverPhone;
    private String giftMessage;

    private String receiverName;
    private String receiverPhone;
    private String receiverZipcode;
    private String receiverAddress1;
    private String receiverAddress2;
    private String etcMessage;

    private Long buyerUserId;

    private ZonedDateTime paidAt;
    private ZonedDateTime pushedAt;
    private ZonedDateTime acceptedAt;
    private ZonedDateTime expiredAt;

    @Builder
    public Gift(Long buyerUserId,
                String orderToken,
                String giftReceiverName,
                String giftReceiverPhone,
                String giftMessage,
                PushType pushType) {
        if (StringUtils.isEmpty(orderToken)) throw new IllegalArgumentException("Gift.orderToken");
        if (StringUtils.isEmpty(giftReceiverName)) throw new IllegalArgumentException("Gift.giftReceiverName");
        if (StringUtils.isEmpty(giftReceiverPhone)) throw new IllegalArgumentException("Gift.giftReceiverPhone");
        if (orderToken.isEmpty()) throw new IllegalArgumentException("Gift.pushType");

        this.buyerUserId = buyerUserId;
        this.orderToken = orderToken;
        this.pushType = pushType;
        this.giftReceiverName = giftReceiverName;
        this.giftReceiverPhone = giftReceiverPhone;
        this.giftMessage = giftMessage;

        this.giftToken = TokenGenerator.randomCharacterWithPrefix(GIFT_PREFIX);
        this.status = Status.INIT;
    }

    public void completePayment() {
        this.status = Status.ORDER_COMPLETE;
    }

    @Getter
    @AllArgsConstructor
    public enum Status {
        INIT("선물 주문 생성"),
        IN_PAYMENT("결제 중"),
        ORDER_COMPLETE("주문 완료"),
        PUSH_COMPLETE("선물 링크 발송 완료"),
        ACCEPT("선물 수락"),
        DELIVERY_PREPARE("상품준비"),
        IN_DELIVERY("배송중"),
        DELIVERY_COMPLETE("배송완료"),
        EXPIRATION("선물 수락 만료");

        private final String description;
    }

    @Getter
    @AllArgsConstructor
    public enum PushType {
        KAKAO("카카오톡"),
        SMS("문자");

        private final String description;
    }
}
