package org.mj.thrfitserver.proxy.client;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.mj.thrfitserver.service.HelloService;

public class HelloServiceClient {
    public void connect(){
        // 设置指定ip+端口
        try(TSocket transport = new TSocket("127.0.0.1", 9999)){
            TFramedTransport tFramedTransport = new TFramedTransport(transport);
            TBinaryProtocol tBinaryProtocol = new TBinaryProtocol(tFramedTransport);
            // 创建服务的客户端
            HelloService.Client.Factory helloServiceClientFactory = new HelloService.Client.Factory();
            HelloService.Client helloServiceClient = helloServiceClientFactory.getClient(tBinaryProtocol);

            transport.open();

            System.out.println(helloServiceClient.hello("aaa"));
            Thread.sleep(2000);
            // 关闭连接
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String getParam(){
        return "张三";
    }

    public static void main(String[] args) {
        new HelloServiceClient().connect();
    }
}