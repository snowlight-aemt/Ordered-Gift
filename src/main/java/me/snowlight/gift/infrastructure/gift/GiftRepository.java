package me.snowlight.gift.infrastructure.gift;

import me.snowlight.gift.domain.gift.Gift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GiftRepository extends JpaRepository<Gift, Long> {

    Optional<Gift> findByOrderToken(String orderToken);

    Optional<Gift> findByGiftToken(String giftToken);
}
