package magnus;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Displays the main Magnus application window.
 */
public class Main extends Application {
    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 400;

    /**
     * Creates and displays a plain white application window.
     *
     * @param stage Primary window supplied by JavaFX.
     */
    @Override
    public void start(Stage stage) {
        Pane root = new Pane();
        root.setStyle("-fx-background-color: white;");

        stage.setTitle("Magnus");
        stage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
        stage.show();
    }
}
