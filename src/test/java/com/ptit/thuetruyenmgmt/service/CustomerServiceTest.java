package com.ptit.thuetruyenmgmt.service;

import com.ptit.thuetruyenmgmt.repository.CustomerRepository;
import com.ptit.thuetruyenmgmt.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository repository;

    @InjectMocks
    private CustomerServiceImpl service;

    /**
     * POSITIVE CASE: Có tên
     */
    @Test
    public void whenGetCustomerByName_shouldReturnList() {

    }

}
