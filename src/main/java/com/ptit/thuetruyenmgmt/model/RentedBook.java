package com.ptit.thuetruyenmgmt.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rented_book")
public class RentedBook implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private double amount;

    private LocalDateTime rentedTime;

    /**
     * Map sang {@link java.util.List} {@link BookTitle#rentedBooks} trong {@link BookTitle}
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "book_title_id", referencedColumnName = "id", nullable = false)
    private BookTitle bookTitle;

    /**
     * Map sang {@link java.util.List} {@link Customer#rentedBooks} trong {@link Customer}
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    private Customer customer;

    /**
     * Map sang {@link java.util.List} {@link Bill#rentedBooks} trong {@link Bill}
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "bill_id", referencedColumnName = "id", nullable = false)
    private Bill bill;

    /**
     * Map sang rentedBook trong {@link RentedBookPenalty#rentedBook}
     */
    @OneToMany(mappedBy = "penalty", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RentedBookPenalty> penalties;

}
