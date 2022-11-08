package com.ptit.thuetruyenmgmt.model.request;

import com.ptit.thuetruyenmgmt.model.RentedBook;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReturnRentedBookRequest implements Serializable {

    private int customerId;

    private List<RentedBookDTO> rentedBookDtos;


    public void addReadyToReturnBook(RentedBookDTO book) {
        this.rentedBookDtos.add(book);
    }

}
