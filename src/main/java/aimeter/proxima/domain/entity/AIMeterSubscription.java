package aimeter.proxima.domain.entity;

import aimeter.proxima.domain.AIMeterSubscriptionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static aimeter.proxima.domain.entity.BaseEntity.DEFAULT_SCHEMA;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ai_meter_subscription", schema = DEFAULT_SCHEMA)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "subscription_type")
public abstract class AIMeterSubscription extends BaseEntity {

    @Column(name = "subscription_type", nullable = false, insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    AIMeterSubscriptionType type;
    
    @OneToOne
    @JoinColumn(name = "device_id")
    AIMeterDevice device;

    @Column
    String description;
}
