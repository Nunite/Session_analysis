package com.zhipu.oapi;

import com.zhipu.oapi.service.v4.model.*;
import java.util.ArrayList;
import java.util.List;

public class Glm4_Flash {

    public static final String API_KEY = "2b5147d4dfee46bd8dfe60734c9750fa.7MosuymwHMoaIth2";
    private static final String requestIdTemplate = "miitang-%d";//用于传入当前时间作为id
    private static final List<ChatMessage> messages = new ArrayList<>();

    public static String chatGLM4(String message) {
        messages.add(new ChatMessage(ChatMessageRole.USER.value(), message));
        ClientV4 client = new ClientV4.Builder(API_KEY).build();

        // 构建请求ID
        String requestId = String.format(requestIdTemplate, System.currentTimeMillis());
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4_Flash) // 设置模式
                .stream(Boolean.FALSE)
                .invokeMethod(Constants.invokeMethod)
                .messages(messages)
                .requestId(requestId)
                .build();

        ModelApiResponse invokeModelApiResp = client.invokeModelApi(chatCompletionRequest);

        // 假设 invokeModelApiResp.getData() 返回模型响应的内容
        String modelResponseContent = "";  // 初始化模型响应内容

        if (invokeModelApiResp.isSuccess()) {
            ModelData responseData = invokeModelApiResp.getData(); // 假设 getData() 方法提供返回内容
            if (responseData!= null && responseData.getChoices()!= null &&!responseData.getChoices().isEmpty()) {
                // 进行类型转换
                modelResponseContent = (String) responseData.getChoices().get(0).getMessage().getContent();
            }
        }

        return modelResponseContent;
    }


}
