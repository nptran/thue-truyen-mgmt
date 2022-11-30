package com.ptit.thuetruyenmgmt.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book_title")
public class BookTitle implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String code;

    @Column
    private String titleName;

    @Column
    private int quantity;

    @Column
    private String author;

    @Column
    private int year;

    @Column
    private double price;

    /**
     * Map sang {@link java.util.List} {@link Publisher#books} trong {@link Publisher}
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "publisher_id", referencedColumnName = "id", nullable = false)
    private Publisher publisher;

    /**
     * Map sang bookTitle trong {@link RentedBook#bookTitle}
     */
    @OneToMany(mappedBy = "bookTitle", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<RentedBook> rentedBooks;

}
