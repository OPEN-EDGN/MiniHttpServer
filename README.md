
# Mini Http Server (Unfinished)


> 用 ` Kotlin ` 实现的轻量级 HTTP 服务器 

参照 [httpwg.org](https://httpwg.org/specs/) 并使用 `Kotlin` 编写的轻量级 HTTP 服务器，正在开发中，覆盖单元测试。

## 已知问题

 - [x] 存在缓存溢出漏洞
 
 - [x] 大文件 `POST` 请求处理较慢。(搜索算法问题)
 
 - [x] 未实现完整的 `HTTP 1.1` 协议（仅实现 `GET` 和 `POST` ），~~可能~~已查明存在部分兼容性问题
 
 - [X] 存在一定的线程安全问题（不能在处理会话时添加解析绑定）

