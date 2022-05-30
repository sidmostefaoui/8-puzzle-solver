package org.openjfx.javafx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.openjfx.benchmark.Statistics;
import org.openjfx.puzzle.*;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class HelloController {

    public HBox root;
    private PuzzleState initial;
    private PuzzleState target;
    private IPuzzleSolver solver;

    @FXML private List<Label> initial_grid_labels;
    @FXML private List<Label> target_grid_labels;
    @FXML public ComboBox<String> algorithm_combo_box;
    @FXML private TableView<Statistics> table_statistics;
    @FXML private TableColumn<Statistics, String> col_algorithm;
    @FXML private TableColumn<Statistics, Boolean> col_solution;
    @FXML private TableColumn<Statistics, Integer> col_iterations;
    @FXML private TableColumn<Statistics, Long> col_duration;
    @FXML private TableColumn<Statistics, Long> col_memory;


    private void showBoard() {
        Integer[] initial_arr = initial.as_integer_array();
        for (int i = 0; i < 9; i++)  {
            if (initial_arr[i] == 0) initial_grid_labels.get(i).setText("");
            else initial_grid_labels.get(i).setText(initial_arr[i].toString());
        }

        Integer[] target_arr = target.as_integer_array();
        for (int i = 0; i < 9; i++)  {
            if (target_arr[i] == 0) target_grid_labels.get(i).setText("");
            else target_grid_labels.get(i).setText(target_arr[i].toString());
        }

        col_algorithm.setCellValueFactory(new PropertyValueFactory<>("algorithm"));
        col_solution.setCellValueFactory(new PropertyValueFactory<>("solution"));
        col_iterations.setCellValueFactory(new PropertyValueFactory<>("iterations"));
        col_duration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        col_memory.setCellValueFactory(new PropertyValueFactory<>("memory"));
    }

    @FXML
    protected void onGenerateButtonClick() {
        initial = new PuzzleState();
        target = new PuzzleState();
        table_statistics.getItems().clear();
        showBoard();
    }

    @FXML
    protected void onSolveButtonClick() throws IOException {

        String algorithm = algorithm_combo_box.getValue();

        switch (algorithm) {
            case "A* inversions":
                solver = new AStar(initial, target, PuzzleState::inversions);
                break;
            case "A* mal placée":
                solver = new AStar(initial, target, PuzzleState::misplaced);
                break;
            case "A* distances":
                solver = new AStar(initial, target, PuzzleState::distances);
            case "BFS":
                solver = new BFS(initial, target);
                break;
            case "DFS":
                solver = new DFS(initial, target);
                break;
            case "Genetic":
                solver = new Genetic(initial, target, PuzzleState::inversions);
                break;
        }

        table_statistics.getItems().add(new Statistics(algorithm, solver.findSolution(),
                                solver.iterations(), solver.duration(), solver.memory()));

    }
}