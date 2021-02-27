package cn.offcn.service.impl;

import cn.offcn.entity.RoleSourcesExample;
import cn.offcn.entity.Sources;
import cn.offcn.entity.SourcesExample;
import cn.offcn.mapper.RoleSourcesMapper;
import cn.offcn.mapper.SourcesMapper;
import cn.offcn.service.SourcesService;
import cn.offcn.utils.JsonUtils;
import cn.offcn.utils.OAResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

@Service
public class SourcesServiceImpl implements SourcesService {

    @Autowired
    private SourcesMapper sourcesMapper;
    @Autowired
    private RoleSourcesMapper roleSourcesMapper;
    @Autowired
    private JedisPool jedisPool;
    @Override
    public List<Sources> getRootSourcesByPid(int pid) {

        SourcesExample sourcesExample=new SourcesExample();
        SourcesExample.Criteria criteria = sourcesExample.createCriteria();
        criteria.andPidEqualTo(pid);
        List<Sources> sourcesList = sourcesMapper.selectByExample(sourcesExample);
        for (Sources sources : sourcesList) {
            List<Sources> subList=getRootSourcesByPid(sources.getId());
            sources.setChildren(subList);
        }
        return sourcesList;
    }

    @Override
    public List<Sources> getAllParentNode(List<Sources> parentNodeList) {
        SourcesExample sourcesExample=new SourcesExample();
        List<Sources> sourcesList = sourcesMapper.selectByExample(sourcesExample);
        for (Sources sources : sourcesList) {
            if(isParentNode(sources.getId())){
                parentNodeList.add(sources);
            }
        }
        return parentNodeList;
    }

    /**
     * 判断该点节是否为父节点
     * @param id
     * @return
     */
    public boolean isParentNode(int id){
        SourcesExample sourcesExample=new SourcesExample();
        SourcesExample.Criteria criteria = sourcesExample.createCriteria();
        criteria.andPidEqualTo(id);
        List<Sources> subList = sourcesMapper.selectByExample(sourcesExample);
        if(subList!=null && subList.size()>0){
            return true;
        }
        return false;
    }

    @Override
    public OAResult saveSources(Sources sources) {

        int rows=sourcesMapper.insert(sources);
        if(rows==1){
            return OAResult.ok(200,"操作成功");
        }
        return OAResult.ok(400,"操作失败");
    }

    @Override
    public Sources getSourceById(int id) {

        return sourcesMapper.selectByPrimaryKey(id);
    }

    @Override
    public OAResult updateSources(Sources sources) {

        int rows=sourcesMapper.updateByPrimaryKey(sources);
        if(rows==1){
            return OAResult.ok(200,"操作成功");
        }
        return OAResult.ok(400,"操作失败");
    }

    @Override
    public OAResult deleteSourcesById(int id) {

        //删除资源与角色的中间表中对应数据
        RoleSourcesExample roleSourcesExample=new RoleSourcesExample();
        RoleSourcesExample.Criteria roleSourcesExampleCriteria = roleSourcesExample.createCriteria();
        roleSourcesExampleCriteria.andResourcesFkEqualTo(id);
        roleSourcesMapper.deleteByExample(roleSourcesExample);
        //删除资源表中数据
        int rows=sourcesMapper.deleteByPrimaryKey(id);
        if(rows==1){
            return OAResult.ok(200,"操作成功");
        }
        return OAResult.ok(400,"操作失败");
    }

    public List<Sources>  getCacheRedisEmployeeSources(String username,Integer eid, int pid){

        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            String jsonData=jedis.hget("menu",username);
            if(StringUtils.isNotBlank(jsonData)){
                return JsonUtils.jsonToList(jsonData,Sources.class);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(jedis!=null) jedis.close();
        }

        List<Sources> sourcesList=getCurrentEmployeeSources(eid,pid);

        try {
            jedis=jedisPool.getResource();
            //把sourcesList缓存到redis中
            jedis.hset("menu",username, JsonUtils.objectToJson(sourcesList));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(jedis!=null) jedis.close();
        }

        return sourcesList;

    }

    public List<Sources> getCurrentEmployeeSources(Integer eid, int pid){
        List<Sources> sourcesList=sourcesMapper.getCurrentEmployeeSources(eid,pid);
        for (Sources sources : sourcesList) {
            List<Sources> subList= getCurrentEmployeeSources(eid,sources.getId());
            sources.setChildren(subList);
        }
        return sourcesList;
    }


}
