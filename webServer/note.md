#  关于 HTTP 协议的笔记 

服务器应该发送一个响应版本，该响应版本等于服务器符合的最高版本，该版本的主版本小于或等于请求中收到的版本。服务器不得发送不符合的版本。服务器如果想要以任何原因拒绝服务客户端的主版本，可以发送一个505响应（HTTP Version Not Supported [注：HTTP版本不支持]）。

 ```text
http-URI = "http:" "//" authority path-abempty [ "?" query ] [ "#" fragment ]
```

[消息分类](https://pearzl.gitbooks.io/http11_chinese/rfc7230/Message_Format/Message_Body/Transfer-Encoding.html)