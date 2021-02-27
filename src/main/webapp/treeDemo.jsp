<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/plugins/zTreeStyle/zTreeStyle.css" />
    <script src="${pageContext.request.contextPath}/js/jquery.min.js?v=2.1.4"></script>
    <script src="${pageContext.request.contextPath}/js/plugins/ztree/jquery.ztree.core.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/plugins/ztree/jquery.ztree.excheck.js"></script>
    <script>
        var setting={
            check: {
                enable: true
            },
            view:{"showLine":true}
        };
        var zNodes=[
            {"id":1,"name":"日常管理","open":true,
            "children":[
                {"id":2,"name":"我的任务"},
                {"id":3,"name":"我的考勤"},
                {"id":4,"name":"我的福利"}
            ]},
            {"id":5,"name":"权限管理",
             "children":[
                 {"id":6,"name":"资源管理"},
                 {"id":7,"name":"角色管理"},
                 {"id":8,"name":"剪贴","icon":"${pageContext.request.contextPath}/img/1705.jpg"}
             ]}
        ];

        $(function(){
            $.fn.zTree.init($("#treeDemo"), setting, zNodes);
        });
    </script>
</head>
<body>
     <ul id="treeDemo" class="ztree"></ul>
</body>
</html>
