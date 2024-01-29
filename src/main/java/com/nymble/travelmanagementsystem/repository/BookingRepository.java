package com.nymble.travelmanagementsystem.repository;

import com.nymble.travelmanagementsystem.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, String> {
    BookingEntity getBookingEntityByBookingId(String bookingId);
}
