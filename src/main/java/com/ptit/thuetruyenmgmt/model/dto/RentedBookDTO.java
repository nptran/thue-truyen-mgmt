package com.ptit.thuetruyenmgmt.model.dto;

import com.ptit.thuetruyenmgmt.model.*;
import lombok.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class RentedBookDTO implements Serializable {

    private int customerId;

    private int rentedBookId;

    private String code;

    private String titleName;

    private LocalDateTime rentedTime;

    private double amount;

    private double amountTilToday;

    @Valid
    private List<Penalty> penalties;

    public static RentedBookDTO fromRentedBook(RentedBook rentedBook) {
        return RentedBookDTO.builder()
                .customerId(rentedBook.getCustomer().getId())
                .rentedBookId(rentedBook.getId())
                .code(rentedBook.getBookTitle().getCode())
                .titleName(rentedBook.getBookTitle().getTitleName())
                .rentedTime(rentedBook.getRentedTime())
                .amount(rentedBook.getAmount())
                .amountTilToday(calculateAmountTilToday(rentedBook))
                .penalties(rentedBookPenaltiesToPenalties(rentedBook.getPenalties()))
                .build();
    }

    public static List<RentedBookDTO> rentedBooksToRentedBookDTOs(List<RentedBook> rentedBooks) {
        List<RentedBookDTO> rentedBookDtos = new ArrayList<>();
        for (RentedBook book : rentedBooks) {
            rentedBookDtos.add(fromRentedBook(book));
        }

        return rentedBookDtos;
    }


    public static double calculateAmountTilToday(RentedBook book) {
        double amount = book.getAmount();
        // Tính tổng số thời gian đã mượn
        LocalDateTime rentedFrom = book.getRentedTime();
        Duration totalTime = Duration.between(rentedFrom, LocalDateTime.now());
        long days = totalTime.toDays();
        if (days == 0) days = 1; // thời gian thuê tối thiểu
        amount *= days; // Nhân số ngày thuê

        return amount;
    }


    public static List<Penalty> rentedBookPenaltiesToPenalties(List<RentedBookPenalty> rentedBookPenalties) {
        List<Penalty> penalties = new ArrayList<>();
        if (rentedBookPenalties != null) {
            for (RentedBookPenalty r : rentedBookPenalties) {
                Penalty p = r.getPenalty();
                p.setRecommendedFee(r.getFee());
                penalties.add(p);
            }
        }
        return penalties;
    }

}
