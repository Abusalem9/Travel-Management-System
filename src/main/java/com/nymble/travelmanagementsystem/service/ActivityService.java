package com.nymble.travelmanagementsystem.service;

import com.nymble.travelmanagementsystem.request.ActivityFilterRequest;
import com.nymble.travelmanagementsystem.request.CreateActivityV1;
import com.nymble.travelmanagementsystem.request.UpdateActivity;
import com.nymble.travelmanagementsystem.response.BaseResponse;

public interface ActivityService {
    BaseResponse createActivity(CreateActivityV1 createActivity);

    BaseResponse updateActivity(UpdateActivity updateActivity);

    BaseResponse filterActivity(ActivityFilterRequest activityFilterRequest);

    BaseResponse getActivityById(String activityId);
}
