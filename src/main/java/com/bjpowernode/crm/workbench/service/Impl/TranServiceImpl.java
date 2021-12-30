package com.bjpowernode.crm.workbench.service.Impl;

import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.CustomerDao;
import com.bjpowernode.crm.workbench.dao.TranDao;
import com.bjpowernode.crm.workbench.dao.TranHistoryDao;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.TranService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TranServiceImpl implements TranService {

    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);


    @Override
    public boolean save(Tran tran, String customerName) {

        boolean flag = true;

        Customer customer = customerDao.getCustomerByName(customerName);

        if (customer == null){

            customer = new Customer();
            customer.setContactSummary(tran.getContactSummary());
            customer.setId(UUIDUtil.getUUID());
            customer.setNextContactTime(tran.getNextContactTime());
            customer.setOwner(tran.getOwner());
            customer.setName(customerName);
            customer.setDescription(tran.getDescription());
            customer.setCreateTime(tran.getCreateTime());
            customer.setCreateBy(tran.getCreateBy());
            customer.setContactSummary(tran.getContactSummary());

            int count = customerDao.save(customer);

            if (count != 1) {

                flag = false;

            }

        }

        tran.setCustomerId(customer.getId());
        int count1 = tranDao.save(tran);

        if (count1 != 1) {

            flag = false;

        }

        if (flag){

            TranHistory th = new TranHistory();

            th.setTranId(tran.getId());
            th.setStage(tran.getStage());
            th.setMoney(tran.getMoney());
            th.setExpectedDate(tran.getExpectedDate());
            th.setId(UUIDUtil.getUUID());
            th.setCreateTime(tran.getCreateTime());
            th.setCreateBy(tran.getCreateBy());

            tranHistoryDao.save(th);

        }

        return flag;
    }

    @Override
    public Tran detail(String id) {

        Tran t = tranDao.detail(id);

        return t;
    }

    @Override
    public Map<String, Object> echarts() {

        int total = tranDao.getTotal();

        List<Map<String,String>> dataList = tranDao.getCharts();

        Map<String,Object> map = new HashMap<>();

        map.put("total",total);
        map.put("dataList",dataList);

        return map;
    }
}
