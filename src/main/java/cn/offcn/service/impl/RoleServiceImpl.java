package cn.offcn.service.impl;

import cn.offcn.entity.*;
import cn.offcn.mapper.EmpRoleMapper;
import cn.offcn.mapper.RoleMapper;
import cn.offcn.mapper.RoleSourcesMapper;
import cn.offcn.service.RoleService;
import cn.offcn.utils.LayuiTable;
import cn.offcn.utils.OAResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleSourcesMapper roleSourcesMapper;
    @Autowired
    private EmpRoleMapper empRoleMapper;
    @Override
    public OAResult saveRole(Role role, int[] ids) {

        //保存角色
        int rows=roleMapper.insert(role);
        int count=0;
        //保存角色所拥用资源(role_sources)
         if(ids!=null && ids.length>0){
             for (int id : ids) {
                 RoleSources roleSources=new RoleSources();
                 roleSources.setResourcesFk(id);
                 roleSources.setRoleFk(role.getRoleid());
                 roleSourcesMapper.insert(roleSources);
                 count++;
             }
         }
         if(rows==1 && count==ids.length){
             return OAResult.ok(200,"操作成功");
         }
        return OAResult.ok(400,"操作失败");
    }

    @Override
    public LayuiTable<Role> getAllRoles(int page,int limit) {
        PageHelper.startPage(page,limit);
        List<Role> roleList = roleMapper.selectByExample(new RoleExample());
        PageInfo<Role> pageInfo=new PageInfo<Role>(roleList);
        LayuiTable<Role> layuiTable=new LayuiTable<Role>();
        layuiTable.setCode(0);
        layuiTable.setMsg("");
        layuiTable.setCount(pageInfo.getTotal());
        layuiTable.setData(pageInfo.getList());
        return layuiTable;
    }

    @Override
    public Map<String, Object> getRoleById(int roleid) {
        Role role=roleMapper.selectByPrimaryKey(roleid);
        List<Integer> sourceIds=new ArrayList();
        RoleSourcesExample roleSourcesExample=new RoleSourcesExample();
        RoleSourcesExample.Criteria criteria = roleSourcesExample.createCriteria();
        criteria.andRoleFkEqualTo(roleid);
        List<RoleSources> roleSourcesList=roleSourcesMapper.selectByExample(roleSourcesExample);
        for (RoleSources roleSources : roleSourcesList) {
            sourceIds.add(roleSources.getResourcesFk());
        }
        Map<String,Object> map=new HashMap();
        map.put("role",role);
        map.put("sourceIds",sourceIds);
        return map;
    }

    @Override
    public OAResult updateRole(Role role, int[] ids) {

        //更新角色
        int rows=roleMapper.updateByPrimaryKey(role);
        //先删除中间表中原角色所对应的资源
        RoleSourcesExample roleSourcesExample =new RoleSourcesExample();
        RoleSourcesExample.Criteria criteria = roleSourcesExample.createCriteria();
        criteria.andRoleFkEqualTo(role.getRoleid());
        roleSourcesMapper.deleteByExample(roleSourcesExample);
        int count=0;
        //重新加角色资源
        if(ids!=null && ids.length>0){
            for (int id : ids) {
                RoleSources roleSources=new RoleSources();
                roleSources.setResourcesFk(id);
                roleSources.setRoleFk(role.getRoleid());
                roleSourcesMapper.insert(roleSources);
                count++;
            }
        }
        if(rows==1 && count==ids.length){
            return OAResult.ok(200,"操作成功");
        }
        return OAResult.ok(400,"操作失败");
    }

    @Override
    public OAResult deleteRole(int roleid) {
        //1. role_emp的中间表
        EmpRoleExample empRoleExample=new EmpRoleExample();
        EmpRoleExample.Criteria roleExampleCriteria = empRoleExample.createCriteria();
        roleExampleCriteria.andRoleFkEqualTo(roleid);
        empRoleMapper.deleteByExample(empRoleExample);
        //2. role_sources的中间表
        RoleSourcesExample roleSourcesExample=new RoleSourcesExample();
        RoleSourcesExample.Criteria roleSourcesExampleCriteria = roleSourcesExample.createCriteria();
        roleSourcesExampleCriteria.andRoleFkEqualTo(roleid);
        roleSourcesMapper.deleteByExample(roleSourcesExample);
        //3.删除角色
        int rows=roleMapper.deleteByPrimaryKey(roleid);
        if(rows==1){
            return OAResult.ok(200,"操作成功");
        }
        return OAResult.ok(400,"操作失败");
    }

    @Override
    public List<Role> findAllRole() {
        return roleMapper.selectByExample(new RoleExample());
    }
}
