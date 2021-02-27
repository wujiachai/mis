<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
 <!DOCTYPE html>
<html>
<!-- Mirrored from www.gzsxt.cn/theme/hplus/table_basic.html by HTTrack Website Copier/3.x [XR&CO'2014], Wed, 20 Jan 2016 14:20:01 GMT -->
<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">


    <title>办公系统 - 基础表格</title>
    <meta name="keywords" content="办公系统">
    <meta name="description" content="办公系统">

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
	    <div class="row">
	    		<div class="col-sm-5">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>添加部门</h5>
                    </div>
                    <div class="ibox-content">
                        <form class="form-horizontal">
                            <div class="form-group">
                                <label class="col-sm-3 control-label">部门编号：</label>

                                <div class="col-sm-8">
                                    <input type="email" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">部门名称：</label>

                                <div class="col-sm-8">
                                    <input type="email" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">区域：</label>

                                <div class="col-sm-8">
                                    <input type="email" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-offset-3 col-sm-8">
                                    <button class="btn btn-sm btn-white" type="submit"><i class="fa fa-save"></i> 保存</button>
                                    <button class="btn btn-sm btn-white" type="submit"><i class="fa fa-undo"></i> 重置</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
	    		<div class="col-sm-7">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>部门列表</h5>
                    </div>
                    <div class="ibox-content">
                        <div class="row">
                            <div class="table-responsive">
                            <table id="deptTable" lay-filter="test"></table>
                            <script type="text/html" id="toolbarDemo">
                                <div class="layui-btn-container">
                                    <button class="layui-btn layui-btn-sm" lay-event="getCheckData">编辑</button>
                                    <button class="layui-btn layui-btn-sm" lay-event="getCheckLength">删除</button>
                                </div>
                            </script>
                        </div>

                        </div>
                    </div>
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
	$(document).ready(function() {
         showDept();

	});

	function showDept(){
        layui.use('table', function(){
            var table = layui.table;
            table.render({
                elem: '#deptTable',
                url:'${pageContext.request.contextPath}/dept/getAllDept',
                toolbar: '#toolbarDemo', //开启头部工具栏，并为其绑定左侧模板
                defaultToolbar: ['filter', 'exports', 'print', { //自定义头部工具栏右侧图标。如无需自定义，去除该参数即可
                    title: '提示'
                    ,layEvent: 'LAYTABLE_TIPS'
                    ,icon: 'layui-icon-tips'
                }],
                title: '用户数据表',
                even:true,
                page:true,
                cols: [[
                    {type: 'checkbox', fixed: 'left'},
                    {field:'deptno', title:'编号', width:80, fixed: 'left', unresize: true, sort: true},
                    {field:'dname', title:'部门名称', width:120, edit: 'text'},
                    {field:'local', title:'区域', width:80, edit: 'text'}
                ]]

            });
            //头工具栏事件
            table.on('toolbar(test)', function(obj){
                var checkStatus = table.checkStatus(obj.config.id);
                switch(obj.event){
                    case 'getCheckData':
                        var data = checkStatus.data;
                        if(data.length!=1){
                            swal({
                                title : "提示信息",
                                text :"只能选择一条记录进行编辑"
                            });
                            return false;
                        }else{
                            //发请求
                            alert(data[0].deptno);
                        }
                        break;
                    case 'getCheckLength':
                        var data = checkStatus.data;
                        if(data.length==0){
                            swal({
                                title : "提示信息",
                                text :"至少选择一条记录进行删除"
                            });
                            return false;
                        }else{
                            //发请求

                        }
                        break;
                };
            });

        });
    }
    </script>  
</body>


</html>
    
