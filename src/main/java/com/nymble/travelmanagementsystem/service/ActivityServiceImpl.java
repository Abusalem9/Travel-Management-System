package com.nymble.travelmanagementsystem.service;

import com.nymble.travelmanagementsystem.entity.ActivityEntity;
import com.nymble.travelmanagementsystem.entity.DestinationEntity;
import com.nymble.travelmanagementsystem.mapper.TravelPackageMapper;
import com.nymble.travelmanagementsystem.repository.ActivityRepository;
import com.nymble.travelmanagementsystem.repository.DestinationRepository;
import com.nymble.travelmanagementsystem.request.ActivityFilterRequest;
import com.nymble.travelmanagementsystem.request.CreateActivityV1;
import com.nymble.travelmanagementsystem.request.UpdateActivity;
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
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    ActivityRepository activityRepository;
    @Autowired
    DestinationRepository destinationRepository;
    @Autowired
    TravelPackageMapper travelPackageMapper;
    @Autowired
    EntityManager entityManager;

    /**
     * @param createActivity
     * @return
     */
    @Override
    public BaseResponse createActivity(CreateActivityV1 createActivity) {
        try {
            DestinationEntity destinationEntity = destinationRepository.getDestinationEntityByDestinationId(createActivity.getDestinationId());
            if (ObjectUtils.isEmpty(destinationEntity)) {
                return BaseResponseUtil.createBaseResponse(StatusCode.NO_DATA);
            }
            ActivityEntity activityEntity = travelPackageMapper.createActivityV1ModelToActivityEntity(createActivity);
            ActivityEntity saved = activityRepository.save(activityEntity);
            return BaseResponseUtil.createBaseResponse(new TravelPackageResponse(activityEntity), StatusCode.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponseUtil.createErrorBaseResponse(e.getMessage());
        }
    }

    /**
     * @param updateActivity
     * @return
     */
    @Override
    public BaseResponse updateActivity(UpdateActivity updateActivity) {
        try {
            ActivityEntity activityEntity = activityRepository.getActivityEntityByActivityId(updateActivity.getActivityId());
            if (ObjectUtils.isEmpty(activityEntity)) {
                return BaseResponseUtil.createBaseResponse(StatusCode.ACTIVITY_NOT_FOUND);
            }
            if (StringUtils.isNotEmpty(updateActivity.getDestinationId())) {
                activityEntity.setDestinationId(updateActivity.getDestinationId());
            }

            if (StringUtils.isNotEmpty(updateActivity.getActivityName())) {
                activityEntity.setActivityName(updateActivity.getActivityName());
            }

            if (StringUtils.isNotEmpty(updateActivity.getActivityDescription())) {
                activityEntity.setActivityDescription(updateActivity.getActivityDescription());
            }

            if (updateActivity.getActivityCost() != null) {
                activityEntity.setActivityCost(updateActivity.getActivityCost());
            }

            if (updateActivity.getActivityCapacity() != null) {
                activityEntity.setActivityCapacity(updateActivity.getActivityCapacity());
            }

            if (updateActivity.getConsumedActivityCapacity() != null) {
                activityEntity.setConsumedActivityCapacity(updateActivity.getConsumedActivityCapacity());
            }

            if (StringUtils.isNotEmpty(updateActivity.getUpdatedBy())) {
                activityEntity.setUpdatedBy(updateActivity.getUpdatedBy());
            }

            if (StringUtils.isNotEmpty(updateActivity.getUpdatedByName())) {
                activityEntity.setUpdatedByName(updateActivity.getUpdatedByName());
            }
            ActivityEntity updatedActivity = activityRepository.save(activityEntity);
            return BaseResponseUtil.createBaseResponse(new TravelPackageResponse(updatedActivity), StatusCode.OK);
        } catch (Exception e) {
            return BaseResponseUtil.createErrorBaseResponse(e.getMessage());
        }
    }

    /**
     * @param activityFilterRequest
     * @return
     */
    @Override
    public BaseResponse filterActivity(ActivityFilterRequest activityFilterRequest) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

            // Count query
            CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
            Root<ActivityEntity> countRoot = countQuery.from(ActivityEntity.class);

            List<Predicate> countPredicates = buildPredicates(activityFilterRequest, countRoot, criteriaBuilder);
            countQuery.select(criteriaBuilder.count(countRoot));
            countQuery.where(countPredicates.toArray(new Predicate[0]));

            // Execute the count query
            long totalResults = entityManager.createQuery(countQuery).getSingleResult();

            // Data query
            CriteriaQuery<ActivityEntity> dataQuery = criteriaBuilder.createQuery(ActivityEntity.class);
            Root<ActivityEntity> dataRoot = dataQuery.from(ActivityEntity.class);

            List<Predicate> dataPredicates = buildPredicates(activityFilterRequest, dataRoot, criteriaBuilder);
            dataQuery.where(dataPredicates.toArray(new Predicate[0]));

            if (Objects.equals(SortOrder.ASCENDING, activityFilterRequest.getSortByCreatedDate())) {
                Order updated = criteriaBuilder.asc(dataRoot.get("created"));
                dataQuery.orderBy(updated);
            }
            if (Objects.equals(SortOrder.DESCENDING, activityFilterRequest.getSortByCreatedDate())) {
                Order updated = criteriaBuilder.desc(dataRoot.get("created"));
                dataQuery.orderBy(updated);
            }

            if (Objects.equals(SortOrder.ASCENDING, activityFilterRequest.getSortByUpdatedDate())) {
                Order updated = criteriaBuilder.asc(dataRoot.get("updated"));
                dataQuery.orderBy(updated);
            }
            if (Objects.equals(SortOrder.DESCENDING, activityFilterRequest.getSortByUpdatedDate())) {
                Order updated = criteriaBuilder.desc(dataRoot.get("updated"));
                dataQuery.orderBy(updated);
            }

            // Execute the data query
            List resultList = entityManager.createQuery(dataQuery)
                    .setFirstResult((activityFilterRequest.getPage() - 1) * activityFilterRequest.getPageSize())
                    .setMaxResults(activityFilterRequest.getPageSize())
                    .getResultList();

            long totalPages = (totalResults + activityFilterRequest.getPageSize() - 1) / activityFilterRequest.getPageSize(); // Calculate total pages
            long remainingPages = Math.max(0, totalPages - activityFilterRequest.getPage()); // Calculate remaining pages
            FilterResponse filterResponse = new FilterResponse<>(totalResults, remainingPages, activityFilterRequest.getPage(), resultList.size(), resultList);
            return BaseResponseUtil.createBaseResponse(filterResponse, StatusCode.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponseUtil.createErrorBaseResponse(e.getMessage());
        }
    }

    private List<Predicate> buildPredicates(ActivityFilterRequest filterRequest, Root<ActivityEntity> root, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(filterRequest.getActivityId())) {
            predicates.add(root.get("activityId").in(filterRequest.getActivityId()));
        }

        if (CollectionUtils.isNotEmpty(filterRequest.getDestinationId())) {
            predicates.add(root.get("destinationId").in(filterRequest.getDestinationId()));
        }

        if (CollectionUtils.isNotEmpty(filterRequest.getActivityName())) {
            predicates.add(root.get("activityName").in(filterRequest.getActivityName()));
        }

        if (CollectionUtils.isNotEmpty(filterRequest.getActivityDescription())) {
            predicates.add(root.get("activityDescription").in(filterRequest.getActivityDescription()));
        }

        if (filterRequest.getActivityCost() != null) {
            predicates.add(criteriaBuilder.equal(root.get("activityCost"), filterRequest.getActivityCost()));
        }

        if (filterRequest.getActivityCapacity() != null) {
            predicates.add(criteriaBuilder.equal(root.get("activityCapacity"), filterRequest.getActivityCapacity()));
        }

        if (filterRequest.getConsumedActivityCapacity() != null) {
            predicates.add(criteriaBuilder.equal(root.get("consumedActivityCapacity"), filterRequest.getConsumedActivityCapacity()));
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
     * @param activityId
     * @return
     */
    @Override
    public BaseResponse getActivityById(String activityId) {
        try {
            ActivityEntity activityEntity = activityRepository.getActivityEntityByActivityId(activityId);
            if (ObjectUtils.isEmpty(activityEntity)) {
                return BaseResponseUtil.createBaseResponse(StatusCode.ACTIVITY_NOT_FOUND);
            }
            return BaseResponseUtil.createBaseResponse(new TravelPackageResponse(activityEntity), StatusCode.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponseUtil.createErrorBaseResponse(e.getMessage());
        }
    }
}
