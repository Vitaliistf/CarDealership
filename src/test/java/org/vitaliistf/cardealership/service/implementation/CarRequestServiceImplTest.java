package org.vitaliistf.cardealership.service.implementation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.vitaliistf.cardealership.data.Car;
import org.vitaliistf.cardealership.data.CarRequest;
import org.vitaliistf.cardealership.data.User;
import org.vitaliistf.cardealership.exception.ForbiddenException;
import org.vitaliistf.cardealership.repository.CarRequestRepository;
import org.vitaliistf.cardealership.service.CarSuggestionService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarRequestServiceImplTest {

    @Mock
    private CarRequestRepository carRequestRepository;

    @Mock
    private CarSuggestionService carSuggestionService;

    @InjectMocks
    private CarRequestServiceImpl carRequestService;

    @Test
    void testCreateCarRequest() {
        User user = new User();
        CarRequest request = new CarRequest();

        carRequestService.createCarRequest(request, user);

        verify(carRequestRepository, times(1)).save(request);
        assertEquals(user, request.getUser());
    }

    @Test
    void testDeleteCarRequestById() {
        User user = new User();
        user.setId(1L);
        CarRequest request = new CarRequest();
        request.setId(1L);
        request.setUser(user);

        when(carRequestRepository.findById(1L)).thenReturn(Optional.of(request));

        carRequestService.deleteCarRequestById(1L, user);

        verify(carRequestRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCarRequestByIdForbiddenException() {
        User user = new User();
        user.setId(2L);
        CarRequest request = new CarRequest();
        request.setId(1L);
        request.setUser(new User());
        request.getUser().setId(1L);

        when(carRequestRepository.findById(1L)).thenReturn(Optional.of(request));

        assertThrows(ForbiddenException.class, () -> carRequestService.deleteCarRequestById(1L, user));
    }

    @Test
    void testEditCarRequest() {
        User user = new User();
        user.setId(1L);
        CarRequest request = new CarRequest();
        request.setId(1L);
        request.setUser(user);
        CarRequest editedRequest = new CarRequest();

        when(carRequestRepository.findById(1L)).thenReturn(Optional.of(request));

        carRequestService.editCarRequest(1L, editedRequest, user);

        verify(carRequestRepository, times(1)).save(editedRequest);
        assertEquals(1L, editedRequest.getId());
        assertEquals(user, editedRequest.getUser());
    }

    @Test
    void testEditCarRequestForbiddenException() {
        User user = new User();
        user.setId(2L);
        CarRequest request = new CarRequest();
        request.setId(1L);
        request.setUser(new User());
        request.getUser().setId(1L);
        CarRequest editedRequest = new CarRequest();

        when(carRequestRepository.findById(1L)).thenReturn(Optional.of(request));

        assertThrows(ForbiddenException.class, () -> carRequestService.editCarRequest(1L, editedRequest, user));
    }

    @Test
    void testGetCarRequestById() {
        User user = new User();
        user.setId(1L);
        CarRequest request = new CarRequest();
        request.setId(1L);
        request.setUser(user);

        when(carRequestRepository.findById(1L)).thenReturn(Optional.of(request));

        CarRequest result = carRequestService.getCarRequestById(1L, user);

        assertEquals(request, result);
    }

    @Test
    void testGetCarRequestByIdForbiddenException() {
        User user = new User();
        user.setId(2L);
        CarRequest request = new CarRequest();
        request.setId(1L);
        request.setUser(new User());
        request.getUser().setId(1L);

        when(carRequestRepository.findById(1L)).thenReturn(Optional.of(request));

        assertThrows(ForbiddenException.class, () -> carRequestService.getCarRequestById(1L, user));
    }

    @Test
    void testGetCarRequestsByUser() {
        User user = new User();
        Pageable pageable = PageRequest.of(0, 10);
        Page<CarRequest> page = new PageImpl<>(List.of(new CarRequest(), new CarRequest()));

        when(carRequestRepository.findByUser(user, pageable)).thenReturn(page);

        Page<CarRequest> result = carRequestService.getCarRequestsByUser(user, pageable);

        assertEquals(page, result);
    }

    @Test
    void testGetCarsByCarRequest() {
        CarRequest request = new CarRequest();
        List<Car> cars = List.of(new Car(), new Car());

        when(carSuggestionService.findCarsByRequest(request)).thenReturn(cars);

        List<Car> result = carRequestService.getCarsByCarRequest(request);

        assertEquals(cars, result);
    }

}
