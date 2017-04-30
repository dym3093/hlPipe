package org.hpin.webservice.foreign;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hpin.common.core.SpringTool;
import org.hpin.webservice.service.YmGeneReportServiceImpl;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * 用于测试HTTP请求
 * Created by root on 17-2-8.
 */
public class InfoImgServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Logger log = Logger.getLogger("getGeneReportInfoImg");
        //接收到的请求数据
        String reqStr;
        //请求的JSON
        JSONObject reqJson;

        String name = null;
        String tel = null;
        String birthday= null;

        try {
            //JSON格式传送
            request.setCharacterEncoding("UTF8");
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), "utf-8"));
            StringBuilder sb = new StringBuilder();

            String temp;
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }

            if(StringUtils.isNotEmpty(sb.toString())){
                reqStr = sb.toString();
                log.info("接收到的请求数据  reqStr: "+reqStr);
                reqJson = new JSONObject(reqStr);
                if (reqJson!=null){
                    if (reqJson.has("name")){
                        name = reqJson.getString("name");
                    }
                    if (reqJson.has("tel")){
                        tel = reqJson.getString("tel");
                    }
                    if (reqJson.has("birthday")){
                        birthday = reqJson.getString("birthday");
                    }
                }
            } else {
                //URL中传参数
                name = new String(request.getParameter("name").getBytes("ISO-8859-1"),"utf-8");
                tel = new String(request.getParameter("tel").getBytes("ISO-8859-1"),"utf-8");
                birthday = new String(request.getParameter("birthday").getBytes("ISO-8859-1"),"utf-8");
            }
            log.info("接收到微服务的数据： name="+name+", tel="+tel+", birthday="+birthday);
            YmGeneReportServiceImpl ymGeneReportService = (YmGeneReportServiceImpl) SpringTool.getBean(YmGeneReportServiceImpl.class);
            String respStr = ymGeneReportService.getGeneReportInfoImg(name,tel, birthday);
            writeToClient(response, respStr);
        } catch (JSONException e) {
            log.info(e);
        }
    }

    /**
     * 将字符串传到客户端
     * @param response
     * @param content
     * @throws IOException
     */
    public void writeToClient(HttpServletResponse response, String content) throws IOException {
        response.setHeader("content-type", "text/html;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(content);
    }
}
