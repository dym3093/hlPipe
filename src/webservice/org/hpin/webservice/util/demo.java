package org.hpin.webservice.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.huawei.uds.services.UdsConfiguration;
import com.huawei.uds.services.UdsService;
import com.huawei.uds.services.exception.UdsException;
import com.huawei.uds.services.model.PutObjectResult;
import com.huawei.uds.services.model.S3Bucket;
import com.huawei.uds.services.model.S3Object;

public class demo {
	// 指定用户自己的AK，SK
	public final static String AK = "CF7A0CEE5D274031457D";
	public final static String SK = "0QAEGXeCL9nfqTv2eGUZ8YB+75cAAAFSXSdAMY5a";
	public final static String Server = "s3.hwclouds.com";

	public static void main(String args[]) throws UdsException, IOException {
		// 初始化环境
		long t1=System.currentTimeMillis();
		UdsConfiguration config = new UdsConfiguration();
		config.setEndPoint(Server);
		config.setEndpointHttpPort(5080);
		config.setHttpsOnly(false);
		config.setDisableDnsBucket(true);
		UdsService service = new UdsService(AK, SK, config);
		long t11=System.currentTimeMillis();
		System.out.println("init times---"+(t11-t1));
		// 创建桶
		//S3Bucket bucket = service.createBucket("lqtest");
		// 上传对象
		String filePath = "E:/SY-32737.pdf";
		File file = new File(filePath);
		PutObjectResult result = service.putObject("lqtest/20160215", file.getName(),
				file);	
		long t2=System.currentTimeMillis();
		System.out.println("push used times---"+(t2-t1)+"--"+result.getEtag());
		// 下载对象		
		S3Object obj = service.getObject("lqtest/20160215", "SY-32737.pdf",null);
		if (obj != null) {
			InputStream inStream = obj.getObjectContent();
			OutputStream outStream = new FileOutputStream("e:/copy"
					+ "SY-32737.pdf"); //

			byte[] buffer = new byte[65563];
			int count;
			while ((count = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, count);
			}
			outStream.close();
		}
		long t3=System.currentTimeMillis();
		System.out.println("get used times---"+(t3-t2));
		
	}
}