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
    <link href="${pageContext.request.contextPath}/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/animate.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.min862f.css?v=4.1.0" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/plugins/select/bootstrap-select.min.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/css/font-awesome.min93e3.css?v=4.4.0" rel="stylesheet">
</head>

<body class="gray-bg">
    <div class="wrapper wrapper-content animated fadeInRight">
        
      <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>个人资料<small>>修改密码</small></h5>
                    </div>
                    <div class="ibox-content">

                       	<div class="row">
                            <div class="form-group">
                                <label class="col-sm-2 control-label">原密码</label>
                                <div class="col-sm-3">
                                    <input name="password" id="password" type="password" onblur="checkPassword();" class="form-control input-sm">
                                    <span id="passwordMsg"></span>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="form-group">
                                <label class="col-sm-2 control-label">新密码</label>
                                <div class="col-sm-3">
                                    <input name="newPassword" id="newPassword" type="password" onblur="checkNewPassword();" class="form-control input-sm">
                                    <span id="newPasswordMsg"></span>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="form-group">
                                <label class="col-sm-2 control-label">确认密码</label>
                                <div class="col-sm-3">
                                    <input name="confirmPassword" id="confirmPassword" type="password" onblur="checkConfirmPassword();"  class="form-control input-sm">
                                    <span id="confirmPasswordMsg"></span>
                                </div>
                            </div>
                        </div>
                        
                        
                     	<div class="row">
                     		<div class="hr-line-dashed"></div>
                     	</div>
                          
                         <div class="row">
                            <div class="form-group">
                                <div class="col-sm-3 col-sm-offset-3 text-right">
                                    <input type="button" onclick="updatePassword();" value="保存内容"></input>
                                </div>
                                <div class="col-sm-3">
                                	<a href="list-project.html" class="btn btn-white">返回</a>
                                	</div>
                            </div>
                       </div>

                    </div>
                </div>
            </div>
        </div>
    </div>

 
    
    
    <script src="${pageContext.request.contextPath}/js/jquery.min.js?v=2.1.4"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js?v=3.3.6"></script>
    <script src="${pageContext.request.contextPath}/js/plugins/sweetalert/sweetalert.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/plugins/select/bootstrap-select.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/plugins/layer/laydate/laydate.js"></script>
    <!-- 修复日期控件长度-->
    <link href="${pageContext.request.contextPath}/css/customer.css" rel="stylesheet">
   <script>

          var flag=false;
          function checkPassword(){

              $("#passwordMsg").html("");
              var password=$("#password").val();
              if(password==null || password==""){
                  $("#passwordMsg").html("原密码不能为空");
                  $("#passwordMsg").css("color","red");
                  flag=false;
                  return false;
              }else{
                  $.ajax({
                      url:"${pageContext.request.contextPath}/emp/checkPassword",
                      type:"post",
                      data:{"password":password},
                      dataType:"json",
                      cache:false,
                      success:function(rs){
                          $("#passwordMsg").html(rs.msg);
                           if(rs.status==200){
                               $("#passwordMsg").css("color","green");
                               flag=true;
                           }else{
                               $("#passwordMsg").css("color","red");
                               flag=false;
                           }
                      }
                  });
              }
              return flag;
          }

          function checkNewPassword(){
              $("#newPasswordMsg").html("");
              var newPassword=$("#newPassword").val();
              if(newPassword==null || newPassword==""){
                  $("#newPasswordMsg").html("新密码不能为空");
                  $("#newPasswordMsg").css("color","red");
                  return false;
              }
              return true;
          }

          function checkConfirmPassword(){
              $("#confirmPasswordMsg").html("");
              var confirmPassword=$("#confirmPassword").val();
              var newPassword=$("#newPassword").val();
              if(confirmPassword==null || confirmPassword==""){
                  $("#confirmPasswordMsg").html("确认密码不能为空");
                  $("#confirmPasswordMsg").css("color","red");
                  return false;
              }
              if(newPassword!=confirmPassword){
                  $("#confirmPasswordMsg").html("两次密码不一致");
                  $("#confirmPasswordMsg").css("color","red");
                  return false;
              }
              return true;
          }

          function updatePassword(){


              if(flag && checkConfirmPassword() && checkNewPassword()){

                  var newPassword=$("#newPassword").val();
                  $.ajax({
                      url:"${pageContext.request.contextPath}/emp/updatePassword",
                      type:"post",
                      data:{"password":newPassword},
                      dataType:"json",
                      cache:false,
                      success:function(rs){
                           if(rs.status==200){
                               alert(rs.msg);
                               window.top.location="${pageContext.request.contextPath}/login";
                           }else{
                               alert(rs.msg);
                           }
                      }
                  });
              }
          }

   </script>

</body>


</html>
    