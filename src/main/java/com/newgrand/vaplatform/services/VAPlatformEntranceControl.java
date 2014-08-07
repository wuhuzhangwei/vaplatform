package com.newgrand.vaplatform.services;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.newgrand.vaplatform.filesrv.OSSObjectHelper;
import com.newgrand.vaplatform.helper.ConstantHelper;
import com.newgrand.vaplatform.helper.LogHelper;

@Controller
@RequestMapping("/auth")
public class VAPlatformEntranceControl {
	//@Autowired
	//private NGNetcallAdDao ngNetcallAdDao;

	/**
	 * 获取授权用户列表
	 * @param esn
	 * @param stat
	 * @return
	 */
	@RequestMapping(value = "/checkenabled", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String checkEnabled(String product) {
		try{
			return "0";
		}
		catch (Exception e) {
			LogHelper.error(e.getMessage());
			return "0";
		}
	}
	
	/**
	 * 获取授权用户列表
	 * 
	 * @param esn
	 * @param content
	 * @return
	 */
	@RequestMapping(value = "/uploaddata", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String uploadData(HttpServletRequest request) {
		try {
			if (request instanceof MultipartHttpServletRequest) {
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				CommonsMultipartFile myfile = (CommonsMultipartFile) multipartRequest.getFile("file");
				boolean allsuccess = false;
				allsuccess = OSSObjectHelper.uploadFile(myfile.getName(), myfile.getBytes());
				if (allsuccess) {
					return "1";
				}
			}
			return "0";
		} catch (Exception e) {
			LogHelper.error(e.getMessage());
			return "0";
		}
	}
	
	/**
	 * 获取授权用户列表
	 * 
	 * @param esn
	 * @param content
	 * @return
	 */
	@RequestMapping(value = "/getproperty", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String getProperty(int arg0, Boolean arg1, String arg2, String arg3) {
		try {
			VAPlatformSyncService.getProperties(arg2);
			return "0";
		} catch (Exception e) {
			LogHelper.error(e.getMessage());
			return "0";
		}
	}
	
}
