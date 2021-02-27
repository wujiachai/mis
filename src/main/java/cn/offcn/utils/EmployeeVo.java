package cn.offcn.utils;

import cn.offcn.entity.Employee;

public class EmployeeVo  extends Employee {

    private String deptName;
    private String roleName;
    public EmployeeVo(){}
    public EmployeeVo(Employee employee){
        this.setEid(employee.getEid());
        this.setEname(employee.getEname());
        this.setEsex(employee.getEsex());
        this.setEage(employee.getEage());
        this.setTelephone(employee.getTelephone());
        this.setHiredate(employee.getHiredate());
        this.setPnum(employee.getPnum());
        this.setUsername(employee.getUsername());
        this.setPassword(employee.getPassword());
        this.setPic(employee.getPic());
        this.setRemark(employee.getRemark());
        this.setDfk(employee.getDfk());
    }
    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
