package com.nymble.travelmanagementsystem.service;

import com.nymble.travelmanagementsystem.response.BaseResponse;

public interface TaskService {
    BaseResponse retrieveOutputOfTheFirstTask(String travelPackageId);

    BaseResponse retrieveOutputOfTheSecondTask(String travelPackageId);

    BaseResponse retrieveOutputOfTheThirdTask(String travelPackageId);

    BaseResponse retrieveOutputOfTheFourthTask(String travelPackageId);
}
