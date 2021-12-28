package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.ClueRemark;
import com.bjpowernode.crm.workbench.domain.CustomerRemark;

import java.util.List;

public interface CustomerRemarkDao {

    List<ClueRemark> getListByClueId(String clueId);

    int save(CustomerRemark customerRemark);
}
