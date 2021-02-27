package cn.offcn.controller;

import cn.offcn.service.EmployeeService;
import cn.offcn.utils.OAResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private EmployeeService employeeService;
    @RequestMapping("/login")
    public String forwardPage(){

        return "pages/login";
    }

    @ResponseBody
    @RequestMapping("/checkLogin")
    public OAResult checkLogin(String username, String password, HttpSession session){
        OAResult oaResult=employeeService.checkLogin(username,password);
        if(oaResult.getStatus()==200){
            session.setAttribute("activeUser",oaResult.getData());
        }
        return oaResult;
    }

    @RequestMapping("/exitSystem")
    public String exitSystem(HttpSession session){

        if(session!=null){
            session.invalidate();
        }
        return  "redirect:/login";
    }
}
