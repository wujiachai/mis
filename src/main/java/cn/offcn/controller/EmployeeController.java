package cn.offcn.controller;

import cn.offcn.entity.Employee;
import cn.offcn.service.EmployeeService;
import cn.offcn.utils.EmployeeVo;
import cn.offcn.utils.LayuiTable;
import cn.offcn.utils.OAResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/emp")
public class EmployeeController {


    @Autowired
    private EmployeeService employeeService;

    @RequestMapping("/{page}")
    public String forwardPage(@PathVariable("page") String page, Integer eid, Model model){
        model.addAttribute("eid",eid);
        return "pages/"+page;
    }

    @ResponseBody
    @RequestMapping("/getAllEmps")
    public LayuiTable<EmployeeVo> getAllEmps(int page, int limit,
                                             @RequestParam(defaultValue ="0") Integer searchType,
                                             @RequestParam(defaultValue ="") String keyword){
        return employeeService.getAllEmps(page,limit,searchType,keyword);
    }

    @ResponseBody
    @RequestMapping("/saveEmployee")
    public OAResult saveEmployee(Employee employee, MultipartFile picImage){

       return employeeService.saveEmployee(employee,picImage);
    }

    @ResponseBody
    @RequestMapping("/getEmployeeById")
    public Employee getEmployeeById(int eid){
        return employeeService.getEmployeeById(eid);
    }

    @ResponseBody
    @RequestMapping("/updateEmployee")
    public OAResult updateEmployee(Employee employee, MultipartFile picImage){
        return employeeService.updateEmployee(employee,picImage);
    }

    @ResponseBody
    @RequestMapping("/updatePicImage")
    public OAResult updatePicImage(MultipartFile picImage, HttpSession session){
        Employee employee=(Employee)session.getAttribute("activeUser");
        return employeeService.updatePicImage(employee,picImage);
    }
    @ResponseBody
    @RequestMapping("/checkPassword")
    public OAResult checkPassword(String password,HttpSession session){
        Employee employee=(Employee)session.getAttribute("activeUser");
        if(employee.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))){
              return OAResult.ok(200,"密码正确");
        }
        return OAResult.ok(400,"原密码错误");
    }

    @ResponseBody
    @RequestMapping("/updatePassword")
    public OAResult updatePassword(String password,HttpSession session){
        Employee employee=(Employee)session.getAttribute("activeUser");
        return employeeService.updatePassword(employee,password);
    }
}
