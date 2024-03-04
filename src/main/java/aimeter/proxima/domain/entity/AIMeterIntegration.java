package aimeter.proxima.domain.entity;

import aimeter.proxima.domain.AIMeterIntegrationStatus;
import aimeter.proxima.domain.AIMeterIntegrationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static aimeter.proxima.domain.entity.BaseEntity.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ai_meter_integration", schema = DEFAULT_SCHEMA)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class AIMeterIntegration extends BaseEntity {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    AIMeterIntegrationType type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    AIMeterIntegrationStatus status;
    
    @Column
    String description;
}
