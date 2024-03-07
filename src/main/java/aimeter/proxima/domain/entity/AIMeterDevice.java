package aimeter.proxima.domain.entity;

import aimeter.proxima.domain.AIMeterDeviceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static aimeter.proxima.domain.entity.BaseEntity.DEFAULT_SCHEMA;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ai_meter_device", schema = DEFAULT_SCHEMA)
public class AIMeterDevice extends BaseEntity {

    @CreatedBy
    @Column(nullable = false, updatable = false)
    String createdByUser;

    @LastModifiedBy
    String updatedByUser;

    @Column(nullable = false)
    UUID deviceId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    AIMeterDeviceType deviceType;

    @Column
    boolean registered;

    @Column
    boolean deleted;

    @Column
    int batteryLevel;

    @Column
    String address;

    @Column
    String description;
    
    @OneToOne
    @JoinColumn(name = "config_id")
    AIMeterConfig meterConfig;

    @OneToMany
    Set<AIMeterSubscription> integrations = new HashSet<>();

    @OneToMany
    Set<AIMeterData> data = new HashSet<>();
}
