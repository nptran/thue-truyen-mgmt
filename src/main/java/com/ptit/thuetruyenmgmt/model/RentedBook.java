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
@Table(name = "RentedBook")
public class RentedBook implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Map sang {@link java.util.List} {@link Bill#rentedBooks} trong {@link Bill}
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "bill_id", referencedColumnName = "id", nullable = false)
    private Bill bill;

    /**
     * Map sang question trong {@link RentedBookPenalty#rentedBook}
     */
    @OneToMany(mappedBy = "penalty", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RentedBookPenalty> penalties;

}
