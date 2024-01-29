package com.nymble.travelmanagementsystem.service;

import com.nymble.travelmanagementsystem.entity.PassengerEntity;
import com.nymble.travelmanagementsystem.mapper.TravelPackageMapper;
import com.nymble.travelmanagementsystem.repository.PassengerRepository;
import com.nymble.travelmanagementsystem.request.CreatePassengerV1;
import com.nymble.travelmanagementsystem.request.PassengerFilterRequest;
import com.nymble.travelmanagementsystem.request.UpdatePassenger;
import com.nymble.travelmanagementsystem.response.BaseResponse;
import com.nymble.travelmanagementsystem.response.FilterResponse;
import com.nymble.travelmanagementsystem.response.TravelPackageResponse;
import com.nymble.travelmanagementsystem.utils.BaseResponseUtil;
import com.nymble.travelmanagementsystem.utils.StatusCode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PassengerServiceImpl implements PassengerService {

    @Autowired
    PassengerRepository passengerRepository;

    @Autowired
    TravelPackageMapper travelPackageMapper;
    @Autowired
    EntityManager entityManager;

    /**
     * @param createPassenger
     * @return
     */
    @Override
    public BaseResponse createPassenger(CreatePassengerV1 createPassenger) {
        try {
            PassengerEntity passengerEntity = travelPackageMapper.createPassengerModelToPassengerEntity(createPassenger);
            PassengerEntity savedPassenger = passengerRepository.save(passengerEntity);
            return BaseResponseUtil.createBaseResponse(new TravelPackageResponse(savedPassenger), StatusCode.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponseUtil.createErrorBaseResponse(e.getMessage());
        }
    }

    /**
     * @param updatePassenger
     * @return
     */
    @Override
    public BaseResponse updatePassenger(UpdatePassenger updatePassenger) {
        try {
            PassengerEntity passengerEntity = passengerRepository.getPassengerEntityByPassengerIdOrPassengerNumber(updatePassenger.getPassengerId(), updatePassenger.getPassengerNumber());
            if (ObjectUtils.isEmpty(passengerEntity)) {
                return BaseResponseUtil.createBaseResponse(StatusCode.PASSENGER_NOT_FOUND);
            }
            if (StringUtils.isNotEmpty(updatePassenger.getPassengerId())) {
                passengerEntity.setPassengerId(updatePassenger.getPassengerId());
            }

            if (StringUtils.isNotEmpty(updatePassenger.getPassengerNumber())) {
                passengerEntity.setPassengerNumber(updatePassenger.getPassengerNumber());
            }

            if (StringUtils.isNotEmpty(updatePassenger.getPassengerName())) {
                passengerEntity.setPassengerName(updatePassenger.getPassengerName());
            }

            if (updatePassenger.getPassengerType() != null) {
                passengerEntity.setPassengerType(updatePassenger.getPassengerType().name());
            }

            if (updatePassenger.getPassengerBalance() != null) {
                passengerEntity.setPassengerBalance(updatePassenger.getPassengerBalance());
            }

            if (StringUtils.isNotEmpty(updatePassenger.getUpdatedBy())) {
                passengerEntity.setUpdatedBy(updatePassenger.getUpdatedBy());
            }

            if (StringUtils.isNotEmpty(updatePassenger.getUpdatedByName())) {
                passengerEntity.setUpdatedByName(updatePassenger.getUpdatedByName());
            }
            return BaseResponseUtil.createBaseResponse(StatusCode.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponseUtil.createErrorBaseResponse(e.getMessage());
        }
    }

    /**
     * @param passengerFilterRequest
     * @return
     */
    @Override
    public BaseResponse filterPassenger(PassengerFilterRequest passengerFilterRequest) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

            // Count query
            CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
            Root<PassengerEntity> countRoot = countQuery.from(PassengerEntity.class);

            List<Predicate> countPredicates = buildPredicates(passengerFilterRequest, countRoot, criteriaBuilder);
            countQuery.select(criteriaBuilder.count(countRoot));
            countQuery.where(countPredicates.toArray(new Predicate[0]));

            // Execute the count query
            long totalResults = entityManager.createQuery(countQuery).getSingleResult();

            // Data query
            CriteriaQuery<PassengerEntity> dataQuery = criteriaBuilder.createQuery(PassengerEntity.class);
            Root<PassengerEntity> dataRoot = dataQuery.from(PassengerEntity.class);

            List<Predicate> dataPredicates = buildPredicates(passengerFilterRequest, dataRoot, criteriaBuilder);
            dataQuery.where(dataPredicates.toArray(new Predicate[0]));

            if (Objects.equals(SortOrder.ASCENDING, passengerFilterRequest.getSortByCreatedDate())) {
                Order updated = criteriaBuilder.asc(dataRoot.get("created"));
                dataQuery.orderBy(updated);
            }
            if (Objects.equals(SortOrder.DESCENDING, passengerFilterRequest.getSortByCreatedDate())) {
                Order updated = criteriaBuilder.desc(dataRoot.get("created"));
                dataQuery.orderBy(updated);
            }

            if (Objects.equals(SortOrder.ASCENDING, passengerFilterRequest.getSortByUpdatedDate())) {
                Order updated = criteriaBuilder.asc(dataRoot.get("updated"));
                dataQuery.orderBy(updated);
            }
            if (Objects.equals(SortOrder.DESCENDING, passengerFilterRequest.getSortByUpdatedDate())) {
                Order updated = criteriaBuilder.desc(dataRoot.get("updated"));
                dataQuery.orderBy(updated);
            }

            // Execute the data query
            List resultList = entityManager.createQuery(dataQuery)
                    .setFirstResult((passengerFilterRequest.getPage() - 1) * passengerFilterRequest.getPageSize())
                    .setMaxResults(passengerFilterRequest.getPageSize())
                    .getResultList();

            long totalPages = (totalResults + passengerFilterRequest.getPageSize() - 1) / passengerFilterRequest.getPageSize(); // Calculate total pages
            long remainingPages = Math.max(0, totalPages - passengerFilterRequest.getPage()); // Calculate remaining pages
            FilterResponse filterResponse = new FilterResponse<>(totalResults, remainingPages, passengerFilterRequest.getPage(), resultList.size(), resultList);
            return BaseResponseUtil.createBaseResponse(filterResponse, StatusCode.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponseUtil.createErrorBaseResponse(e.getMessage());
        }
    }

    private List<Predicate> buildPredicates(PassengerFilterRequest filterRequest, Root<PassengerEntity> root, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(filterRequest.getPassengerId())) {
            predicates.add(root.get("passengerId").in(filterRequest.getPassengerId()));
        }

        if (CollectionUtils.isNotEmpty(filterRequest.getPassengerName())) {
            predicates.add(root.get("passengerName").in(filterRequest.getPassengerName()));
        }

        if (CollectionUtils.isNotEmpty(filterRequest.getPassengerNumber())) {
            predicates.add(root.get("passengerNumber").in(filterRequest.getPassengerNumber()));
        }

        if (CollectionUtils.isNotEmpty(filterRequest.getPassengerType())) {
            predicates.add(root.get("passengerType").in(filterRequest.getPassengerType()));
        }

        if (filterRequest.getPassengerBalance() != null) {
            predicates.add(criteriaBuilder.equal(root.get("passengerBalance"), filterRequest.getPassengerBalance()));
        }

        if (CollectionUtils.isNotEmpty(filterRequest.getCreatedBy())) {
            predicates.add(root.get("createdBy").in(filterRequest.getCreatedBy()));
        }

        if (CollectionUtils.isNotEmpty(filterRequest.getCreatedByName())) {
            predicates.add(root.get("createdByName").in(filterRequest.getCreatedByName()));
        }

        if (CollectionUtils.isNotEmpty(filterRequest.getUpdatedBy())) {
            predicates.add(root.get("updatedBy").in(filterRequest.getUpdatedBy()));
        }

        if (CollectionUtils.isNotEmpty(filterRequest.getUpdatedByName())) {
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
     * @param passengerId
     * @return
     */
    @Override
    public BaseResponse getPassengerById(String passengerId) {
        try {
            PassengerEntity passengerEntity = passengerRepository.getPassengerEntityByPassengerId(passengerId);
            if (ObjectUtils.isEmpty(passengerEntity)) {
                return BaseResponseUtil.createBaseResponse(StatusCode.PASSENGER_NOT_FOUND);
            }
            return BaseResponseUtil.createBaseResponse(new TravelPackageResponse(passengerEntity), StatusCode.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponseUtil.createErrorBaseResponse(e.getMessage());
        }
    }
}
