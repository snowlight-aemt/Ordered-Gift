package me.snowlight.gift.infrastructure.gift;

import lombok.RequiredArgsConstructor;
import me.snowlight.gift.common.exception.EntityNotFoundException;
import me.snowlight.gift.domain.gift.Gift;
import me.snowlight.gift.domain.gift.GiftReader;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;

@Component
@RequiredArgsConstructor
public class GiftReaderImpl implements GiftReader {
    private final GiftRepository giftRepository;

    @Override
    public Gift getGiftByOrderToken(String orderToken) {
        if (StringUtils.isEmpty(orderToken)) throw new InvalidParameterException();
        return this.giftRepository.findByOrderToken(orderToken)
                                    .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Gift getGiftByGiftToken(String giftToken) {
        if (StringUtils.isEmpty(giftToken)) throw new InvalidParameterException();
        return this.giftRepository.findByGiftToken(giftToken)
                .orElseThrow(EntityNotFoundException::new);
    }
}
