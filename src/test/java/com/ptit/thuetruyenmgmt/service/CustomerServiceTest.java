package com.ptit.thuetruyenmgmt.service;

import com.ptit.thuetruyenmgmt.exception.NotFoundException;
import com.ptit.thuetruyenmgmt.model.Address;
import com.ptit.thuetruyenmgmt.model.Customer;
import com.ptit.thuetruyenmgmt.model.FullName;
import com.ptit.thuetruyenmgmt.repository.CustomerRepository;
import com.ptit.thuetruyenmgmt.service.impl.CustomerServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class CustomerServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceTest.class);

    @Mock
    private CustomerRepository repository;

    @InjectMocks
    private CustomerServiceImpl service;


    /**
     * {@link CustomerServiceImpl#getCustomerByName(String)}
     *
     * @POSITIVE_CASE: Repository có trả về ds KH - có dữ liệu
     */
    @Test
    void whenGetCustomerByName_shouldReturnCustomers() {
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
     * {@link CustomerServiceImpl#getCustomerByName(String)}
     *
     * @POSITIVE_CASE: Repository có trả về ds KH - không có dữ liệu
     */
    @Test
    void whenGetCustomerByName_shouldReturnEmpty() {
        String mockKeyword = "Name";
        when(repository.findByName(mockKeyword))
                .thenReturn(new ArrayList<>());

        List<Customer> receivedCustomers = service.getCustomerByName(mockKeyword);
        assertEquals(0, receivedCustomers.size());

        verify(repository).findByName(mockKeyword);
    }


    /**
     * {@link CustomerServiceImpl#getCustomerById(int)}
     *
     * @POSITIVE_CASE: Repository có trả về ds KH - không có dữ liệu
     */
    @Test
    void whenGetCustomerById_shouldReturnACustomer() {
        Customer mock = Customer.builder().id(1).cccd(10000).build();
        when(repository.findById(1)).thenReturn(Optional.of(mock));

        Customer received = service.getCustomerById(1);

        assertEquals(received, mock);

        verify(repository).findById(1);
    }


    /**
     * {@link CustomerServiceImpl#getCustomerById(int)}
     *
     * @NEGATIVE_CASE: Repository có trả về ds KH - không có dữ liệu
     */
    @Test
    void whenGetCustomerById_shouldThrowNotFoundException() {
        when(repository.findById(1)).thenReturn(Optional.ofNullable(null));

        Assertions.assertThatThrownBy(() -> service.getCustomerById(1))
                .isInstanceOf(NotFoundException.class);

        verify(repository).findById(1);
    }

}
