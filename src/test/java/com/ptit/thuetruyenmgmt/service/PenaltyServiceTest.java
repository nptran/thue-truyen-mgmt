package com.ptit.thuetruyenmgmt.service;

import com.ptit.thuetruyenmgmt.exception.NotFoundException;
import com.ptit.thuetruyenmgmt.model.Penalty;
import com.ptit.thuetruyenmgmt.repository.PenaltyRepository;
import com.ptit.thuetruyenmgmt.service.impl.PenaltyServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class PenaltyServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceTest.class);

    @Mock
    private PenaltyRepository repository;

    @InjectMocks
    private PenaltyServiceImpl service;


    /**
     * {@link PenaltyServiceImpl#getAllPenalties()}
     *
     * @POSITIVE_CASE: Repository có trả về ds Penalty - có dữ liệu
     */
    @Test
    void whenGetAllPenalties_shouldReturnPenalties() {
        List<Penalty> mock = IntStream.range(0, 5)
                .mapToObj(i -> Penalty.builder().id(i).name("Lỗi " + i).build())
                .collect(Collectors.toList());
        when(repository.findAll()).thenReturn(mock);

        List<Penalty> received = service.getAllPenalties();

        assertEquals(received.size(), mock.size());

        verify(repository, times(1)).findAll();

        verifyNoMoreInteractions(repository);
    }


    /**
     * {@link PenaltyServiceImpl#getAllPenalties()}
     *
     * @POSITIVE_CASE: Repository có trả về ds KH - không có dữ liệu
     */
    @Test
    void whenGetCustomerByName_shouldReturnEmpty() {
        when(repository.findAll()).thenReturn(new ArrayList<>());

        List<Penalty> received = service.getAllPenalties();

        assertEquals(0, received.size());

        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }


    /**
     * {@link PenaltyServiceImpl#getPenaltyById(int)}
     *
     * @POSITIVE_CASE: Repository có trả về một Penalty
     */
    @Test
    void whenGetPenaltyById_shouldReturnAPenalty() {
        Penalty mock = Penalty.builder().id(1).name("Lỗi").build();
        when(repository.findById(1)).thenReturn(Optional.of(mock));

        Penalty received = service.getPenaltyById(1);

        assertEquals(received, mock);

        verify(repository, times(1)).findById(1);
        verifyNoMoreInteractions(repository);
    }


    /**
     * {@link PenaltyServiceImpl#getPenaltyById(int)}
     *
     * @NEGATIVE_CASE: Repository không trả về Penalty → Throw {@link NotFoundException}
     */
    @Test
    void whenGetCustomerById_shouldThrowNotFoundException() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> service.getPenaltyById(1))
                .isInstanceOf(NotFoundException.class);

        verify(repository, times(1)).findById(1);
        verifyNoMoreInteractions(repository);
    }

}
