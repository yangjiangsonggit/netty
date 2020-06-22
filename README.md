[TOC]



# Netty

## 1 概述

### 1.1 netty是什么

*[Netty项目](https://netty.io/)*致力于提供一个异步事件驱动的网络应用程序框架和工具，以快速开发可维护的高性能和高可扩展性协议服务器和客户端。

换句话说，Netty是一个NIO客户端服务器框架，可以快速轻松地开发网络应用程序，例如协议服务器和客户端。它极大地简化和简化了网络编程，例如TCP和UDP套接字服务器开发。

“快速简便”并不意味着最终的应用程序将遭受可维护性或性能问题的困扰。Netty经过精心设计，结合了从许多协议（例如FTP，SMTP，HTTP以及各种基于二进制和文本的旧式协议）的实施中获得的经验。结果，Netty成功地找到了一种无需妥协即可轻松实现开发，性能，稳定性和灵活性的方法。

一些用户可能已经发现其他声称具有相同优势的网络应用程序框架，并且您可能想问一下Netty与他们有何不同。答案是它所基于的哲学。Netty旨在从第一天开始就API和实施方面为您提供最舒适的体验。这不是有形的东西，但是您会意识到，当您阅读本指南并与Netty一起玩时，这种哲学将使您的生活更加轻松。



### 1.2 学习netty需要的基础

- Java多线程编程
- Java IO编程
- Java网络编程



## 2 为什么要学习netty

### 2.1 使用了netty的公司和项目

- Alibaba
  - [HSF-](https://help.aliyun.com/document_detail/100087.html)高速服务框架，阿里巴巴使用的易于使用的高性能RPC框架
  - [Seata-](https://github.com/seata/seata)一个易于使用，高性能，开源的分布式交易解决方案

- Apache ActiveMQ Artemis
  - 异步消息传递系统，面向消息的中间件的示例。
- Apache Dubbo
  - 高性能RPC框架
- Apache Spark
  - 通用集群计算框架-使用Netty进行RPC和改组数据通信
- [Facebook](https://www.facebook.com/)
  - [Nifty-](https://github.com/facebook/nifty)基于Netty的Thrift传输实施
- [Google](https://www.google.com/)
  - [gRPC-](https://github.com/grpc/grpc-java)基于HTTP / 2的远程过程调用（RPC）系统。
  - [OpenBidder-](https://developers.google.com/ad-exchange/rtb/open-bidder/guides/webserver)一种开源实时出价（RTB）框架，可让您实时出价广告。
- [Netflix](https://www.netflix.com/)
  - [NetflixOSS聚会](https://www.youtube.com/watch?v=aEuNBk1b5OE)（视频）
  - [功能区](https://github.com/Netflix/ribbon) -具有内置软件负载平衡器的进程间通信（远程过程调用）库
  - [RxNetty](https://github.com/Netflix/RxNetty) -Netty的反应性扩展（Rx）适配器



### 2.2 netty的重要性

分布式 -> 微服务 -> RPC(Remote Procedure Call) -> 网络通信(Netty/Mina,都是同一个作者,netty更新)





## 3 IO模型(BIO/NIO)

### 3.1 BIO和NIO的比较

BIO和NIO模型

![](/Users/yjs/Documents/技术资料2020(Xmind)/netty/BIO NIO.png)