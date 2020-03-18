package com.ihrm.system.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.PageResult;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.poi.ExcelImportUtil;
import com.ihrm.common.util.JwtUtils;
import com.ihrm.domain.system.response.ProfileResult;
import com.ihrm.domain.system.User;
import com.ihrm.domain.system.response.UserResult;
import com.ihrm.system.client.DepartmentFeignClient;
import com.ihrm.system.service.PermissionService;
import com.ihrm.system.service.UserService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
//设置父路径
@RequestMapping(value = "/sys")
public class UserController  extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private DepartmentFeignClient departmentFeignClient;

    /**
     * 用base64的方法保存头像
    * */
    @RequestMapping(value = "/user/upload/{id}",method = RequestMethod.POST)
    public Result upload(@PathVariable String id,@RequestParam(name = "file")MultipartFile file) throws IOException {
       //1.调用service保存图片（获取图片的访问地址（dataUrl|http地址））
        String imgUrl= userService.uploadImage(id,file);
       //2.返回数据
        return new Result(ResultCode.SUCCESS,imgUrl);
    }





    /**
     * 导入excel 添加用户
     * 文件上传
     * */
    @RequestMapping(value = "/user/import",method = RequestMethod.POST)
    public Result importUser(@RequestParam(name = "file")MultipartFile file) throws IOException {
     /*   //1.解析excel
        //1.1.根据excel文件创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        //1.2.获取sheet
        XSSFSheet sheet = workbook.getSheetAt(0);//参数 索引
        //1.3.获取sheet中的每一行和每一个单元格
        //2.获取用户数据
        List<User> list=new ArrayList<>();
        for (int rowNum=1;rowNum<=sheet.getLastRowNum();rowNum++){
            Row row = sheet.getRow(rowNum);//根据索引获取每一行
            Object[] values=new Object[(row.getLastCellNum())];
            for (int cellNum=1;cellNum<row.getLastCellNum();cellNum++){
                Cell cell = row.getCell(cellNum);
                Object value = getCellValue(cell);
                values[cellNum]=value;
            }
            User user = new User(values);
            list.add(user);
        }*/

        List<User> list = new ExcelImportUtil(User.class).readExcel(file.getInputStream(), 1, 1);
        userService.saveAll(list,companyId,companyName);
        return new Result(ResultCode.SUCCESS);
    }


/*
    public static Object getCellValue(Cell cell){
        //1.获取单元格属性
        CellType cellType = cell.getCellType();
        //2.根据单元格数据类型获取数据
        Object value=null;
        switch (cellType){
            case STRING:
                value=cell.getStringCellValue();
                break;
            case BOOLEAN:
                value=cell.getBooleanCellValue();
                break;
            case NUMERIC:
                //日期格式
                if (DateUtil.isCellDateFormatted(cell)){
                    value=cell.getDateCellValue();
                }else {
                    //数字
                    value=cell.getNumericCellValue();
                }
                break;
            case FORMULA://公式
                value=cell.getCellFormula();
                break;
            default:
                break;
        }
        return value;
    }
*/





    /**
     * 测试feign组件
     * 调用系统微服务的/tst接口传递部门id，通过feign调用部门微服务获取信息
     * */
    @RequestMapping(value = "/test/{id}",method = RequestMethod.GET)
    public Result testFeign(@PathVariable(value = "id")String id){
        Result result = departmentFeignClient.findById(id);
        return result;
    }

    /**
     * 分配角色
     * */
    @RequestMapping(value = "/user/assignRoles",method = RequestMethod.PUT)
    public Result save(@RequestBody Map<String,Object> map){
        for (Map.Entry<String,Object> entry : map.entrySet())
            {
                   System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
             }
        //1.获取别分配的用户id
        String userId = (String)map.get("id");
        //2.获取到角色的id列表
        List<String> roleIds = (List<String>)map.get("roleIds");
        //3.调用service完成角色分配
    userService.assignRoles(userId,roleIds);
        return new Result(ResultCode.SUCCESS);
    }
    /**
     * 保存部门
     * */
    @RequestMapping(value = "/user",method = RequestMethod.POST)
    public Result save(@RequestBody User user){
        //设置保存企业id
        user.setCompanyId(companyId);
        user.setCompanyName(companyName);
        //调用service保存
        userService.save(user);
        //构造返回结果
        return new Result(ResultCode.SUCCESS);
    }
    /**
     * 查询企业的用户
     * 指定企业id
     * */
    @RequestMapping(value = "/user",method = RequestMethod.GET)
    public Result findAll(int page, int size, @RequestParam Map map){
        //获取 企业当前id
        map.put("company_id",companyId);
        Page<User> pageUser = userService.findAll(map, page, size);
        PageResult<User> userPageResult = new PageResult<User>( pageUser.getTotalElements(),pageUser.getContent());
        return new Result(ResultCode.SUCCESS,userPageResult);
    }
    /**
     * 根据id查询部门
     * */
    @RequestMapping(value = "/user/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable(value = "id") String id){
        User user= userService.findById(id);
        //添加roleIds用户已经具有的role_ids数组
        UserResult userResult = new UserResult(user);
        return new Result(ResultCode.SUCCESS,userResult);
    }
    /**
     * 根据id修改部门
     * */
    @RequestMapping(value = "/user/{id}",method = RequestMethod.PUT)
    public Result update(@PathVariable(value = "id") String id,@RequestBody User user){
        //1.设置部门id
        user.setId(id);
        userService.update(user);
        return new Result(ResultCode.SUCCESS);
    }
    /**
     * 根据id删除部门
     * */
    @RequestMapping(value = "/user/{id}",method = RequestMethod.DELETE,name = "API-USER-DELETE")
    public Result delete(@PathVariable(value = "id") String id){
        userService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }
    /**
     * 用户登录
     * 1.通过service根据mobile查询用户
     * 2.比较password
     * 3.生成jwt信息
     * */
      //数据存放在请求体当中
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Result login(@RequestBody Map<String,String> loginMap){

        String mobile=loginMap.get("mobile");
        String password=loginMap.get("password");
        try{
            //1.构造登录令牌 UserNameAndPasswordToken
            //密码加密
            password = new Md5Hash(password,mobile,3).toString();//1.密码 ，盐  加密次数
            UsernamePasswordToken upToken = new UsernamePasswordToken(mobile, password);
            //2.获取subject
            Subject subject = SecurityUtils.getSubject();
            //3.调用login方法进入realm完成认证
            subject.login(upToken);
            //4.获取sessionId
            String sessionId = (String)subject.getSession().getId();
            //5.构造返回结果
            return new Result(ResultCode.SUCCESS,sessionId);
        }catch (Exception e){
            return new Result(ResultCode.MOBILEORPASSWORDERROR);
        }



        /**

        User user = userService.findByMobile(mobile);
        //登录成功
        if (user==null||!user.getPassword().equals(password)){
            return new Result(ResultCode.MOBILEORPASSWORDERROR);
        }else{
            //登陆成功  存放company_id和company_name
            //api授权字符串
            StringBuffer sb = new StringBuffer();
            //获取所有的可访问的API权限
            for (Role role:user.getRoles()){
                for (Permission perm:role.getPermissions()){
                    if (perm.getType()== PermissionConstants.PERMISSION_API){
                        sb.append(perm.getCode()).append(",");
                    }
                }
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("apis",sb);//可访问的api权限字符串
            map.put("company_id",user.getCompany_id());
            map.put("company_name",user.getCompany_name());
            String token = jwtUtils.createJwt(user.getId(), user.getUsername(), map);
            return new Result(ResultCode.SUCCESS,token);
         }
            */

    }
    /**
     * 用户登录成功之后获取用户信息
     * 1.获取用户id
     * 2.根据用户id查询用户
     * 3.构建返回对象
     * 4.响应
     * */
    @RequestMapping(value ="/profile" ,method = RequestMethod.POST)
    public Result profile(HttpServletRequest request) throws Exception{

        //获取session中的安全数据
        Subject subject = SecurityUtils.getSubject();
        //1.subject获取所有的安全数据集合
        PrincipalCollection principals = subject.getPrincipals();
        //2.安全数据
        ProfileResult result =(ProfileResult) principals.getPrimaryPrincipal();
        return new Result(ResultCode.SUCCESS,result);
        /**
        Claims claims =(Claims) request.getAttribute("user_claims");
        //4.获取clams
        String userId = claims.getId();
        User user=userService.findById(userId);
      //根据不同的用户级别获取用户权限
        ProfileResult result=null;
        System.out.println(user.getLevel());
        if ("user".equals(user.getLevel())) {
            result = new ProfileResult(user);
        } else {
            Map map = new HashMap();
            if ("coAdmin".equals(user.getLevel())){
                map.put("en_visible","1");
            }
            List<Permission> list = permissionService.findAll(map);
            result=new ProfileResult(user,list);
        }
        return new Result(ResultCode.SUCCESS,result);

       */
    }
}
