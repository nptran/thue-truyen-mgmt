package com.ptit.thuetruyenmgmt.service;

import com.ptit.thuetruyenmgmt.model.Address;
import com.ptit.thuetruyenmgmt.model.Customer;
import com.ptit.thuetruyenmgmt.model.FullName;
import com.ptit.thuetruyenmgmt.repository.CustomerRepository;
import com.ptit.thuetruyenmgmt.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class CustomerServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceTest.class);

    @Mock
    private CustomerRepository repository;

    @InjectMocks
    private CustomerServiceImpl service;


    /**
     * POSITIVE CASE: Repository có trả về ds KH - có dữ liệu
     */
    @Test
    public void whenGetCustomerByName_shouldReturnCustomers() {
        List<Customer> mockCustomers = IntStream.range(0, 5)
                .mapToObj(i -> new Customer(i,
                        1000 + i,
                        "0999999" + i,
                        "customer" + i + "@mail.com",
                        LocalDate.of(2000, 1, 1),
                        new FullName(i, "Tên" + i, "Họ" + i),
                        Address.builder().id(i).city("Hà Nội").build(),
                        null
                ))
                .collect(Collectors.toList());
        String mockKeyword = "Name";
        when(repository.findByName(mockKeyword))
                .thenReturn(mockCustomers);

        List<Customer> receivedCustomers = service.getCustomerByName(mockKeyword);

        assertEquals(receivedCustomers.size(), mockCustomers.size());

        verify(repository).findByName(mockKeyword);
    }


    /**
     * POSITIVE CASE: Repository có trả về ds KH - không có dữ liệu
     */
    @Test
    public void whenGetCustomerByName_shouldReturnEmpty() {
        List<Customer> mockCustomers = new ArrayList<>();
        String mockKeyword = "Name";
        when(repository.findByName(mockKeyword))
                .thenReturn(mockCustomers);

        assertEquals(0, mockCustomers.size());

        verify(repository).findByName(mockKeyword);
    }

}
