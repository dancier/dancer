package net.dancier.dancer.controller.payload;

import java.util.Arrays;
import java.util.List;

public class ApiResponse {  private Boolean success;
    private List<String> messages;

    public ApiResponse(Boolean success,
                       String... message) {
        this.success = success;
        this.messages = Arrays.asList(message);
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessage(List<String> messages) {
        this.messages = messages;
    }
}
