package com.bjpowernode.crm.workbench.web.controller;


import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.Impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            System.out.println("进入到市场活动控制台");
            String path=request.getServletPath();
            if ("/workbench/activity/getUserList.do".equals(path)){

                getUserList(request,response);

            }else if ("/workbench/activity/save.do".equals(path)){

                save(request,response);

            }else if ("/workbench/activity/pageList.do".equals(path)){

                pageList(request,response);

            }else if ("/workbench/activity/delete.do".equals(path)){

                delete(request,response);

            }else if ("/workbench/activity/getUserListAndActivity.do".equals(path)){

                getUserListAndActivity(request,response);

            }
    }

    private void getUserListAndActivity(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("获取要修改的uList和a");

        String id = request.getParameter("id");

        ActivityService as =(ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        /*

            总结：
                controller调用service的方法，返回值已改是什么？
                    前端要什么，就从service层取什么

                前端需要的，关业务层去要
                uList
                a

                以上的两项信息，复用率不高，我们选择使用map打包这两项信息即可
                map

         */

        Map<String,Object> map = as.getUserListAndActivity(id);

        PrintJson.printJsonObj(response,map);

    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行市场活动那个的删除操作");

        String[] ids = request.getParameterValues("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.delete(ids);

        PrintJson.printJsonFlag(response,flag);

    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到查询市场活动信息列表的操作（结合条件查询+分页查询）");


        String name = request.getParameter("name");
        String owner  = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");


        String pageNoStr = request.getParameter("pageNo");

        String pageSizeStr = request.getParameter("pageSize");

        //每页展现的记录数
        int pageSize = Integer.valueOf(pageSizeStr);
        int pageNo = Integer.valueOf(pageNoStr);

        //计算出略过的记录数
        int skipCount = (pageNo-1)*pageSize;

        Map<String,Object> map = new HashMap<>();

        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);


        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        /*

            前端要：
                市场活动信息列表
                查询的总条数

                业务层拿到了以上的两项信息之后，如果做返回
                map

                map.put("total":total);
                map.put("dataList":dataList);
                PrintJSON map ---> json
                {"total":100,"dataList":[{市场活动1},...]}


                vo

                PaginationVO<T>
                    private Integer total;
                    private List<T> dataList;

                PaginationVO<Activity> vo = new PaginationVO<>;
                vo.setTotal(total);
                vo.setDataList(dataList);
                PrintJSON vo ---> json
                {"total":100,"dataList":[{市场活动1},...]}

                将来分页查询，每个模块都有，所以我们选择使用一个通用vo，操作起来比较方便

         */


        PaginationVO<Activity> vo = as.pageList(map);

        //vo--->{"total":100,"dataList":[{市场活动1},...]}
        PrintJson.printJsonObj(response,vo);

    }

    private void save(HttpServletRequest request, HttpServletResponse response) {

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        //创建时间：当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        //创建人：当前登录用户
        String createBy = ((User)request.getSession().getAttribute("user")).getName();

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Activity a = new Activity();
        a.setId(id);
        a.setOwner(owner);
        a.setName(name);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setCost(cost);
        a.setDescription(description);
        a.setCreateTime(createTime);
        a.setCreateBy(createBy);

        boolean flag = as.save(a);

        PrintJson.printJsonFlag(response,flag);

    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("取得用户信息列表");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> uList = us.getUserList();

        PrintJson.printJsonObj(response,uList);
    }
}
