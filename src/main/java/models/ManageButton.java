package models;

import javafx.animation.ScaleTransition;
import javafx.scene.control.Button;
import javafx.util.Duration;

/**
 * Redefined button class whit made sizes and animation.
 */
public class ManageButton extends Button {
    public ManageButton(String right){
        this.setPrefSize(100, 60);
        this.setText(right);
        this.setStyle("-fx-background-color: grey");

        setOnMouseEntered(event -> {
            ScaleTransition st = new ScaleTransition(Duration.seconds(0.5), this);
            st.setToX(1.1);
            st.setToY(1.1);
            st.play();
        });

        setOnMouseExited(event -> {
            ScaleTransition st = new ScaleTransition(Duration.seconds(0.5), this);
            st.setFromX(1.1);
            st.setFromY(1.1);
            st.setToX(1);
            st.setToY(1);
            st.play();
        });
    }
}
