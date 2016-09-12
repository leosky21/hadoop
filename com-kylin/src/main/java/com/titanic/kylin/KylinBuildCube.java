package com.titanic.kylin;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;

/**
 * kylin构建cube
 * 未测试
 *
 * kylin各种api请求
 * http://www.cnblogs.com/dreamfactory/p/5588203.html
 *
 */
public class KylinBuildCube
{
    static String ACCOUNT = "ADMIN";
    static String PWD = "KYLIN";
    static String PATH = "http://192.168.1.26:7070/kylin/api/cubes/zh_cube_biz_ca_mi/rebuild";

    public static void main(String[] args)
    {
        System.out
                .println(Put(
                        PATH,
                        "{\"startTime\": 1451750400000,\"endTime\": 1451836800000,\"buildType\": \"BUILD\"}"));
    }

    public static String Put(String addr, String params)
    {
        String result = "";
        try
        {
            URL url = new URL(addr);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("PUT");
            connection.setDoOutput(true);
            String auth = ACCOUNT + ":" + PWD;
            String code = new String(new Base64().encode(auth.getBytes()));
            connection.setRequestProperty("Authorization", "Basic " + code);
            connection.setRequestProperty("Content-Type",
                    "application/json;charset=UTF-8");
            PrintWriter out = new PrintWriter(connection.getOutputStream());
            out.write(params);
            out.close();
            BufferedReader in;
            try
            {
                in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
            } catch (FileNotFoundException exception)
            {
                java.io.InputStream err = ((HttpURLConnection) connection)
                        .getErrorStream();
                if (err == null)
                    throw exception;
                in = new BufferedReader(new InputStreamReader(err));
            }
            StringBuffer response = new StringBuffer();
            String line;
            while ((line = in.readLine()) != null)
                response.append(line + "\n");
            in.close();

            result = response.toString();
        } catch (MalformedURLException e)
        {
            System.err.println(e.toString());
        } catch (IOException e)
        {
            System.err.println(e.toString());
        }
        return result;
    }

}
