package cn.offcn.service;

import cn.offcn.entity.Dept;
import cn.offcn.utils.LayuiTable;

import java.util.List;

public interface DeptService {
    public LayuiTable<Dept> getAllDept(int page, int limit);

    public List<Dept> getAllDepts();
}
