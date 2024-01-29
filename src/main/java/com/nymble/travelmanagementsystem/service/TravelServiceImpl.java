package com.nymble.travelmanagementsystem.service;

import com.nymble.travelmanagementsystem.entity.DestinationEntity;
import com.nymble.travelmanagementsystem.entity.TravelPackageEntity;
import com.nymble.travelmanagementsystem.mapper.TravelPackageMapper;
import com.nymble.travelmanagementsystem.repository.TravelPackageRepository;
import com.nymble.travelmanagementsystem.request.CreateTravelPackage;
import com.nymble.travelmanagementsystem.request.TravelPackageFilterRequest;
import com.nymble.travelmanagementsystem.response.BaseResponse;
import com.nymble.travelmanagementsystem.response.TravelPackageResponse;
import com.nymble.travelmanagementsystem.utils.BaseResponseUtil;
import com.nymble.travelmanagementsystem.utils.StatusCode;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TravelServiceImpl implements TravelService {

    @Autowired
    TravelPackageRepository travelPackageRepository;
    @Autowired
    TravelPackageMapper travelPackageMapper;

    /**
     * @param createTravelPackage
     * @return
     */
    @Override
    public BaseResponse createTravelPackage(CreateTravelPackage createTravelPackage) {
        try {
            TravelPackageEntity travelPackageEntity = travelPackageMapper.createTravelPackageModelToTravelPackageEntity(createTravelPackage);
            List<DestinationEntity> destinationEntities = travelPackageMapper.createdDestinationModelListToCreateDestinationEntityList(createTravelPackage.getCreateDestinations());
            travelPackageEntity.setDestinations(destinationEntities);
            TravelPackageEntity savedTravelPackage = travelPackageRepository.save(travelPackageEntity);
            return BaseResponseUtil.createBaseResponse(new TravelPackageResponse(savedTravelPackage), StatusCode.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponseUtil.createErrorBaseResponse(e.getMessage());
        }
    }

    /**
     * @param travelPackageFilterRequest
     * @return
     */
    @Override
    public BaseResponse filterTravelPackage(TravelPackageFilterRequest travelPackageFilterRequest) {
        try {
            return BaseResponseUtil.createBaseResponse(StatusCode.OK);
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
    public BaseResponse getTravelPackageById(String travelPackageId) {
        try {
            TravelPackageEntity retrievedTravelPackageEntity = travelPackageRepository.getTravelPackageEntityByTravelPackageId(travelPackageId);
            if (ObjectUtils.isEmpty(retrievedTravelPackageEntity)) {
                return BaseResponseUtil.createBaseResponse(StatusCode.NO_DATA);
            }
            return BaseResponseUtil.createBaseResponse(new TravelPackageResponse(retrievedTravelPackageEntity), StatusCode.OK);
        } catch (Exception e) {
            return BaseResponseUtil.createErrorBaseResponse(e.getMessage());
        }
    }
}
