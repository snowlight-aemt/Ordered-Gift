package me.snowlight.gift.infrastructure.gift;

import me.snowlight.gift.domain.gift.Gift;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GiftRepository extends JpaRepository<Gift, Long> {

}
