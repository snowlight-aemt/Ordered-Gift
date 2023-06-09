package me.snowlight.gift.domain.gift;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.snowlight.gift.common.exception.IllegalStatusException;
import me.snowlight.gift.common.exception.InvalidParamException;
import me.snowlight.gift.common.util.TokenGenerator;
import me.snowlight.gift.domain.AbstractEntity;
import org.apache.commons.lang3.StringUtils;

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
        this.expiredAt = ZonedDateTime.now().plusDays(7);
    }

    public void completePayment() {
        this.status = Status.ORDER_COMPLETE;
    }

    public void inPayment() {
        this.status = Status.IN_PAYMENT;
    }

    public void accept(GiftCommand.Accept command) {
        if (!availableAccept()) throw new IllegalStatusException();
        if (StringUtils.isEmpty(command.getReceiverName())) throw new InvalidParamException("Gift accept receiverName is empty");
        if (StringUtils.isEmpty(command.getReceiverPhone())) throw new InvalidParamException("Gift accept receiverPhone is empty");
        if (StringUtils.isEmpty(command.getReceiverZipcode())) throw new InvalidParamException("Gift accept receiverZipcode is empty");
        if (StringUtils.isEmpty(command.getReceiverAddress1())) throw new InvalidParamException("Gift accept receiverAddress1 is empty");
        if (StringUtils.isEmpty(command.getReceiverAddress2())) throw new InvalidParamException("Gift accept receiverAddress2 is empty");
        if (StringUtils.isEmpty(command.getEtcMessage())) throw new InvalidParamException("Gift accept etcMessage is empty");

        this.status = Status.ACCEPT;
        this.receiverName = command.getReceiverName();
        this.receiverPhone = command.getReceiverPhone();
        this.receiverZipcode = command.getReceiverZipcode();
        this.receiverAddress1 = command.getReceiverAddress1();
        this.receiverAddress2 = command.getReceiverAddress2();
        this.etcMessage = command.getEtcMessage();
        this.acceptedAt = ZonedDateTime.now();
    }

    private boolean availableAccept() {
        if (this.expiredAt.isBefore(ZonedDateTime.now()))
            return false;

        return this.status == Status.ORDER_COMPLETE;
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
