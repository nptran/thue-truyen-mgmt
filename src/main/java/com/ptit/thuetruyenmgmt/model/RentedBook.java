package com.ptit.thuetruyenmgmt.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
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
     * Map sang {@link List} {@link BookTitle#rentedBooks} trong {@link BookTitle}
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "book_title_id", referencedColumnName = "id", nullable = false)
    private BookTitle bookTitle;

    /**
     * Map sang {@link List} {@link Customer#rentedBooks} trong {@link Customer}
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    private Customer customer;

    /**
     * Map sang {@link List} {@link Bill#rentedBooks} trong {@link Bill}
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RentedBook that = (RentedBook) o;
        return (id == null && that.id == null) || id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
