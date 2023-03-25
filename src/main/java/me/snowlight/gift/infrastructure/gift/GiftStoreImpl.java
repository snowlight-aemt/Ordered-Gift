package me.snowlight.gift.infrastructure.gift;

import lombok.RequiredArgsConstructor;
import me.snowlight.gift.domain.gift.Gift;
import me.snowlight.gift.domain.gift.GiftStore;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GiftStoreImpl implements GiftStore {
    private final GiftRepository giftRepository;

    @Override
    public Gift store(Gift gift) {
        return this.giftRepository.save(gift);
    }
}
