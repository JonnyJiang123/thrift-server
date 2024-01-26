package org.mj.thrfitserver.service.impl;

import org.apache.thrift.TException;
import org.mj.thrfitserver.service.TestService;

/**
 * Created with IntelliJ IDEA.
 * author: jun.jiang
 * Date: 2024/1/25
 * Time: 09:47
 * Description:
 */
public class TestServiceImpl implements TestService.Iface{
    @Override
    public String test() throws TException {
        System.out.println("TestService received");
        return "testttttt";
    }
}
