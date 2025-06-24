package com.genx.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "adn_result")
@Data
@Getter
@Setter
@Builder
public class AdnResult {

    @Id
    private Long id;

    private LocalDateTime createdAt;

    private String conclusion;

    // Lưu lại tham chiếu Booking để biết những ai tham gia
    @OneToOne
    @MapsId
    @JoinColumn(name = "booking_id", referencedColumnName = "id")
    private Booking booking;

    // Lưu kết quả từng locus
    @ElementCollection
    @CollectionTable(name = "adn_locus_result", joinColumns = @JoinColumn(name = "adn_result_id"))
    @MapKeyColumn(name = "locus_name")
    @Column(name = "locus_value") // format: "14;16 - 16;17"
    private Map<String, String> lociResults = new HashMap<>();

}
