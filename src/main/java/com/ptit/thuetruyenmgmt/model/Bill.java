package com.ptit.thuetruyenmgmt.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Bill")
public class Bill implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private double totalAmount;

    @Column
    @CreatedDate
    private LocalDateTime createTime;

    private List<RentedBook> rentedBooks;

    private Staff staff;

}
