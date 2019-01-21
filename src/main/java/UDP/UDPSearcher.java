package UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPSearcher {
    private static final int LISTEN_PORT = 30000;

    public static void main(String[] args) throws IOException {
        System.out.println("UDPProvider Started");

        //作为搜索方、无需指定端口
        DatagramSocket ds = new DatagramSocket();

        //发送
        String requestData = "helloword";
        byte[] requestDataBytes = requestData.getBytes();

        //构建发送包并向服务方发送

        DatagramPacket requestPacket = new DatagramPacket(requestDataBytes,requestDataBytes.length);

        requestPacket.setAddress(InetAddress.getLocalHost());
        requestPacket.setPort(20000);
        ds.send(requestPacket);


        //构建接收实体
        final byte[] buf = new byte[512];
        DatagramPacket receivePack = new DatagramPacket(buf,buf.length);
        //接收
        ds.receive(receivePack);

        String ip_rece = receivePack.getAddress().getHostAddress();
        int port = receivePack.getPort();
        int datalen = receivePack.getLength();

        String data = new String(receivePack.getData(),0,datalen);

        System.out.println("UDPSearcher receive from ip:"+ ip_rece
                + "\tport:" + port + "\tdata:" + data
        );

        //完成
        System.out.println("UDPSearcher Finished");

        ds.close();

    }

    private static void setListenPort(){

    }
}
