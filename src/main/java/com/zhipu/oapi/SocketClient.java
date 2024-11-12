package com.zhipu.oapi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketClient {
    private Socket socket;
    private OutputStream outputStream;
    private InputStream inputStream;

    // 构造函数用于建立长连接
    public SocketClient() throws IOException {
        String host = "192.168.1.169";
        int port = 12345;
        socket = new Socket(host, port);
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();
    }

    // 用于关闭Socket连接的方法
    public void CloseSocket() throws IOException {
        if (socket != null && !socket.isClosed()) {
            inputStream.close();
            outputStream.close();
            socket.close();
            System.out.println("Socket连接已关闭。");
        }
    }

    public String Getmessage(String message) throws IOException {
        // 发送消息
        outputStream.write(message.getBytes("UTF-8"));
        outputStream.flush();  // 确保消息立即发送

        // 接收服务器返回的消息
        byte[] bytes = new byte[1024];
        int len;
        StringBuilder sb = new StringBuilder();

        // 读取服务器返回的信息
        while ((len = inputStream.read(bytes)) != -1) {
            sb.append(new String(bytes, 0, len, "UTF-8"));

            // 如果已经读完消息，跳出循环
            if (inputStream.available() == 0) {
                break;
            }
        }

        return sb.toString();
    }

}
