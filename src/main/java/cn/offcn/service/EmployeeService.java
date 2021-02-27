package cn.offcn.service;

import cn.offcn.entity.Employee;
import cn.offcn.utils.EmployeeVo;
import cn.offcn.utils.LayuiTable;
import cn.offcn.utils.OAResult;
import org.springframework.web.multipart.MultipartFile;

public interface EmployeeService {
   public  LayuiTable<EmployeeVo> getAllEmps(int page, int limit,int searchType,String keyword);

    public OAResult saveEmployee(Employee employee, MultipartFile picImage);

    public Employee getEmployeeById(int eid);

    public OAResult updateEmployee(Employee employee, MultipartFile picImage);

    public OAResult checkLogin(String username, String password);

    public OAResult updatePicImage(Employee employee, MultipartFile picImage);

 OAResult updatePassword(Employee employee, String password);
}
