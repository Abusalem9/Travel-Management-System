package com.nymble.travelmanagementsystem.service;

import com.nymble.travelmanagementsystem.request.BookingFilterRequest;
import com.nymble.travelmanagementsystem.request.CreateBooking;
import com.nymble.travelmanagementsystem.request.CreateBookingV1;
import com.nymble.travelmanagementsystem.request.UpdateBooking;
import com.nymble.travelmanagementsystem.response.BaseResponse;

public interface BookingService {
    BaseResponse createBooking(CreateBooking createBooking);

    BaseResponse createSingleOrMultipleBooking(CreateBookingV1 createBooking);

    BaseResponse getBookingById(String bookingId);

    BaseResponse filterBooking(BookingFilterRequest bookingFilterRequest);

    BaseResponse updateBooking(UpdateBooking updateBooking);
}
