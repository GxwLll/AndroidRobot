package cn.gxw.zero.androidrobot.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import cn.gxw.zero.androidrobot.app.Constants;

/**
 * Created by Administrator on 2017/1/11 0011.
 */

public class HttpUtils {

    public static String getRespones(String msg){
        String result = doGet(setParams(msg));

        return result;
    }



    //拼接url
    private static  String setParams(String msg){
        if ( msg == null)
            msg = "";

        try {
            msg = URLEncoder.encode(msg,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    return Constants.URL+msg;
    }
    //get请求
    private static String doGet(String urlstr) {

        URL url = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            url = new URL(urlstr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5 * 1000);
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                baos = new ByteArrayOutputStream();
                int len = -1;
                byte[] buf = new byte[128];

                while ((len = is.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
                baos.flush();
                return baos.toString();
            } else {
                throw new CommonException("服务器连接错误！");
            }

        } catch (Exception e) {
            e.printStackTrace();
            try {
                throw new CommonException("服务器连接错误！");
            } catch (CommonException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            conn.disconnect();
        }
        return "";
    }


}
