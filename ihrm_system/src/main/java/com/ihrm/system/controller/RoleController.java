package com.ihrm.system.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.PageResult;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.domain.system.Role;
import com.ihrm.domain.system.response.RoleResult;
import com.ihrm.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@CrossOrigin
@RestController
//设置父路径
@RequestMapping(value = "/sys")
public class RoleController extends BaseController {
    @Autowired
    private RoleService roleService;
    /**
     *分配权限
     * */
    @RequestMapping(value = "/role/assignPrem",method = RequestMethod.PUT)
    public Result save(@RequestBody Map<String,Object> map){
        //1.获取别分配的用户id
        String roleId = (String)map.get("roleId");
        //2.获取到角色的id列表
        List<String> permIds = (List<String>)map.get("ids");
        //3.调用service完成角色分配
        roleService.assignPerms(roleId,permIds);
        return new Result(ResultCode.SUCCESS);
    }
    /**
     * 保存角色
     * */
    @RequestMapping(value = "/role",method = RequestMethod.POST)
    public Result save(@RequestBody Role role){
        //设置保存企业id
        role.setCompanyId(companyId);
        //调用service保存
        roleService.save(role);
        //构造返回结果
        return new Result(ResultCode.SUCCESS);
    }
    /**
     * 查询企业的角色
     * 指定企业id
     * */
    @RequestMapping(value = "/role",method = RequestMethod.GET)
    public Result findAll(int page, int pagesize){
        //获取 企业当前id
        Page<Role> rolePage = roleService.findAll(page, pagesize,companyId);
        PageResult<Role> pageResult= new PageResult<Role>( rolePage.getTotalElements(),rolePage.getContent());
        return new Result(ResultCode.SUCCESS,pageResult);
    }
    /**
     * 根据id查询角色
     * */
    @RequestMapping(value = "/role/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable String id){
        Role role= roleService.findById(id);
        RoleResult roleResult = new RoleResult(role);
        return new Result(ResultCode.SUCCESS,roleResult);
    }
    /**
     * 查询角色列表
     * */
    @RequestMapping(value = "/role/list",method = RequestMethod.GET)
    public Result findByUser(){
        List<Role> roleList = roleService.findRoleList(companyId);
        return new Result(ResultCode.SUCCESS,roleList);
    }
    /**
     * 根据id修改角色
     * */
    @RequestMapping(value = "/role/{id}",method = RequestMethod.PUT)
    public Result update(@PathVariable String id,@RequestBody Role role){
        //1.设置部门id
        role.setId(id);
        roleService.update(role);
        return new Result(ResultCode.SUCCESS);
    }
    /**
     * 根据id删除角色
     * */
    @RequestMapping(value = "/role/{id}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id){
        roleService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }
}
