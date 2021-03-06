package mainWindow;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sun.font.TextLabel;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;


public class CreatingNewDatabaseScene {
    final int DEFAULT_NUMBER_OF_COMPONENTS = 10;
    int gridSize = DEFAULT_NUMBER_OF_COMPONENTS;
    int row = 2;
    List<GamePair> textFields = new ArrayList<>();

    CreatingNewDatabaseScene(GameCreator gameCreator,Stage window1,MainDatabase mainDatabase) {

        Stage window = new Stage();
        window.setTitle("Creating new database");
        window.initModality(Modality.APPLICATION_MODAL);
        window.initOwner(window1);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);



        for (int i = 0; i < gridSize; i++) {
            row = addFields(grid, row);
        }
        TextField databaseName=new TextField();
        databaseName.setPromptText("Set your database name:");
        databaseName.setAlignment(Pos.CENTER);
        Label label=new Label("Let's fill your database:");
        label.setAlignment(Pos.CENTER);
        Button addFieldsButton = new Button("Add fields");
        Button submit = new Button("Submit");
        Button back = new Button("Back");

        addFieldsButton.setOnAction(event -> {
            addFields(grid,row);
            GridPane.setConstraints(addFieldsButton, 0, row+1, 2, 1);
            GridPane.setConstraints(submit, 0, row+2, 2, 1);
            GridPane.setConstraints(back, 0, row+3, 2, 1);
            row++;
        });
        GridPane.setConstraints(databaseName, 0,0, 2, 1);
        GridPane.setHalignment(databaseName, HPos.CENTER);
        GridPane.setConstraints(label, 0,1, 2, 1);
        GridPane.setHalignment(label, HPos.CENTER);
        GridPane.setConstraints(addFieldsButton, 0, row+1, 2, 1);
        GridPane.setHalignment(addFieldsButton, HPos.CENTER);
        grid.getChildren().addAll(label,databaseName,addFieldsButton);


        // grid.getChildren().remove(submit);
        submit.setOnAction(event -> {
            if(!databaseName.getText().equals("") && !mainDatabase.contain(databaseName.getText())) {
                ArrayList<String> writeLines = new ArrayList<>();
                writeLines.add(databaseName.getText());
                for (GamePair pair : textFields) {
                    if (pair.t1.getText().length() > 0 && pair.t2.getText().length() > 0)
                        writeLines.add(pair.t1.getText() + ":" + pair.t2.getText());
                }
                Path file = Paths.get("src" + System.getProperty("file.separator") + "databases" +
                        System.getProperty("file.separator") + mainDatabase.getSize() + ".txt");
                    try {
                        Files.write(file, writeLines, Charset.defaultCharset());

                        if (!Files.exists(Paths.get("src" + File.separator + "databases" + File.separator + "importedDatabases.txt")))
                            Files.createFile(Paths.get("src" + File.separator + "databases" + File.separator + "importedDatabases.txt"));
                        Files.write(Paths.get("src" + File.separator + "databases" + File.separator + "importedDatabases.txt"), (databaseName.getText() + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);

                        mainDatabase.insert(databaseName.getText());
                        gameCreator.updateCategories();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Database created");
                        alert.setContentText("Database created successfully.");
                        alert.showAndWait();
                        window.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error during creating new database");
                alert.setContentText("The database already exists! Please change the name");
                if(databaseName.getText().equals("")) alert.setContentText("Category name format is wrong!");
                alert.showAndWait();
            }

        });
        GridPane.setConstraints(submit, 0, row+2, 2, 1);
        GridPane.setHalignment(submit, HPos.CENTER);
        grid.getChildren().add(submit);

        GridPane.setConstraints(back, 0, row+3, 2, 1);
        GridPane.setHalignment(back, HPos.CENTER);
        back.setOnAction(event -> {
            window.close();
        });
        grid.getChildren().add(back);



        ScrollPane scrollPane = new ScrollPane();
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        scrollPane.setPrefHeight(650);
        scrollPane.setPrefWidth(500);
        scrollPane.setContent(grid);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane);
        scene.getStylesheets().add(getClass().getResource(".."+ File.separator+"styleScheets"+File.separator+"newDatabaseStyleScheet.css").toExternalForm());
        window.setScene(scene);
        window.showAndWait();
    }


    private int addFields(GridPane grid, int row) {
        TextField textField1 = new TextField();
        TextField textField2 = new TextField();
        textFields.add(new GamePair(textField1,textField2));
        GridPane.setConstraints(textField1, 0, row);
        GridPane.setConstraints(textField2, 1, row++);
        grid.getChildren().addAll(textField1, textField2);
        return row;
    }

    private class GamePair {
        TextField t1;
        TextField t2;

        public GamePair(TextField t1, TextField t2) {
            this.t1 = t1;
            this.t2 = t2;
        }
    }
}
