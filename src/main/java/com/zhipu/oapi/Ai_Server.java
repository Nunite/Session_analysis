package com.zhipu.oapi;

import com.zhipu.oapi.Glm4_Flash;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Ai_Server {
    private ServerSocket server;
    private Socket clientsocket;

    public Ai_Server() throws IOException {
        int port = 12345;
        server = new ServerSocket(port);
        System.out.println("服务器等待连接中...");
        clientsocket = server.accept();
        System.out.println("连接成功...");
    }

    public void CloseServer() throws IOException {
        clientsocket.close();
        server.close();
    }

    public static void main(String argc[]) throws Exception {
        Ai_Server aiServer = new Ai_Server();
        InputStream inputStream = aiServer.clientsocket.getInputStream();
        OutputStream outputStream = aiServer.clientsocket.getOutputStream();

        byte[] bytes = new byte[1024];
        int len;

        while (true) {
            StringBuilder sb = new StringBuilder();
            try {
                // 检测客户端输入
                while ((len = inputStream.read(bytes)) != -1) {
                    sb.append(new String(bytes, 0, len, "UTF-8"));

                    // 如果读取完一条消息，开始处理
                    if (inputStream.available() == 0) {
                        break;
                    }
                }

                if (sb.length() > 0) {
                    String message = sb.toString();
                    System.out.println("从客户端获取信息为:" + message);

                    // 调用 GLM 方法
                    String Getmessage = Glm4_Flash.chatGLM4(message);
                    System.out.println("获取AI信息为:" + Getmessage);

                    // 发送响应
                    outputStream.write(Getmessage.getBytes("UTF-8"));
                    outputStream.flush();
                }

            } catch (IOException e) {
                System.out.println("检测到客户端断开连接，关闭端口...");
                inputStream.close();
                outputStream.close();
                aiServer.CloseServer();
                break;
            }
        }
    }
}
