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
@Table(name = "BookTitle")
public class BookTitle implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String code;

    @Column
    private String titleName;

    @Column
    private String quantity;

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

}
