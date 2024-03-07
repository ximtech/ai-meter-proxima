package aimeter.proxima.domain.repository;

import aimeter.proxima.domain.entity.AIMeterSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AIMeterIntegrationRepository extends JpaRepository<AIMeterSubscription, Long> {

}
