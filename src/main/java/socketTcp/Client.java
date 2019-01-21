package socketTcp;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException{
        Socket socket = new Socket();
        //读取流 超时时间
        socket.setSoTimeout(3000);
        //连接的超时时间和端口号
        socket.connect(new InetSocketAddress(Inet4Address.getLocalHost(),2000),3000);
        System.out.println("已发起服务器连接，并进入后一流程");
        System.out.println("客户端信息："+ socket.getLocalAddress() + " p:" +socket.getLocalPort());
        System.out.println("服务器信息："+ socket.getInetAddress()+ " p:" + socket.getPort());

        try{
            //发送接收数据
            todo(socket);

        }catch (Exception e){
            System.out.println("异常关闭");
        }


        socket.close();
        System.out.println("客户端已退出~");

    }

    private static void todo(Socket client) throws IOException{
        //获取键盘输入
        InputStream in = System
                .in;
        //换成bufferreader
        BufferedReader input = new BufferedReader(new InputStreamReader(in));

        //得到socket输出流，并转换为打印流
        OutputStream outputStream = client.getOutputStream();
        PrintStream printStream = new PrintStream(outputStream);


        //得到socket输入流
        InputStream inputStream = client.getInputStream();
        BufferedReader socketBufferReader = new BufferedReader(new InputStreamReader(inputStream));

        boolean flag = true;
        do{
            //键盘读取一行
            String str = input.readLine();
            //发送到服务器
            printStream.println(str);

            //从服务器读取一行
            String echo = socketBufferReader.readLine();
            if ("bye".equalsIgnoreCase(echo)) {
                flag = false;
            }else{
                System.out.println(echo);
            }

        }while (flag);

        outputStream.close();
        printStream.close();
        inputStream.close();
        socketBufferReader.close();

    }

}
