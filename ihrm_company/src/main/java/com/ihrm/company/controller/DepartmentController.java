package com.ihrm.company.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.company.service.CompanyService;
import com.ihrm.company.service.DepartmentService;
import com.ihrm.domain.company.Company;
import com.ihrm.domain.company.Department;
import com.ihrm.domain.company.response.DeptListResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//解决跨域
@CrossOrigin
@RestController
//设置父路径
@RequestMapping(value = "/company")
public class DepartmentController extends BaseController {
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private CompanyService companyService;

    /**
     * 保存部门
     * */
    @RequestMapping(value = "/department",method = RequestMethod.POST)
    public Result save(@RequestBody Department department){
        department.setCompanyId(companyId);
        //调用service保存
        departmentService.save(department);
        //构造返回结果
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 查询企业的部门列表
     * 指定企业id
     * */
    @RequestMapping(value = "/department",method = RequestMethod.GET)
    public Result findAll(){
        System.out.println(companyId);
        //根据企业id查询企业
        Company company = companyService.findById(companyId);

        //完成查询
        List<Department> list = departmentService.findAll(companyId);
        //构造返回结果
        DeptListResult deptListResult = new DeptListResult(company,list);
        System.out.println(deptListResult);
        return new Result(ResultCode.SUCCESS,deptListResult);
    }

    /**
     * 根据id查询部门
     * */
    @RequestMapping(value = "/department/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable String id){
        Department department = departmentService.findById(id);
        return new Result(ResultCode.SUCCESS,department);
    }

    /**
     * 根据id修改部门
     * */
    @RequestMapping(value = "/department/{id}",method = RequestMethod.PUT)
    public Result update(@PathVariable String id,@RequestBody Department department){
        //1.设置部门id
        department.setId(id);
        departmentService.update(department);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据id删除部门
     * */
    @RequestMapping(value = "/department/{id}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id){
        departmentService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }



    @RequestMapping(value = "/department/search",method = RequestMethod.POST)
    public Department findByCode(@RequestParam(value = "code") String code,@RequestParam(value = "company_id")String company_id){
         Department department=departmentService.findByCode(code,company_id);
        return department;
    }




}
