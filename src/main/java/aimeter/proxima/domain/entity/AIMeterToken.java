package aimeter.proxima.domain.entity;

import aimeter.proxima.domain.AIMeterSubscriptionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

import static aimeter.proxima.domain.entity.BaseEntity.DEFAULT_SCHEMA;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ai_meter_token", schema = DEFAULT_SCHEMA)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "token_type", discriminatorType = DiscriminatorType.STRING)
public class AIMeterToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Version
    @ColumnDefault("0")
    Long version;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    LocalDateTime dateCreated;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meter_id", nullable = false)
    AIMeterDevice device;
    
    @Column(nullable = false, updatable = false)
    String accessToken;

    @Column(nullable = false, updatable = false)
    LocalDateTime expiresIn;
}
