package aimeter.proxima.domain.repository;

import aimeter.proxima.domain.entity.AIMeterDevice;
import aimeter.proxima.domain.entity.AIMeterSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AIMeterSubscriptionRepository extends JpaRepository<AIMeterSubscription, Long> {
    
    boolean existsAIMeterSubscriptionByDevice(AIMeterDevice device);
}
