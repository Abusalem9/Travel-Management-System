package com.nymble.travelmanagementsystem.service;

import com.nymble.travelmanagementsystem.entity.ActivityEntity;
import com.nymble.travelmanagementsystem.entity.BookingEntity;
import com.nymble.travelmanagementsystem.entity.TravelPackageEntity;
import com.nymble.travelmanagementsystem.mapper.TravelPackageMapper;
import com.nymble.travelmanagementsystem.repository.TravelPackageRepository;
import com.nymble.travelmanagementsystem.response.*;
import com.nymble.travelmanagementsystem.utils.BaseResponseUtil;
import com.nymble.travelmanagementsystem.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {

    @Autowired
    TravelPackageRepository travelPackageRepository;

    @Autowired
    TravelPackageMapper travelPackageMapper;

    @Autowired
    EntityManager entityManager;

    private static Task4Response getTask4Response(ActivityEntity activity) {
        long availableCapacity = activity.getActivityCapacity() - activity.getConsumedActivityCapacity();
        Task4Response task4Response = new Task4Response();
        task4Response.setAvailableActivityCapacity(availableCapacity);
        task4Response.setActivityCapacity(activity.getActivityCapacity());
        task4Response.setActivityName(activity.getActivityName());
        task4Response.setConsumedActivityCapacity(activity.getConsumedActivityCapacity());
        task4Response.setActivityDescription(activity.getActivityDescription());
        return task4Response;
    }

    /**
     * @param travelPackageId
     * @return
     */
    @Override
    public BaseResponse retrieveOutputOfTheFirstTask(String travelPackageId) {
        try {
            TravelPackageEntity travelPackageEntity = travelPackageRepository.getTravelPackageEntityByTravelPackageId(travelPackageId);
            if (ObjectUtils.isEmpty(travelPackageEntity)) {
                return BaseResponseUtil.createBaseResponse(StatusCode.NO_DATA);
            }
            Task1Response task1Response = travelPackageMapper.travelPackageEntityToTask1Response(travelPackageEntity);
            return BaseResponseUtil.createBaseResponse(new TravelPackageResponse(task1Response), StatusCode.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponseUtil.createErrorBaseResponse(e.getMessage());
        }
    }

    /**
     * @param travelPackageId
     * @return
     */
    @Override
    public BaseResponse retrieveOutputOfTheSecondTask(String travelPackageId) {
        try {
            TravelPackageEntity travelPackageEntity = travelPackageRepository.getTravelPackageEntityByTravelPackageId(travelPackageId);
            if (ObjectUtils.isEmpty(travelPackageEntity)) {
                return BaseResponseUtil.createBaseResponse(StatusCode.NO_DATA);
            }
            Task2Response task1Response = travelPackageMapper.travelPackageEntityToTask2Response(travelPackageEntity);
            return BaseResponseUtil.createBaseResponse(new TravelPackageResponse(task1Response), StatusCode.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponseUtil.createErrorBaseResponse(e.getMessage());
        }
    }

    /**
     * @param param
     * @return
     */
    @Override
    public BaseResponse retrieveOutputOfTheThirdTask(String param) {
        try {
            Query query = entityManager.createQuery("select a,b,d.destinationName,d.destinationId,d.destinationDescription,p.passengerId,p.passengerNumber,p.passengerName,p.passengerBalance from PassengerEntity as p left join BookingEntity b on p.passengerId = b.passengerId left join ActivityEntity  as a on a.activityId = b.activityId left join DestinationEntity as d on d.destinationId = a.destinationId where p.passengerId =:param or p.passengerNumber=:param group by a.activityId,d.destinationId,p.passengerId,b.bookingId");
            query.setParameter("param", param);
            List resultList = query.getResultList();

            // Processing the result
            Map<String, Task3Response> responseMap = new HashMap<>();

            for (Object result : resultList) {
                Object[] row = (Object[]) result;

                String passengerNumber = (String) row[6];

                Task3Response task3Response = responseMap.computeIfAbsent(passengerNumber, k -> {
                    Task3Response newResponse = new Task3Response();
                    newResponse.setPassengerNumber(passengerNumber);
                    newResponse.setPassengerId((String) row[5]);
                    newResponse.setPassengerName((String) row[7]);
                    newResponse.setPassengerBalance((BigDecimal) row[8]);
                    return newResponse;
                });

                Task3ActivityResponse task3ActivityResponse = new Task3ActivityResponse();
                ActivityEntity activityEntity = (ActivityEntity) row[0];
                log.info(activityEntity + "");
                task3ActivityResponse.setActivityId(activityEntity.getActivityId());
                task3ActivityResponse.setActivityDescription(activityEntity.getActivityDescription());
                task3ActivityResponse.setActivityName(activityEntity.getActivityName());

                BookingEntity bookingEntity = (BookingEntity) row[1];
                task3ActivityResponse.setBookingCost(bookingEntity.getBookingCost());
                task3ActivityResponse.setDestinationName((String) row[2]);
                task3ActivityResponse.setDestinationId((String) row[3]);
                if (CollectionUtils.isEmpty(task3Response.getActivities())) {
                    task3Response.setActivities(new ArrayList<>());
                }
                task3Response.getActivities().add(task3ActivityResponse);
            }

            List<Task3Response> updatedResponses = new ArrayList<>(responseMap.values());

            return BaseResponseUtil.createBaseResponse(new TravelPackageResponse(updatedResponses), StatusCode.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponseUtil.createErrorBaseResponse(e.getMessage());
        }
    }

    /**
     * @param travelPackageId
     * @return
     */
    @Override
    public BaseResponse retrieveOutputOfTheFourthTask(String travelPackageId) {
        try {
            Query query = entityManager.createQuery("SELECT a FROM ActivityEntity AS a LEFT JOIN DestinationEntity AS d ON a.destinationId = d.destinationId JOIN TravelPackageEntity AS tp ON d.travelPackageId = tp.travelPackageId WHERE tp.travelPackageId =:travelPackageId and a.consumedActivityCapacity < a.activityCapacity");
            query.setParameter("travelPackageId", travelPackageId);
            List<ActivityEntity> resultList = (List<ActivityEntity>) query.getResultList();
            List<Task4Response> task4Responses = new ArrayList<>();
            for (ActivityEntity activity : resultList) {
                Task4Response task4Response = getTask4Response(activity);
                task4Responses.add(task4Response);
            }
            return BaseResponseUtil.createBaseResponse(new TravelPackageResponse(task4Responses), StatusCode.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponseUtil.createErrorBaseResponse(e.getMessage());
        }
    }
}
