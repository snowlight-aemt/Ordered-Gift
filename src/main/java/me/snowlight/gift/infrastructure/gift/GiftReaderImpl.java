package me.snowlight.gift.infrastructure.gift;

import lombok.RequiredArgsConstructor;
import me.snowlight.gift.domain.gift.Gift;
import me.snowlight.gift.domain.gift.GiftReader;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
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
}
