package com.deer.wms.project.root.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpClientUtil {

    public static String doGet(String url, HttpGet httpGet) {
        HttpClient httpClient;

        String result = null;
        try {
            BasicHttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 7000);
            HttpConnectionParams.setSoTimeout(httpParams, 700000);
            HttpConnectionParams.setSoKeepalive(httpParams, true);
            httpClient = new SSLClient(httpParams);

            HttpResponse response = httpClient.execute(httpGet);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                byte[] byteData = EntityUtils.toByteArray(resEntity);
                result = byteToString(byteData);
            }
        } catch (Exception e) {
            log.error("调用访问https工具类, 抛错：", e);
        }
        return result;
    }

    public static HttpResponse doHttp(HttpRequestBase httpRequest) {
        try {
            BasicHttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 7000);
            HttpConnectionParams.setSoTimeout(httpParams, 700000);
            HttpConnectionParams.setSoKeepalive(httpParams, true);
            HttpClient httpClient = new SSLClient(httpParams);
            return httpClient.execute(httpRequest);
        } catch (Exception e) {
            throw new RuntimeException("发送HTTP工具出错", e);
        }
    }

//    public static String doPost(String url, String json) {
//        HttpClient httpClient;
//        HttpPost httpPost;
//        String result = null;
//        try {
//            BasicHttpParams httpParams = new BasicHttpParams();
//            HttpConnectionParams.setConnectionTimeout(httpParams, 7000);
//            HttpConnectionParams.setSoTimeout(httpParams, 700000);
//            HttpConnectionParams.setSoKeepalive(httpParams, true);
//            httpClient = new SSLClient(httpParams);
//            httpPost = new HttpPost(url);
//            StringEntity entity = new StringEntity(json, Charsets.UTF_8);//解决中文乱码问题
//            entity.setContentEncoding("UTF-8");
//            entity.setContentType("application/json");
//            httpPost.setEntity(entity);
//            httpPost.setHeader("Accept","application/json");
//            httpPost.setHeader("Content-Type", "application/json");
//
//            httpPost.setHeader("Username", "SCC4V76F6");
//            httpPost.setHeader("Password", "Password@123");
//            httpPost.setHeader("AccessLicenseNumber", "9D8EABB1E2EBD932");
//          /*  httpPost.setHeader("transId", "deer12343215");
//            httpPost.setHeader("transactionSrc", "deer12343215");*/
//            HttpResponse response = httpClient.execute(httpPost);
//
//
//           // ResponseEntity<String>   response2 = httpClient.execute(httpPost);
//            if (response != null) {
//                JSONObject afsadf =  GetHttpEntity(response);
//                result = afsadf.toString();
//               // String res = response.getBody();
//                //HttpEntity resEntity = response.getEntity();
////                if (resEntity != null) {
////                    result = toString(resEntity, Charsets.UTF_8);
////                }
//            }
//        } catch (Exception e) {
//
//            logger.error("调用访问https工具类, 抛错：", e);
//        }
//        return result;
//    }

    public static String toString(HttpEntity entity, Charset defaultCharset) throws IOException, ParseException {
        Args.notNull(entity, "Entity");
        ContentType contentType = null;

        try {
            contentType = ContentType.get(entity);
        } catch (UnsupportedCharsetException var4) {
            if (defaultCharset == null) {
                throw new UnsupportedEncodingException(var4.getMessage());
            }
        }

        if (contentType != null) {
            if (contentType.getCharset() == null) {
                contentType = contentType.withCharset(defaultCharset);
            }
        } else {
            contentType = ContentType.DEFAULT_TEXT.withCharset(defaultCharset);
        }

        String gaaa = toString(entity, contentType);
        return gaaa;
    }

    private static String toString(HttpEntity entity, ContentType contentType) throws IOException {
        InputStream instream = entity.getContent();
        if (instream == null) {
            return null;
        } else {
            try {
                Long lenght = entity.getContentLength();
                Args.check(lenght <= 2147483647L, "HTTP entity too large to be buffered in memory");
                int i = (int) entity.getContentLength();
                if (i < 0) {
                    i = 4096;
                }

                Charset charset = null;
                if (contentType != null) {
                    charset = contentType.getCharset();
                    if (charset == null) {
                        ContentType defaultContentType = ContentType.getByMimeType(contentType.getMimeType());
                        charset = defaultContentType != null ? defaultContentType.getCharset() : null;
                    }
                }

                if (charset == null) {
                    charset = HTTP.DEF_CONTENT_CHARSET;
                }

                Reader reader = new InputStreamReader(instream, charset);
                CharArrayBuffer buffer = new CharArrayBuffer(i);
                char[] tmp = new char[1024 * 1024];
                /*
                 * int l = reader.read(tmp);
                 * 
                 * buffer.append(tmp, 0, l);
                 */

                String string = "";

                byte[] b = new byte[1024];// 采用byte数组 按字节进行数据的接收 避免readline()方法的阻塞机制！！ 弊端是接受字节长度受限，但是在这个项目中足够了
                // int x= is.read(b, 0, b.length);//相较于readline（）读取数据 字节读取慢 不适合大量数据的通信
                int x = 0;
                int count = 0;
                byte[] bbb = getBytesFromStream(instream);
                /*
                 * while(( x=instream.read(b, 0, b.length))!=0) { count+=1; string+=new String(b, 0, x); if(count>5){ break; } }
                 */

                /*
                 * BufferedReader input = new BufferedReader(reader); String message = ""; String line = null; while((line = input.readLine()) != null) { message += line; }
                 * System.out.println(message);
                 */
                /*
                 * int l;
                 * 
                 * while(true) { l = reader.read(tmp); if(l==-1){ break; } buffer.append(tmp, 0, l); }
                 * 
                 * String var9 = buffer.toString();
                 */
                return bbb.toString();
            } finally {
                instream.close();
            }
        }
    }

    private static byte[] getBytesFromStream(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] kb = new byte[1024];
        int len;
        while ((len = is.read(kb)) > 0) {
            baos.write(kb, 0, len);
        }
        byte[] bytes = baos.toByteArray();
        baos.close();
        is.close();
        return bytes;
    }

    private static String byteToString(byte[] bytes) {
        if (null == bytes || bytes.length == 0) {
            return "";
        }
        String strContent = "";
        try {
            strContent = new String(bytes, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return strContent;
    }

}
