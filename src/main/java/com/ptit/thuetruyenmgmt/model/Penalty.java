package com.ptit.thuetruyenmgmt.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "penalty")
public class Penalty implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private double recommended_fee;

    /**
     * Map sang question trong {@link RentedBookPenalty#penalty}
     */
    @OneToMany(mappedBy = "penalty", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RentedBookPenalty> detectedPenalties;

}
