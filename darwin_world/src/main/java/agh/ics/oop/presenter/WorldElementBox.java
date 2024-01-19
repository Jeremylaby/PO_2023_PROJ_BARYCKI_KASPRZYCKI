package agh.ics.oop.presenter;

import agh.ics.oop.model.elements.WorldElement;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class WorldElementBox {
    private VBox fxElement;

    public WorldElementBox(WorldElement element, double size) {
        createFxElement(getLabel(element.getColor(), size));
    }

    private static ImageView getImageView(String texturePath, double size) {
        Image image = new Image(texturePath);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);
        return imageView;
    }

    private static Label getLabel(Color color, double size) {
        Label label = new Label();
        String colorRgb = "rgb(%s,%s,%s)"
                .formatted(255*color.getRed(), 255*color.getGreen(), 255*color.getBlue());
        label.setStyle("-fx-background-color: %s".formatted(colorRgb));
        label.setMinHeight(size);
        label.setPrefWidth(size);
        return label;
    }

    private void createFxElement(Node node) {
        fxElement = new VBox();
        fxElement.getChildren().add(node);
        fxElement.setAlignment(Pos.CENTER);
    }

    public VBox getFxElement() {
        return fxElement;
    }
}
