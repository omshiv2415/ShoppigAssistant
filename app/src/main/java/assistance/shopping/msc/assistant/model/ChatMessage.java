package assistance.shopping.msc.assistant.model;

/**
 * Created by admin on 26/07/2016.
 */

public class ChatMessage {



    private String author;
    private String message;


    public ChatMessage(){


    }

    public String getAuthor() {
        return author;
    }

    public String getMessage() {
        return message;
    }

    public ChatMessage (String author, String message)
    {
        this.message = message;
        this.author = author;


    }
}
