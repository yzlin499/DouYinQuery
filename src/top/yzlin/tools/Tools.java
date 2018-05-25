package top.yzlin.tools;
import net.sf.json.JSONObject;
import top.yzlin.netInterface.IOExceptionSolution;
import top.yzlin.netInterface.SetConnection;

import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;


public class Tools {
    private static final SimpleDateFormat df = new SimpleDateFormat("[MM-dd HH:mm:ss] ");
    private static MessageDigest md;
    static{
        try {
            md = MessageDigest.getInstance("MD5");
            File temp = new File("doc\\log");
            if (!temp.exists()) {
                temp.mkdir();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    /**
     * 将Unicode转换
     */
    public static String asciiToNative(String asciicode) {
        String[] asciis = asciicode.split("\\\\u");
        String nativeValue = asciis[0];
        try {
            for (int i = 1; i < asciis.length; i++) {
                String code = asciis[i];
                nativeValue += (char) Integer
                        .parseInt(code.substring(0, 4), 16);
                if (code.length() > 4) {
                    nativeValue += code.substring(4, code.length());
                }
            }
        } catch (NumberFormatException e) {
            return asciicode;
        }
        return nativeValue;
    }


    public static String sendGet(String url, String param) {
        return sendGet(url,param, conn -> {
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        });
    }



    /**
     * 获取GET数据
     */
    public static String sendGet(String url, String param,SetConnection connections) {
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) new URL(url + "?" + param).openConnection();
            connections.setConnection(connection);
            HttpURLConnection.setFollowRedirects(true);
            connection.setInstanceFollowRedirects(false);
            connection.connect();
        } catch (IOException e) {
            Tools.print("get的网络区炸了，10秒之后重新获取");
            Tools.sleep(10000);
            return Tools.sendGet(url, param,connections);
        }
        try (BufferedReader in= new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf8"))){
            String line;
            String result="";
            while ((line = in.readLine()) != null) {
                result += line;
            }
            return result;
        } catch (IOException e ) {
            Tools.print("get的读写区炸了，10秒之后重新获取");
            Tools.sleep(10000);
            return Tools.sendGet(url, param,connections);
        }
    }







    /**
     * post数据
     * @param url 网址
     * @param param 参数
     * @return 数据
     */
    public static String sendPost(String url, String param){
        return sendPost(url,param, conn -> {
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        });
    }

    /**
     * 获取post数据，可自定义头文件
     * @param url 网址
     * @param param 参数
     * @param m 用map图来添加各项数据
     * @return 数据
     */
    public static String sendPost(String url, String param,Map<String,String> m){
        return sendPost(url,param,conn->{
            for(String key:m.keySet()){
                conn.setRequestProperty(key,m.get(key));
            }
        });
    }

    /**
     * 获取post数据，可自定义头文件
     * @param url 网址
     * @param param 参数
     * @param connections 头文件接口
     * @return 数据
     */
    public static String sendPost(String url, String param,SetConnection connections){
        return sendPost(url,param,connections,null);
    }

    /**
     * 获取post数据，可自定义头文件与异常处理方法
     * @param url 网址
     * @param param 参数
     * @param connections 头文件接口
     * @param solution 处理方法
     * @return 数据
     */
    public static String sendPost(String url, String param, SetConnection connections, IOExceptionSolution solution){
        URLConnection conn;
        try {
            conn = new URL(url).openConnection();
            connections.setConnection(conn);
            conn.setDoOutput(true);
            conn.setDoInput(true);
        } catch (IOException e) {
            if(solution!=null){
                solution.exceptionSolution(e);
            }
            Tools.print("post的网络区炸了，10秒之后重新获取");
            Tools.sleep(10000);
            return Tools.sendPost(url,param,connections,solution);
        }
        try(PrintWriter out = new PrintWriter(conn.getOutputStream())){
            out.print(param);
            out.flush();
        }catch (IOException e) {
            if(solution!=null){
                solution.exceptionSolution(e);
            }
            Tools.print("post的PrintWriter读写区炸了，10秒之后重新获取");
            Tools.sleep(10000);
            return Tools.sendPost(url,param,connections,solution);
        }
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf8"))){
            String line;
            String result="";
            while ((line = in.readLine()) != null) {
                result += line;
            }
            return result;
        } catch (IOException e) {
            if(solution!=null){
                solution.exceptionSolution(e);
            }
            Tools.print("post的BufferedReader读写区炸了，10秒之后重新获取");
            Tools.sleep(10000);
            return Tools.sendPost(url,param,connections,solution);
        }
    }

    /**
     * 获取短网址，使用的是新浪的接口，t.cn
     * @param url
     * @return
     */
    public static String getTinyURL(String url){
        String temp = sendGet("http://api.t.sina.com.cn/short_url/shorten.json","source=3271760578&url_long="+url);
        if(temp==null){
            return null;
        }
        JSONObject t=JSONObject.fromObject(temp.substring(1,temp.length()-1));
        return t.getString("url_short");
    }


    /**
     * 自定义的打印方法，输出当前时间，并且输出test
     */
    public static void print(){
        print("test");
    }

    /**
     * 自定义的打印方法，输出当前时间，用来做日记打印
     * @param text
     */
    public static void print(Object text){
        System.out.println(df.format(new Date())+text.toString());
    }

    /**
     * 用来做数组输出，一般是做测试用
     * @param text
     */
    public static void printArrays(Object[]text){
        print("数组共"+text.length+"个");
        for(Object temp:text){
            System.out.println(temp.toString());
        }
    }

    /**
     * 简易时间停止
     * @param millis
     */
    public static void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算MD5
     * @param str 要计算的字符串
     */
    public static String MD5(String str){
        try {
            md.update(str.getBytes("utf8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String num=new BigInteger(1, md.digest()).toString(16);
        while(num.length()<32){
            num="0"+num;
        }
        return num;
    }

    /**
     * 计算当天剩余时间
     * @return 毫秒
     */
    public static long todayRemainTime(){
        long t= Clock.systemDefaultZone().millis()+TimeZone.getDefault().getRawOffset();
        return 86400000-(t%86400000);
    }
}
