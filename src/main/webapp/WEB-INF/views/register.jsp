<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
  <head>
    <title>新中大移动应用注册</title>
    <link href="../easyui/themes/gray/easyui.css" rel="stylesheet" type="text/css" />
    <link href="../easyui/themes/icon.css" rel="stylesheet" type="text/css" />
    <link href="../resource/css/register.css" rel="stylesheet" type="text/css" />
    
    <script src="../easyui/scripts/jquery.min.js" type="text/javascript"></script>
    <script src="../easyui/scripts/jquery.easyui.min.js" type="text/javascript"></script>
    <script src="../easyui/scripts/jquery.json-2.4.min.js" type="text/javascript"></script>
    <script src="../resource/js/jquery.blockUI.js" type="text/javascript"></script>
   
     <script type="text/javascript">
     	var strSn = "";
        $(document).ready(function () {
        	InitEnterpriseInfo();
        });
        
        function InitEnterpriseInfo(){
        	/*读取加密狗*/
            var usbobj = document.getElementById("usblock");
            if (usbobj != undefined) {
                BlockUI("正在检测加密狗信息！");
               	strSn = usbobj.GetSn();
				//strSn = "03504";
                //根据加密锁号获取相关信息
                if (strSn != "") {
                    $.ajax({
                        url: "${pageContext.request.contextPath}/service/getcustomerinfo",
                        data: { esn: strSn},
                        type: "post",
                        cache: false,
                        beforeSend: function () {
                            BlockUI();
                        },
                        success: function (data) {
                            var result = data;
                            if(result != null && result != undefined && result.enterpriseSn != undefined){
                            	$("#SN").val(result.enterpriseSn);
                            	$("#UserName").val(result.enterpriseName);
                            }
                            else{
                            	AlertMsg("未能得到客户信息");
                            }
                           
                        },
                        complete: function () {
                           	UnBlockUI();
                        },
				        error: function (errText) {
				            AlertMsg(errText.responseText);
				        }

                    });
                    
                    //读取已注册的企业信息
                    $.ajax({
                        url: "${pageContext.request.contextPath}/service/getenterpriseinfo",
                        data: { esn: strSn},
                        type: "post",
                        cache: false,
                        beforeSend: function () {
                            BlockUI();
                        },
                        success: function (data) {
                            if(data != null){
                            	var netcallinfo = $.evalJSON(data.innerNetcallAdr);
                            	$("#InnerNetcallAdr").val(netcallinfo.url);
                            	$("#LoginPort").val(netcallinfo.loginport);
                            	$("#FileServerPort").val(netcallinfo.fileserverport);
                            	$("#PluginPort").val(netcallinfo.pluginport);
                            	$("#HostName").val(netcallinfo.hostname);
                            	
                            	$("#ProductAdr").val(data.productAdr);
                            	$("#ContactPhone").val(data.contactPhone);
                            	ValidateInput()
                            }
                        },
                        complete: function () {
                           	UnBlockUI();
                        },
				        error: function (errText) {
				            AlertMsg(errText.responseText);
				        }

                    });
                }
                else {//加密锁
                    AlertMsg("未能检测到加密锁信息");
                }
            }
            else {//未检测到加密锁
                AlertMsg("未能检测到加密锁");
            }       	
        }
        
        function UnBlockUI() {
	    	$.unblockUI();
		}

		//锁屏
		function BlockUI(message) {
		    if (message == undefined || message == "") {
		    	message = "加载中，请稍候...";
		    };
		
		    var imgpath = '../resource/img/panel_loading.gif';
		
		    $.blockUI({
		        message: "<div><table style='font-size: 12px'><tr><td style='width:16px;'><img src=" + imgpath + "></img></td><td style='text-align: center;'>" + message + "</td></tr></table></div>",
		        css: {
		            border: 'none',
		            fadeIn: 1000,
		            padding: '8px',
		            backgroundColor: '#fff',
		            '-webkit-border-radius': '10px',
		            '-moz-border-radius': '10px',
		            opacity: .8,
		            color: '#000',
		            width: '200px',
		            left: ($("#selfregisterform").width() - 200) / 2 + 'px',
		            top: ($("#selfregisterform").height() - 50) / 2 + 'px',
		            border: '2px solid #8E8E8E',
		            position: 'absolute'
		        },
		        overlayCSS: { backgroundColor: "#9D9D9D", opacity: 0.5 }
		    });
		}
		
		function AlertMsg(msg) {
    		$.messager.alert("提醒", "<br>" + msg, "info");
		}
		
		//校验输入
		function ValidateInput(){
			var flag = true;
			$('#selfregisterform input').each(function () {
                if ($(this).attr('required') || $(this).attr('validType')) {
                    if (!$(this).validatebox('isValid')) {
                        flag = false;
                        return flag;
                    }
                }
            })
            return flag;
		}

		//注册
		function Register(){
            if(!ValidateInput()){
            	AlertMsg("请填写完整");
            	return;
            }

			var json = "{'url':" + $('#InnerNetcallAdr').val() + ",'loginport':'" 
				+ $('#LoginPort').val() + "','fileserverport':" + $('#FileServerPort').val() + 
				"','pluginport':'" + $('#FileServerPort').val() + "','hostname':'" + $('#HostName').val() + "'}";
		     $.ajax({
                    url: '${pageContext.request.contextPath}/service/updateenterpriseinfo',
                    data: {enterpriseSn: strSn, productAdr: $('#ProductAdr').val(), 
                    innerNetcallAdr: json,//$('#InnerNetcallAdr').val(), 
                    enterpriseName:$('#UserName').val(),
                    contactPhone:$('#ContactPhone').val(),
                    },
                    type: "post",
                    cache: false,
                    beforeSend: function () {
                        BlockUI('注册信息提交中...');
                    },
                    success: function (data) {
                        if(data == "1"){
                        	AlertMsg('注册成功');
                        }
                        else{
                        	AlertMsg('注册失败');
                        }
                    },
                    error: function (returnhttp, errstring, errtype) {
                        AlertMsg('注册失败');
                    },
                    complete: function () {
                        UnBlockUI();
                    }

                });
		}
		
		
     </script>
</head>
<body>
    <form name="form1" method="post" action="${pageContext.request.contextPath}/service/updateenterpriseinfo" id="form1">
    <div id="selfregisterform">
        <table width="855" border="0" align="center" cellspacing="0">
            <tr>
                <td valign="top" class="register_table_top">
                    欢迎注册新中大产品
                </td>
            </tr>
            <tr>
                <td valign="top" class="register_table_body" style="position: relative;">
                    <table width="797" border="0" align="center" cellpadding="0" cellspacing="0" style="margin-top: 12px;">
                        <tr>
                            <td height="35" colspan="2" class="register_table_tabtitle">
                                <b>客户信息</b>
                            </td>
                        </tr>
                         <tr>
                            <td width="180" height="30" align="right" class="lable_reg">加密锁号&nbsp;&nbsp;
                            </td>
                            <td>
                                <input class="easyui-validatebox cominput" id="SN" maxlength="100" name="SN"
                                    disabled="disabled" style="width: 250px;" type="text"/>
                            </td>
       					</tr>
                        <tr>
                            <td width="180" height="30" align="right" class="lable_reg">客户名称&nbsp;&nbsp;
                            </td>
                            <td>
                                <input class="easyui-validatebox cominput" id="UserName" maxlength="100" name="UserName"
                                    disabled="disabled" style="width: 250px;" type="text"/>
                            </td>
       					</tr>
       					<tr>
                            <td width="180" height="30" align="right" class="lable_reg">管理员联系号码<font color="#FF0000">&nbsp;*&nbsp;</font>
                            </td>
                            <td>
                                <input class="easyui-validatebox cominput" id="ContactPhone" maxlength="150" name="ContactPhone"
                                    required="true" style="width: 250px;" type="text" />
                            </td>
                        </tr>
                    </table>
                    
                    <table width="797" border="0" align="center" cellpadding="0" cellspacing="0" style="margin-top: 12px;">
                        <tr>
                            <td height="35" colspan="2" class="register_table_tabtitle">
                                <b>产品信息</b>&nbsp;&nbsp; <font color="#2F4F4F" style="font-weight: normal">(请如实谨慎填写信息)</font>
                            </td>
                        </tr>
                        <tr>
                            <td width="180" height="30" align="right" class="lable_reg">企业Netcall地址<font color="#FF0000">&nbsp;*&nbsp;</font>
                            </td>
                            <td>
                                <input class="easyui-validatebox cominput" id="InnerNetcallAdr" maxlength="150" name="InnerNetcallAdr"
                                    required="true" style="width: 250px;" type="text" />
                            </td>
                        </tr>
                         <tr>
                            <td width="180" height="30" align="right" class="lable_reg">企业Netcall主机名<font color="#FF0000">&nbsp;*&nbsp;</font>
                            </td>
                            <td>
                                <input class="easyui-validatebox cominput" id="HostName" maxlength="150" name=""HostName""
                                    required="true" style="width: 250px;" type="text" />
                            </td>
                        </tr>
                          <tr>
                            <td width="180" height="30" align="right" class="lable_reg">Netcall登录端口<font color="#FF0000">&nbsp;*&nbsp;</font>
                            </td>
                            <td>
                                <input class="easyui-validatebox cominput" id="LoginPort" maxlength="150" name="LoginPort"
                                    required="true" style="width: 250px;" type="text" value="7070" />
                            </td>
                        </tr>
                          <tr>
                            <td width="180" height="30" align="right" class="lable_reg">Netcall文件服务器端口<font color="#FF0000">&nbsp;*&nbsp;</font>
                            </td>
                            <td>
                                <input class="easyui-validatebox cominput" id=FileServerPort" maxlength="150" name="FileServerPort"
                                    required="true" style="width: 250px;" type="text" value="8096"/>
                            </td>
                        </tr>
                          <tr>
                            <td width="180" height="30" align="right" class="lable_reg">Netcall插件端口<font color="#FF0000">&nbsp;*&nbsp;</font>
                            </td>
                            <td>
                                <input class="easyui-validatebox cominput" id="PluginPort" maxlength="150" name="PluginPort"
                                    required="true" style="width: 250px;" type="text" value="9090"/>
                            </td>
                        </tr>
                         <tr>
                            <td width="180" height="30" align="right" class="lable_reg">企业产品地址<font color="#FF0000">&nbsp;*&nbsp;</font>
                            </td>
                            <td>
                                <input class="easyui-validatebox cominput" id="ProductAdr" maxlength="150" name="ProductAdr"
                                    required="true" style="width: 250px;" type="text" />
                            </td>
                        </tr>
                        <!-- 
                        <tr>
                            <td width="180" height="30" align="right" class="lable_reg">企业帐套<font color="#FF0000">&nbsp;*&nbsp;</font>
                            </td>
                            <td>
                                <input class="easyui-validatebox cominput" id="EnterpriseCode" maxlength="150" name="EnterpriseCode"
                                    required="true" style="width: 250px;" type="text" />
                                <font color="#FF0000">（如0001）</font>
                            </td>
                        </tr> -->
                  </table>
                    <table width="797" height="67" border="0" align="center" cellpadding="0" cellspacing="0">
                        <tr>
                            <td align="left" style="margin-left: 115px;">
                                <span id="errinfo" style="font-weight: bold; color: #FF0000;"></span>
                            </td>
                        </tr>
                        <tr>
                            <td align="center" height="20">
                            </td>
                        </tr>
                        <tr>
                            <td align="center">
                                <img id="butSubmitJoinMember" src="../resource/img/submit00.gif" style="cursor: pointer;"
                                    onclick="Register()" border="0" />
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td class="register_table_bottom">
                </td>
            </tr>
        </table>

    </div>
    <!--下面的div中的内容在使用USB验证时是必须的，且当前只支持IE浏览器-->
    <div id="unvisibleActiveXStock" style="display: none;">
       <object id="usblock" name="usblock" codebase="../resource/usblock.CAB#Version=1,0,0,2"
            classid="clsid:2B81FD28-49BE-413B-B16E-6A6CA638CB49" height="1" width="99%" viewastext>
        </object>      
    </div>
    </form>
</body>

</html>
