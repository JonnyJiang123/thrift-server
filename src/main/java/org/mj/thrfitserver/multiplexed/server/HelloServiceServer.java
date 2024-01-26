package org.mj.thrfitserver.multiplexed.server;

import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.mj.thrfitserver.service.HelloService;
import org.mj.thrfitserver.service.TestService;
import org.mj.thrfitserver.service.impl.HelloServiceImpl;
import org.mj.thrfitserver.service.impl.TestServiceImpl;

public class HelloServiceServer {
    public void run(){
        // 监听端口
        try(TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(9999)){

            // 设置TMultiplexedProcessor实现可以处理多个thrift服务
            TMultiplexedProcessor multiplexedProcessor = new TMultiplexedProcessor();
            TProcessor helloServiceProcessor = new HelloService.Processor<>(new HelloServiceImpl());
            TProcessor testServiceProcessor = new TestService.Processor<>(new TestServiceImpl());
            multiplexedProcessor.registerProcessor("HelloService",helloServiceProcessor);
            multiplexedProcessor.registerProcessor("TestService",testServiceProcessor);

            // 构建服务的参数，并指定数据传输方式、接口具体的实现
            TThreadedSelectorServer.Args args = new TThreadedSelectorServer.Args(serverTransport);
            args.protocolFactory(new TBinaryProtocol.Factory());
            args.transportFactory(new TFramedTransport.Factory());
            args.processor(multiplexedProcessor);
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