package ai.fastcode.fastcode.action;

import ai.fastcode.fastcode.service.APIService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;

public class PromptKeyAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        APIService apiService = APIService.getInstance();
        String apiKey = apiService.getApiKey();
        if (apiKey==null || apiKey.isEmpty()) {
            apiKey = Messages.showInputDialog(
                    "Please enter your API Key: NOTE : YOU WILL GET ONLY ONE CHANCE TO ENTER THE KEY , SO MAKE SURE YOU PROVIDE TEH CORRECT KEY",
                    "API Key Required",
                    Messages.getQuestionIcon()
            );

            if (apiKey != null && !apiKey.isEmpty()) {
                // Save the key
                apiService.setApiKey(apiKey);
            }
        }
        // Use the apiKey here.
    }
}
