package ai.fastcode.fastcode.action;

import ai.fastcode.fastcode.adapter.OpenAiAdapter;
import ai.fastcode.fastcode.icon.IconClass;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;

public class CodeThisAction extends AnAction {

    public CodeThisAction() {
        //super("IntelliCodeGen", "IntelliCodeGen generates code with chat gpt 4", IconClass.CUSTOM_ICON);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        final Document document = editor.getDocument();
        String query = Messages.showInputDialog(project, "Please provide code details: type and language/framework", "IntelliCodeGen", Messages.getQuestionIcon());

        if (query != null && !query.isEmpty()) {
            query = query + ", write just code nothing else no explanation";
            final String prompt = query;
            ApplicationManager.getApplication().invokeLater(() -> {
                ApplicationManager.getApplication().runReadAction(() -> {
                    final int offset = editor.getCaretModel().getOffset();
                    ApplicationManager.getApplication().executeOnPooledThread(() -> {
                        try {
                            String res = OpenAiAdapter.generate(prompt);
                            ApplicationManager.getApplication().invokeLater(() -> {
                                WriteCommandAction.runWriteCommandAction(project, () -> {
                                    int validOffset = Math.min(offset, document.getTextLength());
                                    if (validOffset < offset) {
                                        // append newline and content at the end of the document
                                        document.insertString(document.getTextLength(), "\n" + res);
                                    } else {
                                        document.insertString(validOffset, res);
                                    }
                                });
                            });
                        } catch (Exception ex) {
                            System.out.println("exception occured :" + ex.getMessage());
                        }
                    });
                });
            });
        }
    }


//    @Override
//    public void update(AnActionEvent e) {
//        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
//        final CaretModel caretModel = editor.getCaretModel();
//        e.getPresentation().setEnabledAndVisible(caretModel.getCurrentCaret().hasSelection());
//    }
}