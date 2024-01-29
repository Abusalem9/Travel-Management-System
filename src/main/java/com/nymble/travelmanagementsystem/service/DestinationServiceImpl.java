package com.nymble.travelmanagementsystem.service;

import com.nymble.travelmanagementsystem.entity.ActivityEntity;
import com.nymble.travelmanagementsystem.entity.DestinationEntity;
import com.nymble.travelmanagementsystem.mapper.TravelPackageMapper;
import com.nymble.travelmanagementsystem.repository.DestinationRepository;
import com.nymble.travelmanagementsystem.request.CreateDestination;
import com.nymble.travelmanagementsystem.request.DestinationFilterRequest;
import com.nymble.travelmanagementsystem.request.UpdateDestination;
import com.nymble.travelmanagementsystem.response.BaseResponse;
import com.nymble.travelmanagementsystem.response.FilterResponse;
import com.nymble.travelmanagementsystem.response.TravelPackageResponse;
import com.nymble.travelmanagementsystem.utils.BaseResponseUtil;
import com.nymble.travelmanagementsystem.utils.StatusCode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class DestinationServiceImpl implements DestinationService {
    @Autowired
    DestinationRepository destinationRepository;
    @Autowired
    EntityManager entityManager;
    @Autowired
    TravelPackageMapper travelPackageMapper;

    /**
     * @param updateDestination
     * @return
     */
    @Override
    public BaseResponse updateDestination(UpdateDestination updateDestination) {
        return null;
    }

    /**
     * @param createDestination
     * @return
     */
    @Override
    public BaseResponse createDestination(CreateDestination createDestination) {
        try {
            DestinationEntity destinationEntity = travelPackageMapper.mapToDestinationEntity(createDestination);
            DestinationEntity saved = destinationRepository.save(destinationEntity);
            return BaseResponseUtil.createBaseResponse(new TravelPackageResponse(saved), StatusCode.OK);
        } catch (Exception e) {
            return BaseResponseUtil.createErrorBaseResponse(e.getMessage());
        }
    }

    /**
     * @param destinationFilterRequest
     * @return
     */
    @Override
    public BaseResponse filterDestination(DestinationFilterRequest destinationFilterRequest) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

            // Count query
            CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
            Root<DestinationEntity> countRoot = countQuery.from(DestinationEntity.class);

            List<Predicate> countPredicates = buildPredicates(destinationFilterRequest, countRoot, criteriaBuilder);
            countQuery.select(criteriaBuilder.count(countRoot));
            countQuery.where(countPredicates.toArray(new Predicate[0]));

            // Execute the count query
            long totalResults = entityManager.createQuery(countQuery).getSingleResult();

            // Data query
            CriteriaQuery<DestinationEntity> dataQuery = criteriaBuilder.createQuery(DestinationEntity.class);
            Root<DestinationEntity> dataRoot = dataQuery.from(DestinationEntity.class);

            List<Predicate> dataPredicates = buildPredicates(destinationFilterRequest, dataRoot, criteriaBuilder);
            dataQuery.where(dataPredicates.toArray(new Predicate[0]));

            if (Objects.equals(SortOrder.ASCENDING, destinationFilterRequest.getSortByCreatedDate())) {
                Order updated = criteriaBuilder.asc(dataRoot.get("created"));
                dataQuery.orderBy(updated);
            }
            if (Objects.equals(SortOrder.DESCENDING, destinationFilterRequest.getSortByCreatedDate())) {
                Order updated = criteriaBuilder.desc(dataRoot.get("created"));
                dataQuery.orderBy(updated);
            }

            if (Objects.equals(SortOrder.ASCENDING, destinationFilterRequest.getSortByUpdatedDate())) {
                Order updated = criteriaBuilder.asc(dataRoot.get("updated"));
                dataQuery.orderBy(updated);
            }
            if (Objects.equals(SortOrder.DESCENDING, destinationFilterRequest.getSortByUpdatedDate())) {
                Order updated = criteriaBuilder.desc(dataRoot.get("updated"));
                dataQuery.orderBy(updated);
            }

            // Execute the data query
            List resultList = entityManager.createQuery(dataQuery)
                    .setFirstResult((destinationFilterRequest.getPage() - 1) * destinationFilterRequest.getPageSize())
                    .setMaxResults(destinationFilterRequest.getPageSize())
                    .getResultList();

            long totalPages = (totalResults + destinationFilterRequest.getPageSize() - 1) / destinationFilterRequest.getPageSize(); // Calculate total pages
            long remainingPages = Math.max(0, totalPages - destinationFilterRequest.getPage()); // Calculate remaining pages
            FilterResponse filterResponse = new FilterResponse<>(totalResults, remainingPages, destinationFilterRequest.getPage(), resultList.size(), resultList);
            return BaseResponseUtil.createBaseResponse(filterResponse, StatusCode.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponseUtil.createErrorBaseResponse(e.getMessage());
        }
    }

    private List<Predicate> buildPredicates(DestinationFilterRequest filterRequest, Root<DestinationEntity> root, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(filterRequest.getActivityId())) {
            predicates.add(root.get("activityId").in(filterRequest.getActivityId()));
        }

        if (CollectionUtils.isNotEmpty(filterRequest.getTravelPackageId())) {
            predicates.add(root.get("travelPackageId").in(filterRequest.getTravelPackageId()));
        }

        if (CollectionUtils.isNotEmpty(filterRequest.getDestinationId())) {
            predicates.add(root.get("destinationId").in(filterRequest.getDestinationId()));
        }
        Join<DestinationEntity, ActivityEntity> activityJoin = root.join("activities", JoinType.INNER);

        if (CollectionUtils.isNotEmpty(filterRequest.getActivityId())) {
            predicates.add(activityJoin.get("activityId").in(filterRequest.getActivityId()));
        }

        if (CollectionUtils.isNotEmpty(filterRequest.getDestinationDescription())) {
            predicates.add(root.get("destinationDescription").in(filterRequest.getDestinationDescription()));
        }

        if (CollectionUtils.isNotEmpty(filterRequest.getActivityName())) {
            predicates.add(activityJoin.get("activityName").in(filterRequest.getActivityName()));
        }

        if (CollectionUtils.isNotEmpty(filterRequest.getActivityDescription())) {
            predicates.add(activityJoin.get("activityDescription").in(filterRequest.getActivityDescription()));
        }

        if (filterRequest.getActivityCost() != null) {
            predicates.add(activityJoin.get("activityCost").in(filterRequest.getActivityCost()));
        }

        if (filterRequest.getActivityCapacity() != null) {
            predicates.add(activityJoin.get("activityCapacity").in(filterRequest.getActivityCapacity()));
        }

        if (filterRequest.getConsumedActivityCapacity() != null) {
            predicates.add(activityJoin.get("consumedActivityCapacity").in(filterRequest.getConsumedActivityCapacity()));
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
     * @param destinationId
     * @return
     */
    @Override
    public BaseResponse getDestinationById(String destinationId) {
        try {
            DestinationEntity destinationEntity = destinationRepository.getDestinationEntityByDestinationId(destinationId);
            if (ObjectUtils.isEmpty(destinationEntity)) {
                return BaseResponseUtil.createBaseResponse(StatusCode.DESTINATION_NOT_FOUND);
            }
            return BaseResponseUtil.createBaseResponse(new TravelPackageResponse(destinationEntity), StatusCode.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponseUtil.createErrorBaseResponse(e.getMessage());
        }
    }
}
