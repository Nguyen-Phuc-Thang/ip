package magnus;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Displays the frontend-only Magnus chat window.
 */
public class Main extends Application {
    private static final int WINDOW_WIDTH = 640;
    private static final int WINDOW_HEIGHT = 600;
    private static final int MESSAGE_MAX_WIDTH = 420;

    private final VBox messageList = new VBox();
    private final ScrollPane chatScroll = new ScrollPane();
    private final TextField commandInput = new TextField();

    /**
     * Creates and displays the chat interface.
     *
     * @param stage Primary window supplied by JavaFX.
     */
    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("app");
        root.setCenter(createConversationView());
        root.setBottom(createInputArea());

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(Main.class.getResource("chat.css").toExternalForm());

        stage.setTitle("Magnus");
        stage.setMinWidth(WINDOW_WIDTH);
        stage.setMinHeight(WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.show();

        commandInput.requestFocus();
    }

    /**
     * Creates the scrollable area that holds the conversation history.
     *
     * @return Scrollable conversation view.
     */
    private ScrollPane createConversationView() {
        messageList.getStyleClass().add("message-list");

        chatScroll.setContent(messageList);
        chatScroll.setFitToWidth(true);
        chatScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        chatScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        chatScroll.getStyleClass().add("chat-scroll");
        return chatScroll;
    }

    /**
     * Creates the command input field and send button.
     *
     * @return Input controls arranged along the bottom of the window.
     */
    private HBox createInputArea() {
        commandInput.setPromptText("Type a command...");
        commandInput.getStyleClass().add("command-input");
        commandInput.setOnAction(event -> submitCommand());
        HBox.setHgrow(commandInput, Priority.ALWAYS);

        Button sendButton = new Button("Send");
        sendButton.getStyleClass().add("send-button");
        sendButton.setOnAction(event -> submitCommand());

        HBox inputArea = new HBox(commandInput, sendButton);
        inputArea.setAlignment(Pos.CENTER);
        inputArea.getStyleClass().add("input-area");
        return inputArea;
    }

    /**
     * Adds the user's command and a placeholder bot reply to the conversation.
     */
    private void submitCommand() {
        String command = commandInput.getText().trim();
        if (command.isEmpty()) {
            return;
        }

        messageList.getChildren().add(createMessageRow(command, true));
        messageList.getChildren().add(createMessageRow("", false));
        commandInput.clear();

        // Layout must finish before the scroll position can move to the new bottom.
        Platform.runLater(() -> chatScroll.setVvalue(1.0));
    }

    /**
     * Creates a left- or right-aligned message bubble.
     *
     * @param message Text displayed inside the bubble.
     * @param isUserMessage Whether the message belongs to the user.
     * @return Row containing the aligned message bubble.
     */
    private HBox createMessageRow(String message, boolean isUserMessage) {
        Label messageBubble = new Label(message);
        messageBubble.setWrapText(true);
        messageBubble.setMaxWidth(MESSAGE_MAX_WIDTH);
        messageBubble.getStyleClass().addAll("message-bubble",
                isUserMessage ? "user-message" : "bot-message");

        HBox messageRow = new HBox(messageBubble);
        messageRow.setAlignment(isUserMessage ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        return messageRow;
    }
}
