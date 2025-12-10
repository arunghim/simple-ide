package ide;

import interpreter.Id;
import interpreter.Parser;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Optional;

public class SimpleIDE extends Application {

    private static final String CODE_EMOJI = "📝";
    private static final String DATA_EMOJI = "📊";

    private ListView<String> fileExplorer;
    private ObservableList<String> explorerItems;
    private TextArea codeEditor;
    private TextArea dataEditor;
    private TextArea consoleArea;
    private Label codeTabLabel;
    private Label dataTabLabel;
    private Button executeBtn;
    private Button formatBtn;
    private Button newCodeBtn;
    private Button newDataBtn;
    private Button themeBtn;
    private Scene scene;

    private final HashMap<String, String> codeFiles = new HashMap<>();
    private final HashMap<String, String> dataFiles = new HashMap<>();
    private String selectedCodeFile;
    private String selectedDataFile;
    private boolean isDarkTheme = true;

    @Override
    public void start(Stage stage) {

        HBox topRibbon = new HBox();
        topRibbon.getStyleClass().add("ribbon-bar");
        topRibbon.setPadding(new Insets(8, 12, 8, 12));
        topRibbon.setSpacing(12);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        executeBtn = createRibbonButton("EXECUTE");
        formatBtn = createRibbonButton("FORMAT");
        newCodeBtn = createRibbonButton("NEW CODE");
        newDataBtn = createRibbonButton("NEW INPUT");
        themeBtn = createRibbonButton("THEME");

        executeBtn.setOnAction(e -> executeAction());
        formatBtn.setOnAction(e -> formatAction());
        newCodeBtn.setOnAction(e -> promptNewCodeFile());
        newDataBtn.setOnAction(e -> promptNewDataFile());
        themeBtn.setOnAction(e -> toggleTheme());

        executeBtn.setDisable(true);
        formatBtn.setDisable(true);

        HBox leftButtons = new HBox(6);
        leftButtons.getChildren().addAll(executeBtn, formatBtn);

        HBox rightButtons = new HBox(6);
        rightButtons.getChildren().addAll(newCodeBtn, newDataBtn, themeBtn);

        topRibbon.getChildren().addAll(leftButtons, spacer, rightButtons);

        explorerItems = FXCollections.observableArrayList();
        fileExplorer = new ListView<>(explorerItems);
        fileExplorer.setPrefWidth(200);
        fileExplorer.setPadding(new Insets(12, 12, 12, 12));
        fileExplorer.getStyleClass().add("file-explorer");
        fileExplorer.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV == null) return;
            if (newV.startsWith(CODE_EMOJI)) {
                String name = newV.substring(CODE_EMOJI.length()).trim();
                selectCodeFile(name);
            } else if (newV.startsWith(DATA_EMOJI)) {
                String name = newV.substring(DATA_EMOJI.length()).trim();
                selectDataFile(name);
            }
        });

        codeEditor = new TextArea();
        codeEditor.getStyleClass().add("editor-area");
        codeEditor.setWrapText(false);
        codeEditor.setEditable(false);
        codeTabLabel = new Label("");
        codeTabLabel.getStyleClass().add("tab-label");
        codeTabLabel.setVisible(false);
        codeEditor.textProperty().addListener((o, oldV, newV) -> {
            if (selectedCodeFile != null) {
                codeFiles.put(selectedCodeFile, newV);
                updateButtonStates();
            }
        });

        VBox codeBox = new VBox();
        codeBox.getStyleClass().add("editor-container");
        codeBox.setPadding(new Insets(12, 12, 12, 12));
        codeBox.getChildren().addAll(codeTabLabel, codeEditor);
        VBox.setVgrow(codeEditor, Priority.ALWAYS);

        dataEditor = new TextArea();
        dataEditor.getStyleClass().add("editor-area");
        dataEditor.setWrapText(false);
        dataEditor.setEditable(false);
        dataTabLabel = new Label("");
        dataTabLabel.getStyleClass().add("tab-label");
        dataTabLabel.setVisible(false);
        dataEditor.textProperty().addListener((o, oldV, newV) -> {
            if (selectedDataFile != null) {
                dataFiles.put(selectedDataFile, newV);
            }
        });

        VBox dataBox = new VBox();
        dataBox.getStyleClass().add("editor-container");
        dataBox.setPadding(new Insets(12, 12, 12, 12));
        dataBox.getChildren().addAll(dataTabLabel, dataEditor);
        VBox.setVgrow(dataEditor, Priority.ALWAYS);

        SplitPane codeDataSplit = new SplitPane(codeBox, dataBox);
        codeDataSplit.setOrientation(Orientation.HORIZONTAL);
        codeDataSplit.setDividerPositions(0.65);
        SplitPane.setResizableWithParent(codeBox, true);
        SplitPane.setResizableWithParent(dataBox, true);

        consoleArea = new TextArea();
        consoleArea.setEditable(false);
        consoleArea.getStyleClass().add("editor-area");

        Label consoleLabel = new Label("CONSOLE");
        consoleLabel.getStyleClass().add("console-header");

        VBox consoleBox = new VBox();
        consoleBox.getStyleClass().add("editor-container");
        consoleBox.setPadding(new Insets(12, 12, 12, 12));
        consoleBox.getChildren().addAll(consoleLabel, consoleArea);
        VBox.setVgrow(consoleArea, Priority.ALWAYS);

        SplitPane verticalSplit = new SplitPane(codeDataSplit, consoleBox);
        verticalSplit.setOrientation(Orientation.VERTICAL);
        verticalSplit.setDividerPositions(0.75);

        SplitPane mainSplit = new SplitPane(fileExplorer, verticalSplit);
        mainSplit.setOrientation(Orientation.HORIZONTAL);
        mainSplit.setDividerPositions(0.15);

        BorderPane root = new BorderPane();
        root.setTop(topRibbon);
        root.setCenter(mainSplit);

        scene = new Scene(root, 1400, 900);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("MINIMAL IDE");
        stage.setMinWidth(900);
        stage.setMinHeight(600);

        refreshExplorer();

        stage.show();
    }

    private Button createRibbonButton(String text) {
        Button b = new Button(text);
        b.getStyleClass().add("ribbon-button");
        return b;
    }

    private void toggleTheme() {
        isDarkTheme = !isDarkTheme;
        if (isDarkTheme) {
            scene.getRoot().getStyleClass().remove("light-theme");
            scene.getRoot().getStyleClass().add("dark-theme");
        } else {
            scene.getRoot().getStyleClass().remove("dark-theme");
            scene.getRoot().getStyleClass().add("light-theme");
        }
    }

    private void addCodeFile(String name) {
        String key = name.trim().toUpperCase();
        if (codeFiles.containsKey(key)) return;
        codeFiles.put(key, "");
        refreshExplorer();
    }

    private void addDataFile(String name) {
        String key = name.trim().toUpperCase();
        if (dataFiles.containsKey(key)) return;
        dataFiles.put(key, "");
        refreshExplorer();
    }

    private void selectCodeFile(String name) {
        if (name == null) {
            selectedCodeFile = null;
            codeEditor.clear();
            codeEditor.setEditable(false);
            codeTabLabel.setVisible(false);
            return;
        }
        String key = name.trim().toUpperCase();
        if (!codeFiles.containsKey(key)) return;
        if (selectedCodeFile != null) {
            codeFiles.put(selectedCodeFile, codeEditor.getText());
        }
        selectedCodeFile = key;
        codeEditor.setText(codeFiles.get(key));
        codeEditor.setEditable(true);
        codeTabLabel.setText(key);
        codeTabLabel.setVisible(true);
        updateButtonStates();
    }

    private void selectDataFile(String name) {
        if (name == null) {
            selectedDataFile = null;
            dataEditor.clear();
            dataEditor.setEditable(false);
            dataTabLabel.setVisible(false);
            return;
        }
        String key = name.trim().toUpperCase();
        if (!dataFiles.containsKey(key)) return;
        if (selectedDataFile != null) {
            dataFiles.put(selectedDataFile, dataEditor.getText());
        }
        selectedDataFile = key;
        dataEditor.setText(dataFiles.get(key));
        dataEditor.setEditable(true);
        dataTabLabel.setText(key);
        dataTabLabel.setVisible(true);
    }

    private void refreshExplorer() {
        explorerItems.clear();
        for (String k : codeFiles.keySet()) {
            explorerItems.add(CODE_EMOJI + " " + k);
        }
        for (String k : dataFiles.keySet()) {
            explorerItems.add(DATA_EMOJI + " " + k);
        }
    }

    private void updateButtonStates() {
        boolean hasCode = codeFiles.values().stream().anyMatch(text -> !text.isBlank());
        Platform.runLater(() -> {
            executeBtn.setDisable(!hasCode);
            formatBtn.setDisable(!hasCode);
        });
    }

    private void promptNewCodeFile() {
        TextInputDialog d = new TextInputDialog("PROGRAM " + (codeFiles.size() + 1));
        d.setTitle("New Code File");
        d.setHeaderText(null);
        d.setContentText("File name:");
        Optional<String> r = d.showAndWait();
        r.ifPresent(n -> {
            String key = n.trim().toUpperCase();
            if (!key.isEmpty() && !codeFiles.containsKey(key)) {
                addCodeFile(key);
                selectCodeFile(key);
            }
        });
    }

    private void promptNewDataFile() {
        TextInputDialog d = new TextInputDialog("INPUT " + (dataFiles.size() + 1));
        d.setTitle("New Input File");
        d.setHeaderText(null);
        d.setContentText("File name:");
        Optional<String> r = d.showAndWait();
        r.ifPresent(n -> {
            String key = n.trim().toUpperCase();
            if (!key.isEmpty() && !dataFiles.containsKey(key)) {
                addDataFile(key);
                selectDataFile(key);
            }
        });
    }

    private void executeAction() {
        if (selectedCodeFile != null) {
            codeFiles.put(selectedCodeFile, codeEditor.getText());
        }
        if (selectedDataFile != null) {
            dataFiles.put(selectedDataFile, dataEditor.getText());
        }
        consoleArea.clear();
        String codeText = selectedCodeFile != null ? codeFiles.get(selectedCodeFile) : "";
        String dataText = selectedDataFile != null ? dataFiles.get(selectedDataFile) : "";
        PrintStream ps = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                Platform.runLater(() -> consoleArea.appendText(String.valueOf((char) b)));
            }
        });
        PrintStream originalOut = System.out;
        PrintStream originalErr = System.err;
        try {
            Id.reset();
            System.setOut(ps);
            System.setErr(ps);
            Parser parser = new Parser(codeText, dataText, ps);
            parser.parse();
            parser.execute();
        } catch (Exception ex) {
            consoleArea.appendText("ERROR: " + ex.getMessage() + "\n");
        } finally {
            System.setOut(originalOut);
            System.setErr(originalErr);
        }
    }

    private void formatAction() {
        if (selectedCodeFile != null) {
            codeFiles.put(selectedCodeFile, codeEditor.getText());
        }
        if (selectedCodeFile != null) {
            try {
                String codeText = codeFiles.get(selectedCodeFile);
                StringBuilder formatted = new StringBuilder();
                PrintStream ps = new PrintStream(new OutputStream() {
                    @Override
                    public void write(int b) {
                        formatted.append((char) b);
                    }
                });
                Id.reset();
                Parser parser = new Parser(codeText, "", ps);
                parser.parse();
                parser.print();
                String formattedText = formatted.toString().stripLeading();
                codeFiles.put(selectedCodeFile, formattedText);
                codeEditor.setText(formattedText);
            } catch (Exception ex) {
                consoleArea.appendText("FORMAT ERROR: " + ex.getMessage() + "\n");
            }
        }
    }
}
