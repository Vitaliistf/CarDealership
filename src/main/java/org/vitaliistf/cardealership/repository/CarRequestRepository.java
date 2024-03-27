package org.vitaliistf.cardealership.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vitaliistf.cardealership.data.CarRequest;
import org.vitaliistf.cardealership.data.User;

/**
 * Repository interface for accessing CarRequest entities in the database.
 */
@Repository
public interface CarRequestRepository extends JpaRepository<CarRequest, Long> {

    /**
     * Retrieves a page of CarRequest entities by the user.
     *
     * @param user     the user associated with the car requests
     * @param pageable the pageable criteria
     * @return a page of car requests associated with the specified user
     */
    Page<CarRequest> findByUser(User user, Pageable pageable);

}
