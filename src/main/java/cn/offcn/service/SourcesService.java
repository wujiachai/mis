package cn.offcn.service;

import cn.offcn.entity.Sources;
import cn.offcn.utils.OAResult;

import java.util.List;

public interface SourcesService {


    public List<Sources> getRootSourcesByPid(int pid);

    public List<Sources> getAllParentNode(List<Sources> parentNodeList);

    public  OAResult saveSources(Sources sources);

    public Sources getSourceById(int id);

    public OAResult updateSources(Sources sources);

    public OAResult deleteSourcesById(int id);

    public List<Sources> getCurrentEmployeeSources(Integer eid, int pid);

    public List<Sources>  getCacheRedisEmployeeSources(String username,Integer eid, int pid);
}
