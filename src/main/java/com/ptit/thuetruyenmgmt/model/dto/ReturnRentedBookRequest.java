package com.ptit.thuetruyenmgmt.model.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnRentedBookRequest implements Serializable {

    private int customerId;

    private List<RentedBookDTO> rentedBookDtos;

}
