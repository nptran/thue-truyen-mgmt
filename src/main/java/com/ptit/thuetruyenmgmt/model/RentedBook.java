package com.ptit.thuetruyenmgmt.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rented_book")
public class RentedBook implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private double amount;

    @Column
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
    @JoinColumn(name = "bill_id", referencedColumnName = "id")
    private Bill bill;

    /**
     * Map sang rentedBook trong {@link RentedBookPenalty#rentedBook}
     */
    @OneToMany(mappedBy = "rentedBook", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<RentedBookPenalty> penalties;

    @Column
    private boolean isPaid;

}
