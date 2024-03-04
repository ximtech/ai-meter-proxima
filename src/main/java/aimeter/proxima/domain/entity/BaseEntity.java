package aimeter.proxima.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {
    
    public static final String DEFAULT_SCHEMA = "meter";  

    @Id
    @GeneratedValue
    Long id;

    @Version
    @ColumnDefault("0")
    Long version;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    LocalDateTime dateCreated;

    @UpdateTimestamp
    @Column(nullable = false)
    LocalDateTime dateUpdated;

}
