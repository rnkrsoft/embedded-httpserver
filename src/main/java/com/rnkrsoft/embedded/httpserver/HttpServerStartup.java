package com.rnkrsoft.embedded.httpserver;

import com.rnkrsoft.config.ConfigProvider;
import com.rnkrsoft.config.ConfigProviderFactory;
import com.rnkrsoft.embedded.httpserver.server.EmbeddedServer;
import com.rnkrsoft.embedded.httpserver.server.webxml.WebXmlParser;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
public class HttpServerStartup {
    public static void main(String[] args) throws Exception {
        main();
    }
    public static void main() throws Exception {
        ConfigProvider config = ConfigProviderFactory.getPropertiesInstance("tomcat");
        config.param("server.http.hostName", "localhost");
        config.param("server.http.port", "8080");
        config.param("server.http.protocol", "HTTP/1.1");
        config.param("server.http.contextPath", "");
        config.param("server.http.runtimeDir", "./work");
        config.param("server.http.useBodyEncodingForURI", "true");
        config.param("server.http.uriEncoding", "UTF-8");
        config.param("server.http.asyncTimeout", "30000");
        config.param("server.http.connectionTimeout", "30000");
        config.param("server.http.maxConnections", "30000");
        config.param("server.http.maxThreads", "100");
        config.param("file.encoding", "UTF-8");
        config.init("./work", 60);
        main(config);
    }
    public static void main(ConfigProvider config) throws Exception {
        int port = config.getInteger("server.http.port",8080);
        HttpServer server = new EmbeddedServer(config);
        //设置监听端口
        server.bind(new InetSocketAddress(port), 1024);
        //构建解析web.xml解析器
        WebXmlParser webXmlParser = new WebXmlParser("WEB-INF/web.xml");
        //存在web.xml文件则进行解析
        if (webXmlParser.exists()){
            WebXml webXml = webXmlParser.parse();
            server.setting(webXml);
        }
        //启动容器
        server.start(0);
        //主线程挂起
        server.await();
    }
}