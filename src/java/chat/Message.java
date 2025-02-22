package chat;

/**
 *
 * @author DELL
 */
public class Message {
    private int messageId;
    private String sender;
    private String receiver;
    private String message;
    private String time;
    private String fileName;
    private String fileType;
    private int fileId;
    private int fileSize;
    private String filePath;  // New field for file path
    
    // Getter and setter for messageId
    public int getMessageId(){
        return messageId;
    }
    public void setMessageId(int messageId){
        this.messageId = messageId;
    }
    
    // Getter and setter for sender
    public String getSender(){
        return sender;
    }
    public void setSender(String sender){
        this.sender = sender;
    }
    
    // Getter and setter for receiver
    public String getReceiver(){
        return receiver;
    }
    public void setReceiver(String receiver){
        this.receiver = receiver;
    }
    
    // Getter and setter for message
    public String getMessage(){
        return message;
    }
    public void setMessage(String message){
        this.message = message;
    }
   
    // Getter and setter for time
    public String getTime(){
        return time;
    }
    public void setTime(String time){
        this.time = time;
    }

    // Getter and setter for fileName
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    // Getter and setter for fileType
    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    // Getter and setter for fileId
    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    // Getter and setter for fileSize
    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    // Getter and setter for filePath (new)
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
