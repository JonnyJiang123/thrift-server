package org.mj.thrfitserver.service.impl;

import org.apache.thrift.TException;
import org.mj.thrfitserver.service.HelloService;

public class HelloServiceImpl implements HelloService.Iface {

    @Override
    public String hello(String name) throws TException {
        System.out.println("HelloService revived: " + name);
        return "hello, ".concat(name);
    }
}
