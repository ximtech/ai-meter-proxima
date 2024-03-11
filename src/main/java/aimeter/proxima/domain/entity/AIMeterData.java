package aimeter.proxima.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static aimeter.proxima.domain.entity.BaseEntity.DEFAULT_SCHEMA;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ai_meter_data", schema = DEFAULT_SCHEMA)
public class AIMeterData extends BaseEntity {

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
