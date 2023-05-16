package ai.fastcode.fastcode.Model;
import java.util.List;
import ai.fastcode.fastcode.Model.Message;

public class OpenAIConversationDto {
    private String model;
    private List<Message> messages;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    // Getters and setters...
}

