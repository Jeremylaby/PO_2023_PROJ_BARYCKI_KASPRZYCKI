package agh.ics.oop.presenter;

import agh.ics.oop.model.elements.WorldElement;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class WorldElementBox {
    private final VBox fxElement;

    public WorldElementBox(WorldElement element, double size, boolean isSelected) {
        fxElement = createFxElement(getLabel(element.getColor(), size, isSelected));
        if (element.isSelectable()) {
            fxElement.setUserData(element.getPosition());
        }
    }

    private static Label getLabel(Color color, double size, boolean isSelected) {
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
