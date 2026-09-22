package com.flyaway.travel.repository;

import com.flyaway.travel.model.Booking;
import com.flyaway.travel.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByCustomer(User customer);

    List<Booking> findByCustomerId(Long customerId);
}
