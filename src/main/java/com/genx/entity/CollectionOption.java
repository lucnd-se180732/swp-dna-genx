package com.genx.entity;

import com.genx.enums.ECollectionMethod;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "collection_options")
@Getter
@Setter
public class CollectionOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private Long id;

    @Column(name = "option_name", nullable = false, length = 100)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private ServiceType serviceType;

    @Enumerated(EnumType.STRING)
    @Column(name = "collection_method", nullable = false)
    private ECollectionMethod collectionMethod;
}