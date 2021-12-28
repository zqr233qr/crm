package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {

    int save(Activity a);

    Integer getTotalByCondition(Map<String, Object> map);

    List<Activity> getActivityListByCondition(Map<String, Object> map);

    int delete(String[] ids);

    Activity getById(String id);

    int update(Activity a);

    Activity detail(String id);

    List<Activity> getActivityByClue(String clueId);

    List<Activity> getActivityListByNameNotClueId(Map<String, String> map);

    List<Activity> getActivityListByName(String name);

}
