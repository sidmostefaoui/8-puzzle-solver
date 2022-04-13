package org.openjfx.javafx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.openjfx.puzzle.PuzzleState;

import java.util.List;

public class BoardController {
    private PuzzleState state;

    @FXML
    private List<Label> labels;


    public void show() {
        Integer[] arr = state.as_integer_array();
        for (int i = 0; i < 9; i++)  {
            if (arr[i] == 0) labels.get(i).setText("");
            else labels.get(i).setText(arr[i].toString());
        }
    }

    public void random() {
        state = new PuzzleState();
    }



}
