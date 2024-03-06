package aimeter.proxima.domain.entity;

import aimeter.proxima.domain.AIMeterIntegrationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import static aimeter.proxima.domain.entity.BaseEntity.DEFAULT_SCHEMA;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ai_meter_data_transaction", schema = DEFAULT_SCHEMA)
public class AIMeterDataTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Version
    @ColumnDefault("0")
    Long version;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    AIMeterIntegrationStatus status;
    
    @OneToOne
    @JoinColumn(name = "data_id")
    AIMeterData data;

    @OneToOne
    @JoinColumn(name = "integration_id")
    AIMeterIntegration integration;
}
