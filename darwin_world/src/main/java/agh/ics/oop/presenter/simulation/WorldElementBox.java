package agh.ics.oop.presenter.simulation;

import agh.ics.oop.model.elements.WorldElement;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.List;

public class WorldElementBox {
    private static final String DOMINATING_AND_SELECTED = "rgb(253, 128, 0);";
    private static final String SELECTED = "rgb(255, 255, 0);";
    private static final String DOMINATING = "rgb(255, 0, 0);";
    private final Node fxElement;

    public WorldElementBox(WorldElement element, double size, boolean isSelected, boolean hasDominatingGenes) {
        fxElement = getLabel(element.getColor(), size, isSelected, hasDominatingGenes);
    }

    private static Label getLabel(Color color, double size, boolean isSelected, boolean hasDominatingGenes) {
        Label label = new Label();

        label.setStyle(getStyles(color, isSelected, hasDominatingGenes));
        label.setMinHeight(size);
        label.setPrefWidth(size);

        label.setAlignment(Pos.CENTER);

        return label;
    }

    private static String getStyles(Color color, boolean isSelected, boolean hasDominatingGenes) {
        StringBuilder styles = new StringBuilder();

        styles
                .append("-fx-background-color: rgb(")
                .append(255 * color.getRed()).append(",")
                .append(255 * color.getGreen()).append(",")
                .append(255 * color.getBlue())
                .append(");")
                .append("-fx-border-width: 5;");

        if (hasDominatingGenes || isSelected) {
            styles.append("-fx-border-color: ");

            if (hasDominatingGenes && isSelected) {
                styles.append(DOMINATING_AND_SELECTED);
            } else if (isSelected) {
                styles.append(SELECTED);
            } else {
                styles.append(DOMINATING);
            }
        }

        return styles.toString();
    }

    public Node getFxElement() {
        return fxElement;
    }
}
