package cn.offcn.service.impl;

import cn.offcn.entity.*;
import cn.offcn.mapper.DeptMapper;
import cn.offcn.mapper.EmpRoleMapper;
import cn.offcn.mapper.EmployeeMapper;
import cn.offcn.mapper.RoleMapper;
import cn.offcn.service.EmployeeService;
import cn.offcn.utils.EmployeeVo;
import cn.offcn.utils.LayuiTable;
import cn.offcn.utils.OAResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {


    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private DeptMapper deptMapper;
    @Autowired
    private EmpRoleMapper empRoleMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Value("${fileDir}")
    private String fileDir;
    @Override
    public LayuiTable<EmployeeVo> getAllEmps(int page, int limit,int searchType,String keyword) {

        //设置分页
        PageHelper.startPage(page,limit);
        //创建一个EmployeeExample对象
        EmployeeExample employeeExample=new EmployeeExample();
        EmployeeExample.Criteria exampleCriteria = employeeExample.createCriteria();
        if(searchType!=0){

            if(searchType==1){
                if(StringUtils.isNotBlank(keyword)){
                    exampleCriteria.andEnameLike("%"+keyword+"%");
                }
            }
            if(searchType==2){
                if(StringUtils.isNotBlank(keyword)){
                    RoleExample roleExample=new RoleExample();
                    RoleExample.Criteria roleExampleCriteria = roleExample.createCriteria();
                    roleExampleCriteria.andRolenameLike("%"+keyword+"%");
                    List<Role> rolesList = roleMapper.selectByExample(roleExample);
                    List<Integer> roleidList=new ArrayList<Integer>();
                    for (Role role : rolesList) {
                        roleidList.add(role.getRoleid());
                    }
                    EmpRoleExample empRoleExample=new EmpRoleExample();
                    EmpRoleExample.Criteria empRoleExampleCriteria = empRoleExample.createCriteria();
                    empRoleExampleCriteria.andRoleFkIn(roleidList);
                    List<EmpRole> empRoleList = empRoleMapper.selectByExample(empRoleExample);
                    List<Integer> eidList=new ArrayList<Integer>();
                    for (EmpRole empRole : empRoleList) {
                        eidList.add(empRole.getEmpFk());
                    }
                    exampleCriteria.andEidIn(eidList);
                }
            }

        }
        //查询所有的员工
        List<Employee> employeeList = employeeMapper.selectByExample(employeeExample);
        //创建一个PageInfo对象
        PageInfo<Employee> pageInfo=new PageInfo<Employee>(employeeList);
        //创建一个LayuiTable对象
        LayuiTable<EmployeeVo> layuiTable=new LayuiTable<EmployeeVo>();
        layuiTable.setCode(0);
        layuiTable.setMsg("");
        layuiTable.setCount(pageInfo.getTotal());
        //获取分页数据
        List<Employee> cutEmployeeList = pageInfo.getList();
        //创建一个新的集合，集合中存放EmployeeVo对象
        List<EmployeeVo> employeeVoList=new ArrayList<EmployeeVo>();
        //循环获取分页数据
        for (Employee employee : cutEmployeeList) {
            //创建一个EmployeeVo对象
            EmployeeVo employeeVo=new EmployeeVo(employee);
            //查询部门信息
            Dept dept=deptMapper.selectByPrimaryKey(employee.getDfk());
            //把部门的名称设置给EmployeeVo的deptName属性
            employeeVo.setDeptName(dept.getDname());
            //创建一个EmpRoleExample对象
            EmpRoleExample empRoleExample=new EmpRoleExample();
            //创建条件查询对象
            EmpRoleExample.Criteria empRoleCriteria = empRoleExample.createCriteria();
            //把员工的eid设置给中间表的emp_fk属性
            empRoleCriteria.andEmpFkEqualTo(employee.getEid());
            //查询员工所对应的角色
            List<EmpRole> empRoleList = empRoleMapper.selectByExample(empRoleExample);
            //如果没有查询出角色，就把employeeVo的roleName设置成"未知角色"
            if(empRoleList==null || empRoleList.size()==0){
                employeeVo.setRoleName("未知角色");
            }
            //如果查询出角色，根据角色的id查询该角色信息
            else if(empRoleList!=null && empRoleList.size()>0 ){
                //查询角色信息
                Role role= roleMapper.selectByPrimaryKey(empRoleList.get(0).getRoleFk());
                //把角色的名称设置给employeeVo的roleName属性
                employeeVo.setRoleName(role.getRolename());
            }
            //把employeeVo添加到employeeVoList集合中
            employeeVoList.add(employeeVo);
        }
        //把employeeVoList设置到layuiTable的data属性中
        layuiTable.setData(employeeVoList);

        return layuiTable;
    }

    @Override
    public OAResult saveEmployee(Employee employee, MultipartFile picImage) {

        try {
            //处理文件上传
            if(picImage!=null && picImage.getSize()>0){
                //1. 获取上传的文件名
               String fileName= picImage.getOriginalFilename();
               //2. 获取扩展名
                String extName=fileName.substring(fileName.lastIndexOf("."));
                //3.根据文件名生成一个hashCode
               int d1=fileName.hashCode()&0xf;
               //4.hashCode右移两位
               int d2=d1>>2&0xf;
               //5. 文件要存储的目录
               String saveDir=fileDir+"/"+d1+"/"+d2;
               File newSaveDir=new File(saveDir);
               if(!newSaveDir.exists()) newSaveDir.mkdirs();
               //6.新文件名
               String newFileName= UUID.randomUUID().toString()+extName;
               File file=new File(saveDir,newFileName);
               picImage.transferTo(file);
               //设置上传文件的访问路径
                employee.setPic("/pic/images/"+d1+"/"+d2+"/"+newFileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return OAResult.ok(400,"头像上传失败");
        }
        //保存员工信息
        employee.setPassword(DigestUtils.md5DigestAsHex(employee.getPassword().getBytes()));
        int row1=employeeMapper.insert(employee);
        //保存员所拥用的角色
        EmpRole empRole=new EmpRole();
        empRole.setEmpFk(employee.getEid());
        empRole.setRoleFk(employee.getRoleid());
        int row2=empRoleMapper.insert(empRole);
        if(row1==1 && row2==1){
            return OAResult.ok(200,"操作成功");
        }
        return OAResult.ok(401,"操作失败");
    }

    @Override
    public Employee getEmployeeById(int eid) {
        Employee employee=employeeMapper.selectByPrimaryKey(eid);
        EmpRoleExample empRoleExample=new EmpRoleExample();
        EmpRoleExample.Criteria criteria = empRoleExample.createCriteria();
        criteria.andEmpFkEqualTo(eid);
        List<EmpRole> empRoleList = empRoleMapper.selectByExample(empRoleExample);
        if(empRoleList!=null && empRoleList.size()>0){
            employee.setRoleid(empRoleList.get(0).getRoleFk());
        }

        return employee;
    }

    @Override
    public OAResult updateEmployee(Employee employee, MultipartFile picImage) {
        Employee oldEmployee=employeeMapper.selectByPrimaryKey(employee.getEid());
        try{
            //处理文件上传
            if(picImage!=null && picImage.getSize()>0) {
                //1. 获取上传的文件名
                String fileName = picImage.getOriginalFilename();
                //2. 获取扩展名
                String extName = fileName.substring(fileName.lastIndexOf("."));
                //3.根据文件名生成一个hashCode
                int d1 = fileName.hashCode() & 0xf;
                //4.hashCode右移两位
                int d2 = d1 >> 2 & 0xf;
                //5. 文件要存储的目录
                String saveDir = fileDir + "/" + d1 + "/" + d2;
                File newSaveDir = new File(saveDir);
                if (!newSaveDir.exists()) newSaveDir.mkdirs();
                //6.新文件名
                String newFileName = UUID.randomUUID().toString() + extName;
                File file = new File(saveDir, newFileName);
                picImage.transferTo(file);
                //设置上传文件的访问路径
                employee.setPic("/pic/images/" + d1 + "/" + d2 + "/" + newFileName);
            }else{
                employee.setPic(oldEmployee.getPic());
            }
        }catch(Exception e){
            e.printStackTrace();
            return OAResult.ok(400,"头像上传失败");
        }
        if(StringUtils.isNotBlank(employee.getPassword())){
            employee.setPassword(DigestUtils.md5DigestAsHex(employee.getPassword().getBytes()));
        }else{
            employee.setPassword(oldEmployee.getPassword());
        }
        //修改员工信息
        int row1=employeeMapper.updateByPrimaryKey(employee);
        //根据eid把中间表中数据删除
        EmpRoleExample empRoleExample=new EmpRoleExample();
        EmpRoleExample.Criteria criteria = empRoleExample.createCriteria();
        criteria.andEmpFkEqualTo(employee.getEid());
        empRoleMapper.deleteByExample(empRoleExample);

        //重新保存员工与角色信息
        EmpRole empRole=new EmpRole();
        empRole.setEmpFk(employee.getEid());
        empRole.setRoleFk(employee.getRoleid());
        int row2=empRoleMapper.insert(empRole);
        if(row1==1 && row2==1){
            return OAResult.ok(200,"操作成功");
        }
        return OAResult.ok(401,"操作失败");

    }

    public OAResult checkLogin(String username, String password){

        EmployeeExample employeeExample=new EmployeeExample();
        EmployeeExample.Criteria criteria = employeeExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        password=DigestUtils.md5DigestAsHex(password.getBytes());
        criteria.andPasswordEqualTo(password);
        List<Employee> employeeList = employeeMapper.selectByExample(employeeExample);
        if(employeeList!=null && employeeList.size()>0){
            EmpRoleExample empRoleExample=new EmpRoleExample();
            EmpRoleExample.Criteria empRoleExampleCriteria = empRoleExample.createCriteria();
            empRoleExampleCriteria.andEmpFkEqualTo(employeeList.get(0).getEid());
            List<EmpRole> empRoleList = empRoleMapper.selectByExample(empRoleExample);
            if(empRoleList!=null && empRoleList.size()>0){
                Role role=roleMapper.selectByPrimaryKey(empRoleList.get(0).getRoleFk());
                employeeList.get(0).setRoleName(role.getRolename());
            }
            return OAResult.build(200,"登陆成功",employeeList.get(0));
        }
        return OAResult.ok(400,"用户名或密码错误");
    }

    public OAResult updatePicImage(Employee employee, MultipartFile picImage){
        try{
            //处理文件上传
            if(picImage!=null && picImage.getSize()>0) {
                //1. 获取上传的文件名
                String fileName = picImage.getOriginalFilename();
                //2. 获取扩展名
                String extName = fileName.substring(fileName.lastIndexOf("."));
                //3.根据文件名生成一个hashCode
                int d1 = fileName.hashCode() & 0xf;
                //4.hashCode右移两位
                int d2 = d1 >> 2 & 0xf;
                //5. 文件要存储的目录
                String saveDir = fileDir + "/" + d1 + "/" + d2;
                File newSaveDir = new File(saveDir);
                if (!newSaveDir.exists()) newSaveDir.mkdirs();
                //6.新文件名
                String newFileName = UUID.randomUUID().toString() + extName;
                File file = new File(saveDir, newFileName);
                picImage.transferTo(file);
                //设置上传文件的访问路径
                employee.setPic("/pic/images/" + d1 + "/" + d2 + "/" + newFileName);
            }
        }catch(Exception e){
            e.printStackTrace();
            return OAResult.ok(400,"头像上传失败");
        }
        int rows=employeeMapper.updateByPrimaryKey(employee);
        if(rows==1){
            return OAResult.build(200,"操作成功",employee);
        }
        return OAResult.ok(401,"操作失败");
    }

    @Override
    public OAResult updatePassword(Employee employee, String password) {
        employee.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        int rows=employeeMapper.updateByPrimaryKey(employee);
        if(rows==1){
            return OAResult.ok(200,"操作成功");
        }
        return OAResult.ok(401,"操作失败");
    }
}
