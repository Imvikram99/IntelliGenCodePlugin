package ai.fastcode.fastcode.adapter;
import ai.fastcode.fastcode.Model.Message;
import ai.fastcode.fastcode.Model.OpenAIConversationDto;
import ai.fastcode.fastcode.Model.OpenAIConversationResDto;
import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class OpenAiAdapter {
    private static final String MODEL = "gpt-3.5-turbo";
    private static final boolean useLatest = false;

    private static final String MODEL_LATEST = "gpt-4";
    private static final String USER_ROLE = "user";
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = "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";

    private static final int timeout = 60;
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .build();
    private static final Gson gson = new Gson();

    public static String generate(String prompt) {
        OpenAIConversationDto openAIConversationDto = createOpenAIConversationDto(prompt);
        String json = serializeDtoToJson(openAIConversationDto);
        OpenAIConversationResDto openAIConversationResDto = sendRequestToOpenAI(json);
        return openAIConversationResDto.getChoices().get(0).getMessage().getContent();
    }

    private static OpenAIConversationDto createOpenAIConversationDto(String prompt) {
        OpenAIConversationDto openAIConversationDto = new OpenAIConversationDto();
        openAIConversationDto.setModel(useLatest?MODEL_LATEST:MODEL);
        List<Message> messages = new ArrayList<>();
        Message message = new Message();
        message.setRole(USER_ROLE);
        message.setContent(prompt);
        messages.add(message);
        openAIConversationDto.setMessages(messages);
        return openAIConversationDto;
    }

    private static String serializeDtoToJson(OpenAIConversationDto openAIConversationDto) {
        return gson.toJson(openAIConversationDto);
    }

    private static OpenAIConversationResDto sendRequestToOpenAI(String json) {
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            OpenAIConversationResDto openAIConversationResDto = gson.fromJson(response.body().string(), OpenAIConversationResDto.class);
            return openAIConversationResDto;
        } catch (IOException e) {
            throw new RuntimeException("Failed : HTTP error code : " + e.getMessage());
        }
    }
}