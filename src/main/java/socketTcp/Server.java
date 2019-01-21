package socketTcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务器端
 */
public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(2000);

        System.out.println("服务器准备就绪");
        System.out.println("服务器信息："+ serverSocket.getInetAddress()+ " p:" + serverSocket.getLocalPort());

       while(true){
           //等待客户端连接
           //得到客户端
           Socket client = serverSocket.accept();
           //客户端构建异步线程
           ClientHandler clientHandler = new ClientHandler(client);
           //启动线程
           clientHandler.start();
       }

    }

    private static class ClientHandler extends Thread{
        private  Socket socket;
        private boolean flag = true;
        //构造方法
        ClientHandler(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            super.run();
            System.out.println("新客户端连接：" + socket.getInetAddress() + " p:" + socket.getPort());

            try{
                //打印流 用于数据输出
                PrintStream socketOutPut = new PrintStream(socket.getOutputStream());
                BufferedReader socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                do{
                    String str = socketInput.readLine();
                    if("bye".equalsIgnoreCase(str)){
                        flag = false;
                        socketOutPut.println("bye");
                    }else{
                        System.out.println(str);
                        socketOutPut.println("回送：" + str.length());
                    }
                }while (flag);
                socketInput.close();
                socketOutPut.close();
            }catch (Exception e){
                System.out.println("连接异常断开" + e.toString());
            }finally {
                try{
                    socket.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            System.out.println("客户端已退出：" + socket.getInetAddress() + " p:" + socket.getPort());
        }

    }
}
