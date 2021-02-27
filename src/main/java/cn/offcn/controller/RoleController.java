package cn.offcn.controller;

import cn.offcn.entity.Role;
import cn.offcn.service.RoleService;
import cn.offcn.utils.LayuiTable;
import cn.offcn.utils.OAResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/role")
public class RoleController {


    @Autowired
    private RoleService roleService;
    @RequestMapping("/{page}")
    public String forward(@PathVariable("page") String page, Integer roleid, Model model){
        model.addAttribute("roleid",roleid);
        return "pages/"+page;
    }

    @ResponseBody
    @RequestMapping("/saveRole")
    public OAResult saveRole(Role role,@RequestParam(name="ids[]") int[] ids){

        return roleService.saveRole(role,ids);
    }

    @ResponseBody
    @RequestMapping("/getAllRoles")
    public LayuiTable<Role> getAllRoles(int page,int limit){

        return roleService.getAllRoles(page,limit);
    }

    @ResponseBody
    @RequestMapping("/getRoleById")
    public Map<String,Object> getRoleById(int roleid){
       return  roleService.getRoleById(roleid);
    }
    @ResponseBody
    @RequestMapping("/updateRole")
    public OAResult updateRole(Role role,@RequestParam(name="ids[]") int[] ids){

        return roleService.updateRole(role,ids);
    }

    @ResponseBody
    @RequestMapping("/deleteRole")
    public OAResult deleteRole(int roleid){
        return roleService.deleteRole(roleid);
    }

    @ResponseBody
    @RequestMapping("/findAllRole")
    public List<Role> findAllRole(){
        return roleService.findAllRole();
    }
}
