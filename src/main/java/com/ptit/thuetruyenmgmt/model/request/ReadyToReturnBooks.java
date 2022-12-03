package com.ptit.thuetruyenmgmt.model.request;

import com.ptit.thuetruyenmgmt.model.RentedBook;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReadyToReturnBooks implements Serializable {

    private List<RentedBook> willBeReturnedBooks;

    private Integer customerId;

}
