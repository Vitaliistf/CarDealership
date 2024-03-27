package org.vitaliistf.cardealership.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vitaliistf.cardealership.data.Car;
import org.vitaliistf.cardealership.data.User;
import org.vitaliistf.cardealership.data.enums.CarStatus;

import java.util.List;

/**
 * Repository interface for accessing Car entities in the database.
 */
@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    /**
     * Retrieves a list of Car entities by the car status.
     *
     * @param state the status of the car
     * @return a list of cars with the specified status
     */
    List<Car> findByStatus(CarStatus state);

    /**
     * Retrieves a page of Car entities matching the given specification and pageable criteria.
     *
     * @param spec     the specification to filter by
     * @param pageable the pageable criteria
     * @return a page of cars matching the criteria
     */
    Page<Car> findAll(Specification<Car> spec, Pageable pageable);

    /**
     * Retrieves a page of Car entities by the owner and car status.
     *
     * @param owner    the owner of the car
     * @param state    the status of the car
     * @param pageable the pageable criteria
     * @return a page of cars owned by the specified user and with the specified status
     */
    Page<Car> findByOwnerAndStatus(User owner, CarStatus state, Pageable pageable);

    /**
     * Retrieves a page of Car entities by the owner.
     *
     * @param owner    the owner of the car
     * @param pageable the pageable criteria
     * @return a page of cars owned by the specified user
     */
    Page<Car> findByOwner(User owner, Pageable pageable);

}
