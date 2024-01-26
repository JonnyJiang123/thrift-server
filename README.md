# thrift服务
核心功能：单个进程实现处理多个thrift服务。
<img width="919" alt="image" src="https://github.com/JonnyJiang123/thrift-server/assets/56102991/63775b70-fd55-401b-aa66-6d294598cbd5">


# 实现方式
## TMultiplexedProtocol
该方式适用于所有thrift客户端都可以改成使用TMultiplexedProtocol
## 自定义Processor
该方式适用于不改变现有thrift客户端的情况下，但是局限性是多个thrift服务不能有重名的方法存在
