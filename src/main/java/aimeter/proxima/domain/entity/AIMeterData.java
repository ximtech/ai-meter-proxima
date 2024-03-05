package aimeter.proxima.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

import static aimeter.proxima.domain.entity.BaseEntity.DEFAULT_SCHEMA;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ai_meter_data", schema = DEFAULT_SCHEMA)
public class AIMeterData extends BaseEntity {

    @Column
    String mimeType;

    @Column
    String imageName;

    @Column
    long imageSize;

    @Column
    byte[] imageData;

    @Column
    BigDecimal reading;

    @ManyToOne
    @JoinColumn(name = "device_id")
    AIMeterDevice device;
}
