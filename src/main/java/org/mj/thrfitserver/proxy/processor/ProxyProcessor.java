package org.mj.thrfitserver.proxy.processor;

import org.apache.thrift.ProcessFunction;
import org.apache.thrift.TApplicationException;
import org.apache.thrift.TBaseProcessor;
import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TMessage;
import org.apache.thrift.protocol.TMessageType;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolUtil;
import org.apache.thrift.protocol.TType;
import org.mj.thrfitserver.service.HelloService;
import org.mj.thrfitserver.service.TestService;
import org.mj.thrfitserver.service.impl.HelloServiceImpl;
import org.mj.thrfitserver.service.impl.TestServiceImpl;


import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * author: jun.jiang
 * Date: 2024/1/25
 * Time: 14:20
 * Description:
 */
public class ProxyProcessor implements TProcessor {

    FuncService funcService = null;


    {
        // 动态加载thrift的服务
        funcService = new FuncService();
        createProcessMap();
    }
    /**
     * 自定义Processor，解析请求头的方法，通过方法来转发到对应的服务实现
     * @param in
     * @param out
     * @return
     * @throws TException
     */
    @Override
    public boolean process(TProtocol in, TProtocol out) throws TException {
        // 解析请求头，获取方法名
        TMessage msg = in.readMessageBegin();
        // 通过方法名获取对应thrift对应生产的ProcessFunction
        ProcessFunction fn = funcService.funcMap.get(msg.name);
        if (fn == null) {
            TProtocolUtil.skip(in, TType.STRUCT);
            in.readMessageEnd();
            TApplicationException x = new TApplicationException(TApplicationException.UNKNOWN_METHOD, "Invalid method name: '"+msg.name+"'");
            out.writeMessageBegin(new TMessage(msg.name, TMessageType.EXCEPTION, msg.seqid));
            x.write(out);
            out.writeMessageEnd();
            out.getTransport().flush();
            return true;
        }
        // 执行ProcessFunction。传入真实服务的service实现类
        fn.process(msg.seqid, in, out, funcService.serviceMap.get(msg.name));
        return false;
    }

    /**
     * 遍历所有的thrift service，将服务对应的方法的thrift服务、服务实现类都封装到FuncService胶水类中
     * 可以通过遍历某个约定的路径通过动态类加载机制动态的实现
     */
    private void createProcessMap() {
        HelloService.Iface helloService = new HelloServiceImpl();
        TProcessor helloServiceProcessor = new HelloService.Processor<>(helloService);
        Map<String, ProcessFunction> helloProcessMapView = ((TBaseProcessor) helloServiceProcessor).getProcessMapView();
        //org.example.service.HelloService.Processor
        for (Map.Entry<String, ProcessFunction> entry : helloProcessMapView.entrySet()) {
            funcService.funcMap.put(entry.getKey(), entry.getValue());
            funcService.serviceMap.put(entry.getKey(),helloService);
        }

        TestService.Iface testService = new TestServiceImpl();
        TProcessor testServiceProcessor = new TestService.Processor<>(testService);
        Map<String, ProcessFunction> testProcessMapView = ((TBaseProcessor) testServiceProcessor).getProcessMapView();

        for (Map.Entry<String, ProcessFunction> entry : testProcessMapView.entrySet()) {
            funcService.funcMap.put(entry.getKey(), entry.getValue());
            funcService.serviceMap.put(entry.getKey(), testService);
        }
    }


    /**
     * 封装thrift执行需要的方法、执行对象，方便代理。属于胶水类
     * key：方法名
     * funcMap value：对应的thrift生产的方法
     * serviceMap value：对应的服务提供者（实现）
     */
    public static class FuncService{
        public Map<String, ProcessFunction> funcMap = new HashMap<>();
        public Map<String, Object> serviceMap = new HashMap<>();

    }
}
