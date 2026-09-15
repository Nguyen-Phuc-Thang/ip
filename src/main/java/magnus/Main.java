package magnus;

import java.net.URL;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import magnus.exception.MagnusException;
import magnus.ui.Ui;

/**
 * Displays the Magnus chat window and connects it to the command backend.
 */
public class Main extends Application {
    private static final int WINDOW_WIDTH = 720;
    private static final int WINDOW_HEIGHT = 620;
    private static final int MINIMUM_WINDOW_WIDTH = 420;
    private static final int MINIMUM_WINDOW_HEIGHT = 460;
    private static final int BOT_AVATAR_SIZE = 34;
    private static final int USER_AVATAR_SIZE = 26;
    private static final int HEADER_AVATAR_SIZE = 40;
    private static final int AVATAR_BORDER_ALLOWANCE = 4;
    private static final int MESSAGE_ROW_SPACING = 10;
    private static final int MINIMUM_MESSAGE_WIDTH = 180;
    private static final double USER_MESSAGE_WIDTH_RATIO = 0.70;
    private static final double BOT_MESSAGE_WIDTH_RATIO = 0.84;
    private static final double BOTTOM_SCROLL_POSITION = 1.0;
    private static final String DISPLAY_DATE_TIME_PATTERN =
            "[A-Z][a-z]{2} \\d{2}, \\d{4} \\d{2}:\\d{2}";
    private static final Pattern TASK_LINE_PATTERN = Pattern.compile(
            "^(?:\\d+\\.\\s+)?\\[[TDE]\\]\\[[X ]\\]\\s+.+");
    private static final Pattern EVENT_TIME_PATTERN = Pattern.compile(
            "\\s*\\(from:\\s*(?<from>" + DISPLAY_DATE_TIME_PATTERN
                    + ")\\s+to:\\s*(?<to>" + DISPLAY_DATE_TIME_PATTERN + ")\\)$");
    private static final Pattern DEADLINE_TIME_PATTERN = Pattern.compile(
            "\\s*\\(by:\\s*(?<by>" + DISPLAY_DATE_TIME_PATTERN + ")\\)$");

    private final VBox messageList = new VBox();
    private final ScrollPane chatScroll = new ScrollPane();
    private final TextField commandInput = new TextField();
    private Magnus magnus;
    private Image magnusAvatar;
    private Image userAvatar;

    /**
     * Creates and displays the chat interface.
     *
     * @param stage Primary window supplied by JavaFX.
     * @throws MagnusException If saved tasks cannot be loaded when the backend starts.
     */
    @Override
    public void start(Stage stage) throws MagnusException {
        this.magnus = new Magnus();
        loadAvatarImages();

        BorderPane root = new BorderPane();
        root.getStyleClass().add("app");
        root.setTop(createHeader());
        root.setCenter(createConversationView());
        root.setBottom(createInputArea());

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(Main.class.getResource("chat.css").toExternalForm());

        stage.setTitle("Magnus");
        stage.setResizable(true);
        stage.setMinWidth(MINIMUM_WINDOW_WIDTH);
        stage.setMinHeight(MINIMUM_WINDOW_HEIGHT);
        stage.setScene(scene);
        showWelcomeMessage();
        stage.show();

        commandInput.requestFocus();
    }

    /**
     * Loads the avatar assets once so every message can reuse the same images.
     */
    private void loadAvatarImages() {
        this.magnusAvatar = loadImage("images/magnus.png");
        this.userAvatar = loadImage("images/user.png");
    }

    /**
     * Loads an image packaged beside the Magnus JavaFX resources.
     *
     * @param resourcePath Package-relative path of the image resource.
     * @return The loaded image.
     * @throws NullPointerException If the image resource cannot be found.
     */
    private Image loadImage(String resourcePath) {
        URL imageUrl = Objects.requireNonNull(Main.class.getResource(resourcePath));
        return new Image(imageUrl.toExternalForm());
    }

    /**
     * Creates the compact application identity shown above the conversation.
     *
     * @return Header containing the Magnus portrait and app description.
     */
    private HBox createHeader() {
        StackPane avatar = createAvatar(false, HEADER_AVATAR_SIZE);
        avatar.getStyleClass().add("header-avatar");

        Label appName = new Label("Magnus");
        appName.getStyleClass().add("app-name");
        Label appDescription = new Label("Your tactical task companion");
        appDescription.getStyleClass().add("app-description");
        VBox appIdentity = new VBox(appName, appDescription);
        appIdentity.getStyleClass().add("app-identity");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label turnIndicator = new Label("♟  YOUR MOVE");
        turnIndicator.getStyleClass().add("turn-indicator");

        HBox header = new HBox(avatar, appIdentity, spacer, turnIndicator);
        header.setAlignment(Pos.CENTER_LEFT);
        header.getStyleClass().add("app-header");
        return header;
    }

    /**
     * Displays only the short greeting in the graphical interface. The large
     * text banner remains available in the command-line interface, where it
     * does not take space away from the conversation.
     */
    private void showWelcomeMessage() {
        messageList.getChildren().add(createBotMessageRow(Ui.getGreeting(), false, "welcome-message"));
    }

    /**
     * Creates the scrollable area that holds the conversation history.
     *
     * @return Scrollable conversation view.
     */
    private ScrollPane createConversationView() {
        messageList.getStyleClass().add("message-list");
        configureAutomaticScrolling();

        chatScroll.setContent(messageList);
        chatScroll.setFitToWidth(true);
        chatScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        chatScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        chatScroll.setAccessibleText("Conversation history");
        chatScroll.getStyleClass().add("chat-scroll");
        return chatScroll;
    }

    /**
     * Keeps the newest conversation turn visible after its wrapped height has
     * been calculated. Waiting for the content height to grow avoids scrolling
     * against the previous layout bounds.
     */
    private void configureAutomaticScrolling() {
        messageList.heightProperty().addListener((observable, previousHeight, currentHeight) -> {
            if (currentHeight.doubleValue() <= previousHeight.doubleValue()) {
                return;
            }

            Platform.runLater(() -> chatScroll.setVvalue(BOTTOM_SCROLL_POSITION));
        });
    }

    /**
     * Creates the command input field and send button.
     *
     * @return Input controls arranged along the bottom of the window.
     */
    private HBox createInputArea() {
        commandInput.setPromptText("Enter a command, e.g. list");
        commandInput.setAccessibleText("Command input");
        commandInput.getStyleClass().add("command-input");
        commandInput.setOnAction(event -> submitCommand());
        HBox.setHgrow(commandInput, Priority.ALWAYS);

        Button sendButton = new Button("Send");
        sendButton.setAccessibleText("Send command");
        sendButton.getStyleClass().add("send-button");
        sendButton.setDefaultButton(true);
        sendButton.setOnAction(event -> submitCommand());

        HBox inputArea = new HBox(commandInput, sendButton);
        inputArea.setAlignment(Pos.CENTER);
        inputArea.getStyleClass().add("input-area");
        return inputArea;
    }

    /**
     * Sends the user's command to Magnus and displays the returned response.
     */
    private void submitCommand() {
        String command = commandInput.getText().trim();
        if (command.isEmpty()) {
            return;
        }

        messageList.getChildren().add(createUserMessageRow(command));
        commandInput.clear();

        MagnusResponse response = this.magnus.getResponseResult(command);
        if (this.magnus.isExitRequested()) {
            Platform.exit();
            return;
        }

        messageList.getChildren().add(createBotMessageRow(response.message(), response.isError()));
    }

    /**
     * Creates a compact, right-aligned user message. Unlike Magnus responses,
     * user messages do not repeat a sender heading because their alignment and
     * distinct color already establish ownership.
     *
     * @param message Text entered by the user.
     * @return Row containing the user message.
     */
    private HBox createUserMessageRow(String message) {
        Label messageBubble = createMessageLabel(message, "user-message");
        messageBubble.maxWidthProperty().bind(
                Bindings.max(MINIMUM_MESSAGE_WIDTH,
                        chatScroll.widthProperty().multiply(USER_MESSAGE_WIDTH_RATIO)
                                .subtract(USER_AVATAR_SIZE + AVATAR_BORDER_ALLOWANCE)));

        StackPane avatar = createAvatar(true, USER_AVATAR_SIZE);
        HBox messageRow = new HBox(messageBubble, avatar);
        messageRow.setSpacing(MESSAGE_ROW_SPACING);
        messageRow.setAlignment(Pos.TOP_RIGHT);
        messageRow.getStyleClass().addAll("message-row", "user-message-row");
        return messageRow;
    }

    /**
     * Creates a labeled Magnus response card. Error responses receive both a
     * semantic heading and a dedicated style so they remain distinguishable
     * without relying on color alone.
     *
     * @param message Text returned by Magnus.
     * @param isError Whether the response reports a command error.
     * @param additionalStyleClasses Extra CSS classes applied to the response.
     * @return Row containing the Magnus response card.
     */
    private HBox createBotMessageRow(String message, boolean isError,
            String... additionalStyleClasses) {
        Label senderLabel = new Label(isError ? "MAGNUS  •  COMMAND ISSUE" : "MAGNUS");
        senderLabel.getStyleClass().add("sender-label");
        if (isError) {
            senderLabel.getStyleClass().add("error-sender-label");
        }

        VBox messageBubble = createBotMessageCard(message);
        if (isError) {
            messageBubble.getStyleClass().add("error-message");
            messageBubble.setAccessibleText("Command error. " + formatForGraphicalDisplay(message));
        }
        messageBubble.getStyleClass().addAll(additionalStyleClasses);
        messageBubble.maxWidthProperty().bind(
                Bindings.max(MINIMUM_MESSAGE_WIDTH,
                        chatScroll.widthProperty().multiply(BOT_MESSAGE_WIDTH_RATIO)
                                .subtract(BOT_AVATAR_SIZE + AVATAR_BORDER_ALLOWANCE)));

        VBox responseContent = new VBox(senderLabel, messageBubble);
        responseContent.getStyleClass().add("response-content");

        StackPane avatar = createAvatar(false, BOT_AVATAR_SIZE);
        HBox messageRow = new HBox(avatar, responseContent);
        messageRow.setSpacing(MESSAGE_ROW_SPACING);
        messageRow.setAlignment(Pos.TOP_LEFT);
        messageRow.getStyleClass().addAll("message-row", "bot-message-row");
        return messageRow;
    }

    /**
     * Creates a structured Magnus response whose task rows and date-time
     * details can be spaced and styled independently.
     *
     * @param message Text returned by Magnus.
     * @return Styled response card containing each display line.
     */
    private VBox createBotMessageCard(String message) {
        VBox messageCard = new VBox();
        messageCard.setMinWidth(0);
        messageCard.getStyleClass().addAll("message-bubble", "bot-message", "message-body");

        String[] messageLines = formatForGraphicalDisplay(message).split("\\n", -1);
        for (String messageLine : messageLines) {
            messageCard.getChildren().add(createBotMessageLine(messageLine));
        }
        return messageCard;
    }

    /**
     * Converts one response line into plain text, a paragraph gap, or a task
     * row with separately styled time details.
     *
     * @param messageLine One line of a Magnus response.
     * @return Node used to render that line.
     */
    private Node createBotMessageLine(String messageLine) {
        if (messageLine.isBlank()) {
            Region paragraphSpacer = new Region();
            paragraphSpacer.getStyleClass().add("paragraph-spacer");
            return paragraphSpacer;
        }

        Matcher eventTimeMatcher = EVENT_TIME_PATTERN.matcher(messageLine);
        if (eventTimeMatcher.find()) {
            String taskText = messageLine.substring(0, eventTimeMatcher.start()).stripTrailing();
            return createTaskItem(taskText,
                    createTimeChip("from", eventTimeMatcher.group("from")),
                    createTimeChip("to", eventTimeMatcher.group("to")));
        }

        Matcher deadlineTimeMatcher = DEADLINE_TIME_PATTERN.matcher(messageLine);
        if (deadlineTimeMatcher.find()) {
            String taskText = messageLine.substring(0, deadlineTimeMatcher.start()).stripTrailing();
            return createTaskItem(taskText,
                    createTimeChip("by", deadlineTimeMatcher.group("by")));
        }

        if (TASK_LINE_PATTERN.matcher(messageLine).matches()) {
            return createTaskItem(messageLine);
        }
        return createWrappingLabel(messageLine, "message-line");
    }

    /**
     * Creates a vertically spaced task row with optional wrapping time chips.
     *
     * @param taskText Task number, markers, and description.
     * @param timeChips Optional date-time chips belonging to the task.
     * @return Structured task display.
     */
    private VBox createTaskItem(String taskText, HBox... timeChips) {
        Label taskLabel = createWrappingLabel(taskText, "task-text");
        VBox taskItem = new VBox(taskLabel);
        taskItem.getStyleClass().add("task-item");

        if (timeChips.length > 0) {
            FlowPane timeDetails = new FlowPane();
            timeDetails.getChildren().addAll(timeChips);
            timeDetails.getStyleClass().add("time-details");
            taskItem.getChildren().add(timeDetails);
        }
        return taskItem;
    }

    /**
     * Creates one compact label/value box for a task date and time.
     *
     * @param keyword Relationship of the time to the task, such as {@code from}.
     * @param dateTime Display-formatted date and time.
     * @return Styled date-time chip.
     */
    private HBox createTimeChip(String keyword, String dateTime) {
        Label keywordLabel = new Label(keyword);
        keywordLabel.getStyleClass().add("time-keyword");
        Label dateTimeLabel = new Label(dateTime);
        dateTimeLabel.getStyleClass().add("time-value");

        HBox timeChip = new HBox(keywordLabel, dateTimeLabel);
        timeChip.setAlignment(Pos.CENTER_LEFT);
        timeChip.getStyleClass().add("time-chip");
        return timeChip;
    }

    /**
     * Creates a wrapping message label and removes the CLI-only leading tabs
     * that would otherwise waste horizontal space in the graphical interface.
     *
     * @param message Text to display.
     * @param styleClass CSS class describing the message role.
     * @return Styled, wrapping message label.
     */
    private Label createMessageLabel(String message, String styleClass) {
        Label messageLabel = createWrappingLabel(formatForGraphicalDisplay(message));
        messageLabel.getStyleClass().addAll("message-bubble", styleClass);
        return messageLabel;
    }

    /**
     * Creates a label that uses all available card width before wrapping.
     *
     * @param text Text displayed by the label.
     * @param styleClasses CSS classes applied to the label.
     * @return Wrapping label.
     */
    private Label createWrappingLabel(String text, String... styleClasses) {
        Label messageLabel = new Label(text);
        messageLabel.setWrapText(true);
        messageLabel.setMinWidth(0);
        messageLabel.setMaxWidth(Double.MAX_VALUE);
        messageLabel.getStyleClass().addAll(styleClasses);
        return messageLabel;
    }

    /**
     * Removes indentation used only by the command-line presentation.
     *
     * @param message Magnus response text.
     * @return Text formatted for the graphical interface.
     */
    private String formatForGraphicalDisplay(String message) {
        return message.strip().replaceAll("(?m)^\\t", "");
    }

    /**
     * Creates a compact circular avatar containing the user or Magnus image.
     * Landscape images are cropped to a centred square before being displayed.
     *
     * @param isUserMessage Whether the avatar belongs to the user.
     * @param imageSize Diameter of the displayed image in pixels.
     * @return The styled avatar container.
     */
    private StackPane createAvatar(boolean isUserMessage, int imageSize) {
        Image image = isUserMessage ? this.userAvatar : this.magnusAvatar;
        double squareLength = Math.min(image.getWidth(), image.getHeight());
        double viewportX = (image.getWidth() - squareLength) / 2;
        double viewportY = (image.getHeight() - squareLength) / 2;

        ImageView imageView = new ImageView(image);
        imageView.setViewport(new Rectangle2D(viewportX, viewportY, squareLength, squareLength));
        imageView.setFitWidth(imageSize);
        imageView.setFitHeight(imageSize);
        imageView.setPreserveRatio(true);
        imageView.setAccessibleText(isUserMessage ? "User portrait" : "Magnus portrait");
        double avatarRadius = imageSize / 2.0;
        imageView.setClip(new Circle(avatarRadius, avatarRadius, avatarRadius));

        int containerSize = imageSize + AVATAR_BORDER_ALLOWANCE;
        StackPane avatar = new StackPane(imageView);
        avatar.setMinSize(containerSize, containerSize);
        avatar.setPrefSize(containerSize, containerSize);
        avatar.setMaxSize(containerSize, containerSize);
        avatar.getStyleClass().addAll("avatar", isUserMessage ? "user-avatar" : "magnus-avatar");
        return avatar;
    }
}
