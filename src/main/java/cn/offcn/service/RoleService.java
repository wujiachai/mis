package cn.offcn.service;

import cn.offcn.entity.Role;
import cn.offcn.utils.LayuiTable;
import cn.offcn.utils.OAResult;

import java.util.List;
import java.util.Map;

public interface RoleService {
    public  OAResult saveRole(Role role, int[] ids);

    public LayuiTable<Role> getAllRoles(int page,int limit);

    public Map<String, Object> getRoleById(int roleid);

    public OAResult updateRole(Role role, int[] ids);

    public OAResult deleteRole(int roleid);

    public List<Role> findAllRole();
}
