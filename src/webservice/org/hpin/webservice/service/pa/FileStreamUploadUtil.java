package org.hpin.webservice.service.pa;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hpin.webservice.util.PropertiesUtils;

import com.alibaba.fastjson.JSON;

/**
 * Created by fangliang on 17/2/10.
 */
public class FileStreamUploadUtil {

    // UOS平台的URL ，平安好医生提供 测试环境
//    private static String http_url = "http://srv.test.pajk.cn/uos";
    // UOS平台的URL ，平安好医生提供  正式环境
    private static String http_url = "http://srv.jk.cn/uos";
    // 上传file流使用"u"
    private static final String upload = "u";
    // http content type
    private static final String CONTENT_TYPE_MULTIPART = "multipart/form-data";
    
    /**
     * 通过UOS平台上传文件流
     * @throws Exception 
     */
    public static String uploadFileStream(String pathFile) throws Exception {
    	Logger log = Logger.getLogger("geneServicePA");
        HashMap<String, String> map = new HashMap<String, String>();

        //1。从平安健康获取数据
//        String partnerId = "pinganupload"; // 合作伙伴的partnerId
//        String hexKey = "a73bae4fb38b9f9bace812c740848b83";// 合作伙伴的加解密和签名使用的key
        String hexKey = PropertiesUtils.getString("pingan","pingan.key");
		String partnerId = PropertiesUtils.getString("pingan","pingan.partnerid");
        String saveClouds = "1"; // 存放云的位置 0是公有云，1是私有云
        //2. 对 URL中的参数进行加密签名
        map.put("sc", saveClouds);
        String requestParam = RequestEncoder.encode(hexKey, map, partnerId, upload);
        //3。 文件流的转换
        byte[] reqBody = getFileStream(pathFile);
        //4. 发送http请求，并获取fileKey
        String fileKey = uploadPost(requestParam, reqBody, hexKey);
        log.info("fileKey:"+fileKey);
        return fileKey;
    }

    /**
     * 发送http请求
     *
     * @param urlParams
     * @param reqBody
     * @param hexKey
     * @return
     * @throws Exception 
     */
    public static String uploadPost(String urlParams, byte[] reqBody, String hexKey) throws Exception {
    	Logger log = Logger.getLogger("geneServicePA");
        String fileKey = "";
        URL url = new URL(http_url + "?" + urlParams);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setDoOutput(true);
        urlConnection.setConnectTimeout(5 * 1000);
        urlConnection.setRequestProperty("content-type", CONTENT_TYPE_MULTIPART);
        urlConnection.connect();
        BufferedOutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
        // 把数据写入请求的Body
        out.write(reqBody);
        out.flush();
        out.close();

        InputStream inputStream = urlConnection.getInputStream();
        String body = convertStream2Json(inputStream);
        log.info("uploadPost---body:"+body);
        Map obj = JSON.parseObject(body, Map.class);
        if ((Integer)obj.get("code") == 0) { //请求成功
            ResponseDecoder decoder = new ResponseDecoder(hexKey);
            decoder.decode(obj.get("content").toString());
            fileKey = decoder.getData();
            log.info("uploadPost---obj:"+obj.toString());
        } else { // 请求发送失败
            // 错误异常处理
        	 log.error("uploadPost---body:"+body);
        }
        return fileKey;
    }

    private static String convertStream2Json(InputStream inputStream) {
        String jsonStr = "";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        try {
            while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
                out.write(buffer, 0, len);
            }
            jsonStr = new String(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }

    /**
     * get fiel stream
     *
     * @param filePath
     * @return
     */
    private static byte[] getFileStream(String filePath) {
        FileInputStream is;
        byte data[];

        try {
            is = new FileInputStream(filePath);
            int i = is.available();
            data = new byte[i];
            is.read(data); // 读数据
            is.close();
        } catch (IOException e) {
            System.out.println("IOException=" + e.getMessage());
            return null;
        }

        return data;
    }
}
