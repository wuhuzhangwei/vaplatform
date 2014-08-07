package com.newgrand.vaplatform.filesrv;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.aliyun.openservices.ClientException;
import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.OSSException;
import com.aliyun.openservices.oss.model.CannedAccessControlList;
import com.aliyun.openservices.oss.model.GetObjectRequest;
import com.aliyun.openservices.oss.model.OSSObject;
import com.aliyun.openservices.oss.model.OSSObjectSummary;
import com.aliyun.openservices.oss.model.ObjectListing;
import com.aliyun.openservices.oss.model.ObjectMetadata;
import com.newgrand.log4ng.ngLoggerFactory;
import com.newgrand.log4ng.ngLoggerFactory.loggerType;
import com.newgrand.vaplatform.helper.LogHelper;
import com.newgrand.vaplatform.helper.PropertiesHelper;
import com.newgrand.vaplatform.helper.StreamHelper;

/**
 * 该示例代码展示了如果在OSS中创建和删除一个Bucket，以及如何上传和下载一个文件。
 * 
 * 该示例代码的执行过程是：
 * 1. 创建一个Bucket（如果已经存在，则忽略错误码）；
 * 2. 上传一个文件到OSS；
 * 3. 下载这个文件到本地；
 * 4. 清理测试资源：删除Bucket及其中的所有Objects。
 * 
 * 尝试运行这段示例代码时需要注意：
 * 1. 为了展示在删除Bucket时除了需要删除其中的Objects,
 *    示例代码最后为删除掉指定的Bucket，因为不要使用您的已经有资源的Bucket进行测试！
 * 2. 请使用您的API授权密钥填充ACCESS_ID和ACCESS_KEY常量；
 * 3. 需要准确上传用的测试文件，并修改常量uploadFilePath为测试文件的路径；
 *    修改常量downloadFilePath为下载文件的路径。
 * 4. 该程序仅为示例代码，仅供参考，并不能保证足够健壮。
 *
 */
public class OSSObjectHelper {

	private static final String ACCESS_ID = "DayuYmYZgH8bmNPz";
    private static final String ACCESS_KEY = "dIb5A4gom788ewcjGBcdhSYCXJaCAN";
    private static OSSClient ossClient = null;
    
    
    static{
    	Properties property = PropertiesHelper.getProperties("/config/environment.properties");
		if(property != null){
			String urlString = property.getProperty("ngfilesrv.url", "http://oss-cn-hangzhou-internal.aliyuncs.com");
			ossClient = new OSSClient(urlString, ACCESS_ID, ACCESS_KEY);
		}
    }
    
//    public static void main(String[] args) throws Exception {
//        String bucketName = "mytest2014";//ACCESS_ID + "-object-test";
//        String key = "photo1.jpg";
//
//        String uploadFilePath = "d:/temp/photo.jpg";
//        String downloadFilePath = "d:/temp/photo1.jpg";
//        BufferedReader strin = new BufferedReader(new InputStreamReader(System.in));
//        // 使用默认的OSS服务器地址创建OSSClient对象。
//        
//        //ensureBucket(client, bucketName);
//
//        try {
//            //setBucketPublicReadable(client, bucketName);
//        	listObjects(client, bucketName);
//            System.out.println("上传一个文件（如d:/data.txt）：");
//            uploadFilePath = strin.readLine();
//            if(uploadFilePath != ""){
//            	int index = uploadFilePath.lastIndexOf("/");
//            	if(index > 0){
//            		key = uploadFilePath.substring(index+1);
//            		uploadFile(client, bucketName, key, uploadFilePath);
//            	}
//            }
//            listObjects(client, bucketName);
//            System.out.println("下载一个文件（如data.txt d:/data1.txt）");
//            String input = strin.readLine();
//            String[] splits = input.split(" ");
//            if(splits.length >= 2){
//            	downloadFile(client, bucketName, splits[0], splits[1]);
//            }
//        } finally {
//            //deleteBucket(client, bucketName);
//        }
//    }

    private static OSSClient getOSSClient() {
		return ossClient;
	}
    
    public static String getBucketName(){
    	return "ngauthdata";
    }
    
    // 删除一个Bucket和其中的Objects 
    public static void deleteBucket(OSSClient client, String bucketName)
            throws OSSException, ClientException {

        ObjectListing ObjectListing = client.listObjects(bucketName);
        List<OSSObjectSummary> listDeletes = ObjectListing
                .getObjectSummaries();
        for (int i = 0; i < listDeletes.size(); i++) {
            String objectName = listDeletes.get(i).getKey();
            // 如果不为空，先删除bucket下的文件
            client.deleteObject(bucketName, objectName);
        }
        client.deleteBucket(bucketName);
    }

    // 把Bucket设置为所有人可读
    public static void setBucketPublicReadable(OSSClient client, String bucketName)
            throws OSSException, ClientException {
        //创建bucket
        client.createBucket(bucketName);

        //设置bucket的访问权限，public-read-write权限
        client.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
    }

    // 上传文件
    public static boolean uploadFile(String key, byte[] bts){
        //File file = new File(filename);
    	InputStream input = null;
    	try {
    		OSSClient client = getOSSClient();
            String bucketName = getBucketName();
            ObjectMetadata objectMeta = new ObjectMetadata();
            objectMeta.setContentLength(bts.length);
            // 可以在metadata中标记文件类型
            input = new ByteArrayInputStream(bts);
            client.putObject(bucketName, key, input, objectMeta);
            return true;
		} catch (Exception e) {
			//LogHelper.error(e);
			ngLoggerFactory.getLogger(loggerType.filesrv).error(e.getMessage(),e);
			return false;
		}
    	finally{
    		if(input != null){
    			try {
    				input.close();
				} catch (Exception e2) {
					ngLoggerFactory.getLogger(loggerType.filesrv).error(e2.getMessage(),e2);
				}
    			
    		}
    	}
    }

    /**
     * 下载文件
     * @param bucketName
     * @param key
     * @param filename
     * @return
     * @throws OSSException
     * @throws ClientException
     */
    public static byte[] downloadFile(String bucketName, String key)
            throws OSSException, ClientException {
    	InputStream inputStream = null;
    	try{
    		OSSClient client = getOSSClient();
	    	OSSObject object = client.getObject(new GetObjectRequest(bucketName, key));
	    	inputStream = object.getObjectContent();
	    	byte[] bts = StreamHelper.readStream(inputStream);
	    	return bts;
    	}
    	catch(Exception ex){
    		//LogHelper.error(ex);
    		ngLoggerFactory.getLogger(loggerType.control).error(ex.getMessage(),ex);
    		return null;
    	}
    	finally{
    		if(inputStream != null){
    			try {
    				inputStream.close();
    			} catch (Exception e2) {
    				ngLoggerFactory.getLogger(loggerType.filesrv).error(e2.getMessage(),e2);
    			}
    			
    		}
    	}
    	
    }
    
    /**
     * 列出所有的文件
     * @param client
     * @param bucketName
     * @return
     */
    public static List<String> listAllObjects() {
        // 获取指定bucket下的所有Object信息
    	String bucketName = getBucketName();
    	OSSClient client = getOSSClient();
        ObjectListing listing = client.listObjects(bucketName);
        List<String> keys = new ArrayList<String>();
        // 遍历所有Object
        for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
        	keys.add(objectSummary.getKey());
        	//System.out.println(objectSummary.getKey());
        }
        return keys;
    }
}
