package aimeter.proxima.domain.repository;

import aimeter.proxima.domain.entity.AIMeterTelegramOTPToken;
import aimeter.proxima.domain.entity.AIMeterToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AIMeterTokenRepository extends JpaRepository<AIMeterToken, Long> {
    
    @Query("""
        FROM AIMeterTelegramOTPToken meterToken
        WHERE meterToken.device.id = :meterId""")
    List<AIMeterTelegramOTPToken> listAllTelegramOtpTokensForDevice(@Param("meterId") Long meterId);

}
