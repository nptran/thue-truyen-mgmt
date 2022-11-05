package com.ptit.thuetruyenmgmt.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rented_book_penalty")
public class RentedBookPenalty implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Map sang {@link java.util.List} {@link RentedBook#penalties} trong {@link RentedBook}
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "rentedbook_id", referencedColumnName = "id", nullable = false)
    private RentedBook rentedBook;

    /**
     * Map sang {@link java.util.List} {@link Penalty#detectedPenalties} trong {@link Penalty}
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "penalty_id", referencedColumnName = "id", nullable = false)
    private Penalty penalty;

    @Column
    private double fee;

}
