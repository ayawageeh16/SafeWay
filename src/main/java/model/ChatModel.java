package model;


public class ChatModel {

    private String id;
    private UserModel userModel;
    private String message;
    private String timeStamp;

    public ChatModel() {
    }

    public ChatModel(UserModel userModel, String message, String timeStamp) {
        this.userModel = userModel;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public ChatModel(UserModel userModel, String timeStamp) {
        this.userModel = userModel;
        this.timeStamp = timeStamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }


    @Override
    public String toString() {
        return "ChatModel{" +
                ", timeStamp='" + timeStamp + '\'' +
                ", message='" + message + '\'' +
                ", userModel=" + userModel +
                '}';
    }
}
