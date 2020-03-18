package com.ihrm.system.controller;

import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.exception.CommonException;
import com.ihrm.domain.system.Permission;
import com.ihrm.system.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
//设置父路径
@RequestMapping(value = "/sys")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;
    /**
     * 保存权限
     * */
    @RequestMapping(value = "/permission",method = RequestMethod.POST)
    public Result save(@RequestBody Map<String,Object> map) throws Exception {
        permissionService.save(map);
        return new Result(ResultCode.SUCCESS);
    }
    /**
     * 根据id修改权限
     * */
    @RequestMapping(value = "/permission/{id}",method = RequestMethod.PUT)
    public Result update(@PathVariable String id,@RequestBody  Map<String,Object> map)throws Exception{
        //构造 id
        map.put("id",id);
        permissionService.update(map);
        return new Result(ResultCode.SUCCESS);
    }
    /**
     * 查询权限列表
     * */
    @RequestMapping(value = "/permission",method = RequestMethod.GET)
    public Result findAll(@RequestParam Map map){
      List<Permission> list = permissionService.findAll(map);
        return new Result(ResultCode.SUCCESS,list);
    }
    /**
     * 根据id查询权限
     * */
    @RequestMapping(value = "/permission/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable String id) throws Exception {
        Map<String, Object> map = permissionService.findById(id);
        return new Result(ResultCode.SUCCESS,map);
    }
    /**
     * 根据id删除权限
     * */
    @RequestMapping(value = "/permission/{id}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) throws CommonException {
         permissionService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }
}
