<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <!DOCTYPE html>
<html>


<!-- Mirrored from www.gzsxt.cn/theme/hplus/table_basic.html by HTTrack Website Copier/3.x [XR&CO'2014], Wed, 20 Jan 2016 14:20:01 GMT -->
<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">


    <title>绿地中央广场综合物业办公系统 - 基础表格</title>
    <meta name="keywords" content="综合办公系统">
    <meta name="description" content="综合办公系统">

    <link rel="shortcut icon" href="favicon.ico"> 
    	<link href="${pageContext.request.contextPath}/css/bootstrap.min14ed.css?v=3.3.6" rel="stylesheet">
    	<link href="${pageContext.request.contextPath}/css/font-awesome.min93e3.css?v=4.4.0" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/animate.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.min862f.css?v=4.1.0" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/plugins/select/bootstrap-select.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/layui/css/layui.css" />
</head>

<body class="gray-bg">
	<div class="wrapper2 wrapper-content2 animated fadeInRight">
    <div class="ibox float-e-margins">
                   <div class="ibox-content">
                        <div class="row">
                        		<div class="col-sm-3 col-sm-offset-2 text-right">
                        			<h3><small>搜索条件:</small></h3>
                        		</div>
                            <div class="col-sm-2">
                                <select id="searchType" name="searchType">
                                    <option value="0">选择类型</option>
                                    <option value="1">姓名</option>
                                    <option value="2">角色名称</option>
                                </select>
                            </div>
                           
                            <div class="col-sm-3">
                                <div class="input-group">
                                    <input type="text" id="keyword" name="keyword" placeholder="请输入关键词">&nbsp;&nbsp;&nbsp;&nbsp;

                                       <input type="button" value="搜索" onclick="searchEmployee();">

                                </div>                                
                            </div>
                            <div class="col-sm-2 text-right">
                            	 <a href="${pageContext.request.contextPath}/emp/save-employee" class="btn btn-sm btn-primary" ><i class="fa fa-plus-circle"> 添加员工</i></a>
                            </div>
                        </div>
                        <div class="table-responsive">
                            <table id="empList" lay-filter="test"></table>
                            <script type="text/html" id="toolbarDemo">
                                <div class="layui-btn-container">
                                    <button class="layui-btn layui-btn-sm" lay-event="editEmployee">编辑</button>
                                    <button class="layui-btn layui-btn-sm" lay-event="deleteEmployee">删除</button>
                                </div>
                            </script>
                        </div>
                    </div>
                </div>
     </div>       
    <script src="${pageContext.request.contextPath}/js/jquery.min.js?v=2.1.4"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js?v=3.3.6"></script>
     <script src="${pageContext.request.contextPath}/js/plugins/select/bootstrap-select.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/plugins/sweetalert/sweetalert.min.js"></script>
    <script src="${pageContext.request.contextPath}/layui/layui.js"></script>
   <script>

       var searchType=0;
       var keyword="";
	$(document).ready(function() {
		// 设置按钮的样式
		$('.selectpicker').selectpicker('setStyle', 'btn-white').selectpicker('setStyle', 'btn-sm');
        showEmployeeList(searchType,keyword);
	});


    function get_date(sources){
        var date=new Date(sources);
        var year=date.getFullYear();
        var month=formate_date(date.getMonth()+1);
        var day=formate_date(date.getDate());
        return year+"-"+month+"-"+day;
    }

    function formate_date(s){
        if(s<10){
            return "0"+s;
        }
        return s;
    }
	function showEmployeeList(searchType,keyword){
        layui.use('table', function(){
            var table = layui.table;
            table.render({
                elem:"#empList",
                height:"400",
                url:"${pageContext.request.contextPath}/emp/getAllEmps?searchType="+searchType+"&keyword="+keyword,
                loading:"请稍等，正在努力加载中...",
                even:true,
                page:true,
                limit:5,
                limits:[2,5,10,15,20],
                toolbar: '#toolbarDemo',//开启头部工具栏，并为其绑定左侧模板
                cols:[[
                    {type: 'checkbox', fixed: 'left'},
                    {field: 'eid', title: '编号', width:80, sort: true,fixed:'left'},
                    {field: 'ename', title: '姓名', width:120,fixed:'left'},
                    {field: 'esex', title: '性别', width:80},
                    {field: 'eage', title: '年龄', width:80},
                    {field: 'telephone', title: '手机号', width:200},
                    {field: 'hiredate', title: '入职日期', width:200,templet:function(data){
                            return get_date(data.hiredate);
                        }},
                    {field: 'deptName', title: '部门名称', width:200},
                    {field: 'roleName', title: '所属角色', width:200}
                ]]
            });
            //头工具栏事件
            table.on('toolbar(test)', function(obj){
                var checkStatus = table.checkStatus(obj.config.id);
                switch(obj.event){
                    case 'editEmployee':
                        var data = checkStatus.data;
                        if(data.length!=1){
                            swal({
                                title : "提示信息",
                                text :"只能选择一条记录进行编辑"
                            });
                        }else{
                            window.location="${pageContext.request.contextPath}/emp/update-employee?eid="+data[0].eid;
                        }
                        break;
                    case 'deleteEmployee':
                        var data = checkStatus.data;
                        layer.msg('选中了：'+ data.length + ' 个');
                        break;
                };
            });

        });
    }

    function searchEmployee(){
        searchType= $("#searchType").val();
        keyword= $("#keyword").val();
          showEmployeeList(searchType,keyword);
    }
    </script>
    
</body>


</html>
    