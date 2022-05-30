package org.openjfx.javafx;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.openjfx.puzzle.PuzzleState;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BoardController implements Initializable {
    private PuzzleState state;

    static class DragContext {
        double x;
        double y;
    }

    DragContext dragContext = new DragContext();

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        random();
        show();
        for(Label l : labels) makeDraggable(l);
    }

    void makeDraggable(Node node) {
        node.setOnMousePressed( onMousePressedEventHandler);
        node.setOnMouseDragged( onMouseDraggedEventHandler);
    }

    EventHandler<MouseEvent> onMousePressedEventHandler = event -> {
        Node node = ((Node) (event.getSource()));

        dragContext.x = node.getTranslateX() - event.getSceneX();
        dragContext.y = node.getTranslateY() - event.getSceneY();
    };

    EventHandler<MouseEvent> onMouseDraggedEventHandler = event -> {
        Node node = ((Node) (event.getSource()));

        node.setTranslateX( dragContext.x + event.getSceneX());
        node.setTranslateY( dragContext.y + event.getSceneY());

    };
}

