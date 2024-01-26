package org.mj.thrfitserver.proxy.server;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.mj.thrfitserver.proxy.processor.ProxyProcessor;

public class HelloServiceServer {
    public void run(){
        // 监听端口
        try(TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(9999)){


            // 构建服务的参数，并指定数据传输方式、接口具体的实现
            TThreadedSelectorServer.Args args = new TThreadedSelectorServer.Args(serverTransport);
            // 设置二进制传输协议
            args.protocolFactory(new TBinaryProtocol.Factory());
            // 以帧的形式传输
            args.transportFactory(new TFramedTransport.Factory());
            // 设置代理处理器，有代理处理器执行请求转发
            args.processor(new ProxyProcessor());
            // 创建服务并启动
            TServer server = new TThreadedSelectorServer(args);

            server.serve();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new HelloServiceServer().run();
    }
}