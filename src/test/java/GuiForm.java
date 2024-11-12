import com.formdev.flatlaf.FlatDarkLaf;
import com.zhipu.oapi.Glm4_Flash;
import com.zhipu.oapi.SocketClient;

import javax.swing.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.IOException;
import java.time.LocalDateTime;

public class GuiForm {
    private JPanel root;
    private JTextField textField1;
    private JButton SendButtonx;
    private JTextArea textArea1;
    private JButton ClearButton;
    private JTextArea textArea2;

    private SocketClient socketClient;

    // 发送按钮处理类
    private class SendButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            SendChat();
        }
    }

    // 清除按钮处理类
    private class ClearButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            textField1.setText("");
        }
    }


    public GuiForm() throws IOException {
        //初始化socket
        try {
            socketClient = new SocketClient();
            System.out.println("服务器连接成功，程序启动中...");
        } catch (Exception e) {
            System.out.println("服务器未连接，程序关闭中...");
            System.exit(0);
        }

        // 绑定按钮事件
        SendButtonx.addActionListener(new SendButtonHandler());
        ClearButton.addActionListener(new ClearButtonHandler());
        // 创建右键菜单
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem copyItem = new JMenuItem("复制");
        JMenuItem aiAnalyzeItem = new JMenuItem("AI 分析可信度");
        // 复制功能
        copyItem.addActionListener(e -> {
            if (textArea1.getSelectedText() != null) {
                String selectedText = textArea1.getSelectedText();
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                StringSelection selection = new StringSelection(selectedText);
                clipboard.setContents(selection, null);
            }
        });

        // AI 分析功能
        aiAnalyzeItem.addActionListener(e -> analyzeSelectedTextInThread());

        // 将选项添加到右键菜单
        popupMenu.add(copyItem);
        popupMenu.add(aiAnalyzeItem);

        // 添加鼠标监听器，用于显示右键菜单
        textArea1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        textField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    SendButtonx.doClick();
                }
            }
        });
    }


    // 使用 Thread 在后台分析选中的文本
    private void analyzeSelectedTextInThread() {

        String selectedText = textArea1.getSelectedText();
        if (selectedText == null || selectedText.isEmpty()) {
            System.out.println("没有选中内容进行分析");
            return;
        }

        // 显示“创建中...”提示
        SwingUtilities.invokeLater(() -> textArea2.append("AI 分析结果: 创建中...\n"));

        // 创建新线程处理 AI 分析任务
        Thread analysisThread = new Thread(() -> {
            try {
                // 调用 AI 分析方法
                String Content_message = "请你分析这句话的可信度，以及可能隐含的意思:" + selectedText;
                String message = socketClient.Getmessage(Content_message);

                String separator = "\t---------- ### ----------";

                StringBuilder centeredSeparator = new StringBuilder();

                centeredSeparator.append(separator);  // 添加分隔符
                String resultWithCenteredSeparator = message + "\n" + centeredSeparator.toString() + "\n";

                // 使用 invokeLater 将结果更新到 UI
                SwingUtilities.invokeLater(() -> {
                    // 移除“创建中...”提示并添加实际分析结果
                    textArea2.setText(textArea2.getText().replace("AI 分析结果: 创建中...\n", ""));
                    textArea2.append("AI 分析结果:\n" + resultWithCenteredSeparator + "\n");
                });
            } catch (Exception e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    // 移除“创建中...”提示并显示错误信息
                    textArea2.setText(textArea2.getText().replace("AI 分析结果: 创建中...\n", ""));
                    textArea2.append("AI 分析时出错\n");
                });
            }

        });

        analysisThread.start();  // 启动线程
    }   // 发送聊天内容方法

    private void SendChat() {
        if (textField1.getText().isEmpty()) {
            System.out.println("TextField is Empty!");
        } else {
            LocalDateTime now = LocalDateTime.now();
            String currentTime = now.getYear() + "年" + now.getMonthValue() + "月" + now.getDayOfMonth() + "日" + now.getHour() + "时" + now.getMinute() + "分";
            String SendContent = currentTime + "\n" + textField1.getText() + "\n";
            System.out.println(SendContent);
            textArea1.append(SendContent);
            textField1.setText("");
        }
    }

    public static void main(String[] args) throws IOException {
        FlatDarkLaf.setup();
        JFrame frame = new JFrame("嵌入分析AI的聊天窗口");
        frame.setContentPane(new GuiForm().root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}