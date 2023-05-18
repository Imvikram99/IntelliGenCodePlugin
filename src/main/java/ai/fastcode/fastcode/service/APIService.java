package ai.fastcode.fastcode.service;

import com.intellij.openapi.components.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Service
@State(name = "APIService", storages = {@Storage("apiKey.xml")})
public final class APIService implements PersistentStateComponent<APIService.State> {
    static class State {
        public String apiKey = "";
    }

    private State myState = new State();

    public static APIService getInstance() {
        return ServiceManager.getService(APIService.class);
    }

    @Nullable
    @Override
    public State getState() {
        return myState;
    }

    @Override
    public void loadState(@NotNull State state) {
        myState = state;
    }

    public String getApiKey() {
        return myState.apiKey;
    }

    public void setApiKey(String apiKey) {
        myState.apiKey = apiKey;
    }
}
