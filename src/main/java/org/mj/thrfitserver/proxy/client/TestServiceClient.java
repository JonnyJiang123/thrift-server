package org.mj.thrfitserver.proxy.client;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.mj.thrfitserver.service.TestService;

public class TestServiceClient {
    public void connect(){
        // 设置指定ip+端口
        try(TSocket transport = new TSocket("127.0.0.1", 9999)){
            TFramedTransport tFramedTransport = new TFramedTransport(transport);
            TBinaryProtocol tBinaryProtocol = new TBinaryProtocol(tFramedTransport);

            TestService.Client.Factory testServiceFactory = new TestService.Client.Factory();
            TestService.Client testServiceClient = testServiceFactory.getClient(tBinaryProtocol);
            transport.open();

            System.out.println(testServiceClient.test());
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
        new TestServiceClient().connect();
    }
}