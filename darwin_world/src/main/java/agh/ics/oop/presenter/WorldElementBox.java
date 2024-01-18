package agh.ics.oop.presenter;

import agh.ics.oop.model.elements.WorldElement;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class WorldElementBox {
    private VBox fxElement;

    public WorldElementBox(WorldElement element, double width, double height) {
        ImageView imageView = getImageView(element, width, height);
        createFxElement(imageView);
    }

    private static ImageView getImageView(WorldElement element, double width, double height) {
        Image image = new Image(element.getTexturePath());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        return imageView;
    }

    private void createFxElement(ImageView imageView) {
        fxElement = new VBox();
        fxElement.getChildren().add(imageView);
        fxElement.setAlignment(Pos.CENTER);
    }

    public VBox getFxElement() {
        return fxElement;
    }
}
