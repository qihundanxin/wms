import com.deer.wms.project.root.generator.Generator;
import com.deer.wms.project.root.generator.configurer.DatasourceConfigurer;
import com.deer.wms.project.root.generator.configurer.Table;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by guo on 2017/9/29.
 */
public class ReviewGenerator2 {
    public static void main(String[] args) {
        try{
            int port = 2504;
            ServerSocket server = new ServerSocket(port);

            // server将一直等待连接的到来
            System.out.println("server将一直等待连接的到来");
            while (true){
                Socket socket = server.accept();
                // 建立好连接后，从socket中获取输入流，并建立缓冲区进行读取
                InputStream inputStream = socket.getInputStream();
                byte[] bytes = new byte[1024];
                int len;
                StringBuilder sb = new StringBuilder();
                while ((len = inputStream.read(bytes)) != -1) {
                    //注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
                    sb.append(new String(bytes, 0, len,"UTF-8"));
                }
                System.out.println("get message from client: " + sb);
                String msg = sb.toString();
                send(msg);
                inputStream.close();
                socket.close();

            }

        }catch (Exception e){

        }

    }

    private static void send(String message){
        try{
            String host = "10.20.235.74";
            int port = 2505;
            // 与服务端建立连接
            Socket socket = new Socket(host, port);
            // 建立连接后获得输出流
            OutputStream outputStream = socket.getOutputStream();

            socket.getOutputStream().write(message.getBytes("UTF-8"));
            //通过shutdownOutput高速服务器已经发送完数据，后续只能接受数据
            socket.shutdownOutput();

            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[1024];
            int len;
            StringBuilder sb = new StringBuilder();
            while ((len = inputStream.read(bytes)) != -1) {
                //注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
                sb.append(new String(bytes, 0, len,"UTF-8"));
            }
            System.out.println("get message from server: " + sb);

            inputStream.close();
            outputStream.close();
            socket.close();
        }catch (Exception e){

        }

    }
}
