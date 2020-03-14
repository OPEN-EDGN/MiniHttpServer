
# 简单的网络通信协议实现

> 用 ` Kotlin ` 实现部分简单的网络协议

##  HTTP 1.1 

参照 [httpwg.org](https://httpwg.org/specs/) 并使用 `Kotlin` 编写的轻量级 HTTP 服务器，覆盖单元测试。

### 已知问题

 -[x] 存在缓存溢出漏洞
 
 -[x] 大文件`POST`请求处理较慢。(搜索算法问题)
 
 -[x] 并未实现完整的 HTTP 协议，可能存在兼容性问题
