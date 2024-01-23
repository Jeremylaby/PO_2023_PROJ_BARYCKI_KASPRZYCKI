package agh.ics.oop.presenter.simulation;

import agh.ics.oop.model.elements.WorldElement;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.List;

public class WorldElementBox {
    private final VBox fxElement;

    public WorldElementBox(WorldElement element, double size, boolean isSelected, List<Integer> genes) {
        fxElement = createFxElement(getLabel(element.getColor(), size, isSelected,element.hasDominatingGenome(genes)));
        if (element.isSelectable()) {
            fxElement.setUserData(element.getPosition());
        }
    }

    private static Label getLabel(Color color, double size, boolean isSelected,boolean isdominating) {
        Label label = new Label();

        StringBuilder styles = new StringBuilder();

        String colorRgb = "rgb(%s,%s,%s)".formatted(
                255 * color.getRed(),
                255 * color.getGreen(),
                255 * color.getBlue()
        );
        styles.append("-fx-background-color: %s;".formatted(colorRgb));

        if (isSelected) {
            styles.append("-fx-border-width: 5;");
            styles.append("-fx-border-color: rgb(255, 255, 0);");
        }
        if(isdominating){
            styles.append("-fx-border-width: 5;");
            styles.append("-fx-border-color: rgb(250, 0, 0);");
        }
        if(isdominating && isSelected){
            styles.append("-fx-border-width: 5;");
            styles.append("-fx-border-color: rgb(253, 128, 0);");
        }

        label.setStyle(styles.toString());

        label.setMinHeight(size);
        label.setPrefWidth(size);

        return label;
    }

    private VBox createFxElement(Node node) {
        VBox vBox = new VBox();
        vBox.getChildren().add(node);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    public VBox getFxElement() {
        return fxElement;
    }
}
