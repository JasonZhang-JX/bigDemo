package UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.UUID;

/**
 * UDP 提供者，用于提供服务
 */

public class UDPProvider {

    public static void main(String[] args) throws IOException {
        //生成唯一标识
        String sn = UUID.randomUUID().toString();
        Provider provider = new Provider(sn);
        provider.start();

        //读取任意键盘信息后可以退出
        System.in.read();

        provider.exit();
    }

    private static class Provider extends Thread{
        private final String sn ;
        private boolean done = false;
        private DatagramSocket ds = null;

        public Provider(String sn) {
            super();
            this.sn = sn;
        }

        @Override
        public void run() {
            super.run();
            System.out.println("UDPProvider Started");
            try {
                ds = new DatagramSocket(20000);

                while(!done){


                    //构建接收实体
                    final byte[] buf = new byte[512];
                    DatagramPacket receivePack = new DatagramPacket(buf,buf.length);
                    //接收
                    ds.receive(receivePack);

                    String ip_rece = receivePack.getAddress().getHostAddress();
                    int port = receivePack.getPort();
                    int datalen = receivePack.getLength();

                    String data = new String(receivePack.getData(),0,datalen);

                    System.out.println("UDPProvider receive from ip:"+ ip_rece
                            + "\tport:" + port + "\tdata:" + data
                    );

                    //获取数据包中的端口
                    int responsePort = MessageCreater.parsePort(data);

                    if (responsePort != -1){
                        //构建回送数据
                        String responseData = MessageCreater.buildwhitSn(sn);
                        byte[] responseDataBytes = responseData.getBytes();
                        //直接根据发送者构建一份回送信息

                        DatagramPacket responsePacket = new DatagramPacket(responseDataBytes,responseDataBytes.length,receivePack.getAddress(),responsePort);

                        ds.send(responsePacket);

                        //完成
                        System.out.println("UDPProvider Finished");
                    }
                    ds.close();

                }
            } catch (Exception e) {

            }finally {
                close();
            }
            //完成
            System.out.println("UDPProvider Finished");

        }

        private void close(){
            if(ds != null){
                ds.close();
                ds = null;
            }
        }

        void exit(){
            done = true;
            close();
        }
    }

}
