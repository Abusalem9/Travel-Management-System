package com.nymble.travelmanagementsystem.service;

import com.nymble.travelmanagementsystem.constants.PassengerType;
import com.nymble.travelmanagementsystem.entity.*;
import com.nymble.travelmanagementsystem.mapper.TravelPackageMapper;
import com.nymble.travelmanagementsystem.repository.*;
import com.nymble.travelmanagementsystem.request.BookingFilterRequest;
import com.nymble.travelmanagementsystem.request.CreateBooking;
import com.nymble.travelmanagementsystem.request.CreateBookingV1;
import com.nymble.travelmanagementsystem.request.UpdateBooking;
import com.nymble.travelmanagementsystem.response.BaseResponse;
import com.nymble.travelmanagementsystem.response.FilterResponse;
import com.nymble.travelmanagementsystem.response.TravelPackageResponse;
import com.nymble.travelmanagementsystem.utils.BaseResponseUtil;
import com.nymble.travelmanagementsystem.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import javax.swing.*;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {

    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    TravelPackageRepository travelPackageRepository;
    @Autowired
    ActivityRepository activityRepository;
    @Autowired
    PassengerRepository passengerRepository;
    @Autowired
    TravelPackageMapper travelPackageMapper;
    @Autowired
    TravelPackagePassengerRepository travelPackagePassengerRepository;
    @Autowired
    EntityManager entityManager;

    /**
     * @param createBooking
     * @return
     */
    @Override
    @Transactional
    public BaseResponse createBooking(CreateBooking createBooking) {
        try {
            BookingEntity bookingEntity = travelPackageMapper.createBookingModelToBookingEntity(createBooking);
            ActivityEntity activityEntity = activityRepository.getActivityEntityByActivityId(createBooking.getActivityId());
            if (ObjectUtils.isEmpty(activityEntity)) {
                return BaseResponseUtil.createBaseResponse(StatusCode.ACTIVITY_NOT_FOUND);
            }
            if (Objects.equals(activityEntity.getActivityCapacity(), activityEntity.getConsumedActivityCapacity())) {
                return BaseResponseUtil.createBaseResponse(StatusCode.ACTIVITY_CAPACITY_FULL);
            }
            TravelPackageEntity travelPackageEntity = travelPackageRepository.getTravelPackageEntityByTravelPackageId(createBooking.getTravelPackageId());
            if (travelPackageEntity.getConsumedPassengerCapacity() >= travelPackageEntity.getPassengerCapacity()) {
                return BaseResponseUtil.createBaseResponse(StatusCode.PASSENGER_CAPACITY_FULL);
            }
            PassengerEntity passenger = passengerRepository.getPassengerEntityByPassengerId(bookingEntity.getPassengerId());
            if (ObjectUtils.isEmpty(passenger)) {
                return BaseResponseUtil.createBaseResponse(StatusCode.PASSENGER_NOT_FOUND);
            }
            log.info("Passenger : {} " + passenger);
            if (Objects.equals(passenger.getPassengerType(), PassengerType.STANDARD.name())) {
                if (passenger.getPassengerBalance().compareTo(activityEntity.getActivityCost()) < 0) {
                    return BaseResponseUtil.createBaseResponse(StatusCode.INSUFFICIENT_PASSENGER_BALANCE);
                }
                bookingEntity.setBookingCost(activityEntity.getActivityCost());
                passenger.setPassengerBalance(passenger.getPassengerBalance().subtract(activityEntity.getActivityCost()));
            }
            if (Objects.equals(passenger.getPassengerType(), PassengerType.GOLD.name())) {
                BigDecimal discountedValue = activityEntity.getActivityCost().multiply(new BigDecimal("0.1"));
                BigDecimal discountedCost = activityEntity.getActivityCost().subtract(discountedValue);
                if (passenger.getPassengerBalance().compareTo(discountedValue) >= 0) {
                    passenger.setPassengerBalance(passenger.getPassengerBalance().subtract(discountedValue));
                    bookingEntity.setBookingCost(discountedCost);
                } else {
                    return BaseResponseUtil.createBaseResponse(StatusCode.INSUFFICIENT_PASSENGER_BALANCE);
                }
            }
            if (Objects.equals(passenger.getPassengerType(), PassengerType.PREMIUM.name())) {
                bookingEntity.setBookingCost(BigDecimal.ZERO);
            }
            bookingEntity.setCreatedBy(createBooking.getCreatedBy());
            bookingEntity.setCreatedByName(createBooking.getCreatedByName());
            BookingEntity savedBookingEntity = bookingRepository.save(bookingEntity);
            log.info("Saved Booking  : {} " + savedBookingEntity);
            travelPackageEntity.setConsumedPassengerCapacity(travelPackageEntity.getConsumedPassengerCapacity() + 1);
            travelPackageRepository.save(travelPackageEntity);
            TravelPackagePassengerEntity travelPackagePassenger = travelPackagePassengerRepository.getTravelPackagePassengerEntityByPassengerIdAndTravelPackageId(createBooking.getPassengerId(), createBooking.getTravelPackageId());
            if (ObjectUtils.isEmpty(travelPackagePassenger)) {
                TravelPackagePassengerEntity travelPackagePassengerEntity = new TravelPackagePassengerEntity();
                travelPackagePassengerEntity.setPassengerId(createBooking.getPassengerId());
                travelPackagePassengerEntity.setTravelPackageId(createBooking.getTravelPackageId());
                travelPackagePassengerEntity.setCreatedBy(createBooking.getCreatedBy());
                travelPackagePassengerEntity.setCreatedByName(createBooking.getCreatedByName());
                TravelPackagePassengerEntity savedPassengerToPackage = travelPackagePassengerRepository.save(travelPackagePassengerEntity);
                log.info("Mapped Passenger to Travel Package" + savedPassengerToPackage);
            }
            PassengerEntity updatedBalancePassenger = passengerRepository.save(passenger);
            log.info("updated Passenger - Balance  : {} " + updatedBalancePassenger);
            activityEntity.setConsumedActivityCapacity(activityEntity.getConsumedActivityCapacity() + 1);
            ActivityEntity consumedCapacitySaved = activityRepository.save(activityEntity);
            log.info("Consumed Activity Capacity  : {} " + consumedCapacitySaved);
            return BaseResponseUtil.createBaseResponse(new TravelPackageResponse(savedBookingEntity), StatusCode.BOOKING_COMPLETED);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponseUtil.createErrorBaseResponse(e.getMessage());
        }
    }

    /**
     * @param createBooking
     * @return
     */
    @Override
    public BaseResponse createSingleOrMultipleBooking(CreateBookingV1 createBooking) {
        try {
            List<ActivityEntity> activityEntities = activityRepository.getActivityEntitiesByActivityIdIn(createBooking.getActivityIds());
            if (CollectionUtils.isEmpty(activityEntities)) {
                return BaseResponseUtil.createBaseResponse(StatusCode.ACTIVITIES_NOT_FOUND);
            }
            List<BookingEntity> savedBookings = new ArrayList<>();
            for (ActivityEntity activityEntity : activityEntities) {
                if (Objects.equals(activityEntity.getActivityCapacity(), activityEntity.getConsumedActivityCapacity())) {
                    return BaseResponseUtil.createBaseResponse(StatusCode.ACTIVITY_CAPACITY_FULL);
                }
                BookingEntity bookingEntity = new BookingEntity();
                bookingEntity.setActivityId(activityEntity.getActivityId());
                bookingEntity.setDestinationId(createBooking.getDestinationId());
                bookingEntity.setTravelPackageId(createBooking.getTravelPackageId());
                BookingEntity savedBookingEntity = bookingRepository.save(bookingEntity);
                log.info("Saved Booking  : {} " + savedBookingEntity);
                activityEntity.setConsumedActivityCapacity(activityEntity.getConsumedActivityCapacity() + 1);
                ActivityEntity consumedCapacitySaved = activityRepository.save(activityEntity);
                log.info("Consumed Activity Capacity  : {} " + consumedCapacitySaved);
                savedBookings.add(savedBookingEntity);
            }
            return BaseResponseUtil.createBaseResponse(new TravelPackageResponse(savedBookings), StatusCode.BOOKING_COMPLETED);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponseUtil.createErrorBaseResponse(e.getMessage());
        }
    }

    /**
     * @param bookingId
     * @return
     */
    @Override
    public BaseResponse getBookingById(String bookingId) {
        try {
            BookingEntity bookingEntity = bookingRepository.getBookingEntityByBookingId(bookingId);
            if (ObjectUtils.isEmpty(bookingEntity)) {
                return BaseResponseUtil.createBaseResponse(StatusCode.BOOKING_NOT_FOUND);
            }
            return BaseResponseUtil.createBaseResponse(new TravelPackageResponse(bookingEntity), StatusCode.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponseUtil.createErrorBaseResponse(e.getMessage());
        }
    }

    /**
     * @param bookingFilterRequest
     * @return
     */
    @Override
    public BaseResponse filterBooking(BookingFilterRequest bookingFilterRequest) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

            // Count query
            CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
            Root<BookingEntity> countRoot = countQuery.from(BookingEntity.class);

            List<Predicate> countPredicates = buildPredicates(bookingFilterRequest, countRoot, criteriaBuilder);
            countQuery.select(criteriaBuilder.count(countRoot));
            countQuery.where(countPredicates.toArray(new Predicate[0]));

            // Execute the count query
            long totalResults = entityManager.createQuery(countQuery).getSingleResult();

            // Data query
            CriteriaQuery<BookingEntity> dataQuery = criteriaBuilder.createQuery(BookingEntity.class);
            Root<BookingEntity> dataRoot = dataQuery.from(BookingEntity.class);

            List<Predicate> dataPredicates = buildPredicates(bookingFilterRequest, dataRoot, criteriaBuilder);
            dataQuery.where(dataPredicates.toArray(new Predicate[0]));

            if (Objects.equals(SortOrder.ASCENDING, bookingFilterRequest.getSortByCreatedDate())) {
                Order updated = criteriaBuilder.asc(dataRoot.get("created"));
                dataQuery.orderBy(updated);
            }
            if (Objects.equals(SortOrder.DESCENDING, bookingFilterRequest.getSortByCreatedDate())) {
                Order updated = criteriaBuilder.desc(dataRoot.get("created"));
                dataQuery.orderBy(updated);
            }

            if (Objects.equals(SortOrder.ASCENDING, bookingFilterRequest.getSortByUpdatedDate())) {
                Order updated = criteriaBuilder.asc(dataRoot.get("updated"));
                dataQuery.orderBy(updated);
            }
            if (Objects.equals(SortOrder.DESCENDING, bookingFilterRequest.getSortByUpdatedDate())) {
                Order updated = criteriaBuilder.desc(dataRoot.get("updated"));
                dataQuery.orderBy(updated);
            }

            // Execute the data query
            List resultList = entityManager.createQuery(dataQuery)
                    .setFirstResult((bookingFilterRequest.getPage() - 1) * bookingFilterRequest.getPageSize())
                    .setMaxResults(bookingFilterRequest.getPageSize())
                    .getResultList();

            long totalPages = (totalResults + bookingFilterRequest.getPageSize() - 1) / bookingFilterRequest.getPageSize(); // Calculate total pages
            long remainingPages = Math.max(0, totalPages - bookingFilterRequest.getPage()); // Calculate remaining pages
            FilterResponse filterResponse = new FilterResponse<>(totalResults, remainingPages, bookingFilterRequest.getPage(), resultList.size(), resultList);
            return BaseResponseUtil.createBaseResponse(filterResponse, StatusCode.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponseUtil.createErrorBaseResponse(e.getMessage());
        }
    }

    private List<Predicate> buildPredicates(BookingFilterRequest filterRequest, Root<BookingEntity> root, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(filterRequest.getBookingId())) {
            predicates.add(root.get("bookingId").in(filterRequest.getBookingId()));
        }

        if (CollectionUtils.isNotEmpty(filterRequest.getActivityId())) {
            predicates.add(root.get("activityId").in(filterRequest.getActivityId()));
        }

        if (CollectionUtils.isNotEmpty(filterRequest.getTravelPackageId())) {
            predicates.add(root.get("travelPackageId").in(filterRequest.getTravelPackageId()));
        }

        if (CollectionUtils.isNotEmpty(filterRequest.getDestinationId())) {
            predicates.add(root.get("destinationId").in(filterRequest.getDestinationId()));
        }

        if (CollectionUtils.isNotEmpty(filterRequest.getPassengerId())) {
            predicates.add(root.get("passengerId").in(filterRequest.getPassengerId()));
        }

        if (filterRequest.getBookingCost() != null) {
            predicates.add(criteriaBuilder.equal(root.get("bookingCost"), filterRequest.getBookingCost()));
        }

        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(filterRequest.getBookingStatus())) {
            predicates.add(root.get("bookingStatus").in(filterRequest.getBookingStatus()));
        }


        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(filterRequest.getCreatedBy())) {
            predicates.add(root.get("createdBy").in(filterRequest.getCreatedBy()));
        }

        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(filterRequest.getCreatedByName())) {
            predicates.add(root.get("createdByName").in(filterRequest.getCreatedByName()));
        }

        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(filterRequest.getUpdatedBy())) {
            predicates.add(root.get("updatedBy").in(filterRequest.getUpdatedBy()));
        }

        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(filterRequest.getUpdatedByName())) {
            predicates.add(root.get("updatedByName").in(filterRequest.getUpdatedByName()));
        }

        if (filterRequest.getCreatedStart() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("created"), filterRequest.getCreatedStart()));
        }
        if (filterRequest.getCreatedEnd() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("created"), filterRequest.getCreatedEnd()));
        }
        if (filterRequest.getUpdatedStart() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("updated"), filterRequest.getUpdatedStart()));
        }
        if (filterRequest.getUpdatedEnd() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("updated"), filterRequest.getUpdatedEnd()));
        }

        if (filterRequest.getDeleted() != null) {
            predicates.add(root.get("deleted").in(filterRequest.getDeleted()));
        }

        if (filterRequest.getActive() != null) {
            predicates.add(root.get("active").in(filterRequest.getActive()));
        }
        return predicates;
    }

    /**
     * @param updateBooking
     * @return
     */
    @Override
    public BaseResponse updateBooking(UpdateBooking updateBooking) {
        try {
            BookingEntity bookingEntity = bookingRepository.getBookingEntityByBookingId(updateBooking.getBookingId());
            if (ObjectUtils.isEmpty(bookingEntity)) {
                return BaseResponseUtil.createBaseResponse(StatusCode.BOOKING_NOT_FOUND);
            }
            if (StringUtils.isNotEmpty(updateBooking.getActivityId())) {
                bookingEntity.setActivityId(updateBooking.getActivityId());
            }

            if (StringUtils.isNotEmpty(updateBooking.getTravelPackageId())) {
                bookingEntity.setTravelPackageId(updateBooking.getTravelPackageId());
            }

            if (StringUtils.isNotEmpty(updateBooking.getDestinationId())) {
                bookingEntity.setDestinationId(updateBooking.getDestinationId());
            }

            if (StringUtils.isNotEmpty(updateBooking.getPassengerId())) {
                bookingEntity.setPassengerId(updateBooking.getPassengerId());
            }

            if (updateBooking.getBookingCost() != null) {
                bookingEntity.setBookingCost(updateBooking.getBookingCost());
            }

            if (StringUtils.isNotEmpty(updateBooking.getBookingStatus())) {
                bookingEntity.setBookingStatus(updateBooking.getBookingStatus());
            }
            if (StringUtils.isNotEmpty(updateBooking.getUpdatedBy())) {
                bookingEntity.setUpdatedBy(updateBooking.getUpdatedBy());
            }

            if (StringUtils.isNotEmpty(updateBooking.getUpdatedByName())) {
                bookingEntity.setUpdatedByName(updateBooking.getUpdatedByName());
            }
            BookingEntity updatedBooking = bookingRepository.save(bookingEntity);
            return BaseResponseUtil.createBaseResponse(new TravelPackageResponse(updatedBooking), StatusCode.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponseUtil.createErrorBaseResponse(e.getMessage());
        }
    }
}
