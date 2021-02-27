package cn.offcn.controller;

import cn.offcn.entity.Sources;
import cn.offcn.service.SourcesService;
import cn.offcn.utils.OAResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/sources")
public class SourcesController {

    @Autowired
    private SourcesService sourcesService;

    @RequestMapping("/{page}")
    public String forwardPage(@PathVariable("page") String page,  Integer id, Model model) {

        model.addAttribute("id",id);
        return "pages/"+page;
    }
    @ResponseBody
    @RequestMapping("/getRootSourcesByPid")
    public List<Sources> getRootSourcesByPid(@RequestParam(defaultValue = "0") int pid){

        return sourcesService.getRootSourcesByPid(pid);
    }

    @ResponseBody
    @RequestMapping("/getAllParentNode")
    public List<Sources> getAllParentNode(){

        List<Sources> parentNodeList=new ArrayList<Sources>();
       return  sourcesService.getAllParentNode(parentNodeList);
    }

    @ResponseBody
    @RequestMapping("/saveSources")
    public OAResult saveSources(Sources sources){
        return sourcesService.saveSources(sources);
    }

    @ResponseBody
    @RequestMapping("/getSourceById")
    public Sources getSourceById(int id){
        return sourcesService.getSourceById(id);

    }



    @ResponseBody
    @RequestMapping("/updateSources")
    public OAResult updateSources(Sources sources){
        return sourcesService.updateSources(sources);
    }


    @ResponseBody
    @RequestMapping("/deleteSourcesById")
    public OAResult deleteSourcesById(int id){

        return sourcesService.deleteSourcesById(id);
    }
}
