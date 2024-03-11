package aimeter.proxima.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static aimeter.proxima.domain.entity.BaseEntity.DEFAULT_SCHEMA;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ai_meter_data", schema = DEFAULT_SCHEMA)
public class AIMeterData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Version
    @ColumnDefault("0")
    Long version;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    LocalDateTime dateCreated;

    @Column(nullable = false)
    String mimeType;

    @Column(nullable = false)
    String imageName;

    @Column(nullable = false)
    long imageSize;

    @Column(nullable = false)
    byte[] imageData;

    @Column(nullable = false)
    LocalDateTime imageDate;

    @Column
    BigDecimal reading;

    @ManyToOne
    @JoinColumn(name = "meter_id")
    AIMeterDevice device;
}
