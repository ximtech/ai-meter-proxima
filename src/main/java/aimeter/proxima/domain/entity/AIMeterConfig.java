package aimeter.proxima.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

import static aimeter.proxima.domain.entity.BaseEntity.DEFAULT_SCHEMA;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ai_meter_config", schema = DEFAULT_SCHEMA)
public class AIMeterConfig extends BaseEntity {
    
    @Column
    String deviceName;

    @Column
    String deviceIp;

    @Column
    String deviceTimeZone;

    @Column
    String cronExpression;

    @Column
    LocalDateTime lastExecutionTime;
    
    @OneToOne(mappedBy = "meterConfig")
    AIMeterDevice device;

    @OneToMany
    @JoinColumn(name = "config_id")
    Set<AIMeterIntegration> integrations;
}
