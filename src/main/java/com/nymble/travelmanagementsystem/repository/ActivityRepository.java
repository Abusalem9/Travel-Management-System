package com.nymble.travelmanagementsystem.repository;

import com.nymble.travelmanagementsystem.entity.ActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<ActivityEntity, String> {
    ActivityEntity getActivityEntityByActivityId(String activityId);

    List<ActivityEntity> getActivityEntitiesByActivityIdIn(Collection<String> activityId);
}
