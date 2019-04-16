package example;

import guide.Guide;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Controller {
    @FXML
    Button top_left_button;
    @FXML
    Button top_right_button;
    @FXML
    Button bottom_left_button;
    @FXML
    Button bottom_right_button;
    @FXML
    Button left_button;
    @FXML
    Button right_button;
    @FXML
    Button top_button;
    @FXML
    Button bottom_button;
    @FXML
    Button center_button;

    Guide guide;

    Guide.GuideListener listener;

    public Controller() {

    }

    public void initViews(){
        initGuide();
        center_button.setOnMouseClicked(event -> {
            guide.show();
        });
    }

    private void initGuide(){
        listener = (oldIndex, newIndex) -> {};
        guide = Guide.getInstance();
        guide.setListener(listener);
        guide.addGuideStep(top_left_button, "TOP-LEFT Button", "Popup position is determined as BOTTOM-RIGHT", Guide.Position.BOTTOM_RIGHT);
        guide.addGuideStep(top_button, "TOP Button", "Popup position is determined as BOTTOM", Guide.Position.BOTTOM);
        guide.addGuideStep(top_right_button, "TOP-RIGHT Button", "Popup position is determined as BOTTOM-LEFT", Guide.Position.BOTTOM_LEFT);
        guide.addGuideStep(left_button, "LEFT Button", "Popup position is determined as RIGHT", Guide.Position.RIGHT);
        guide.addGuideStep(center_button, "CENTER Button", "Popup position is determined as CENTER", Guide.Position.CENTER);
        guide.addGuideStep(right_button, "RIGHT Button", "Popup position is determined as LEFT", Guide.Position.LEFT);
        guide.addGuideStep(bottom_left_button, "BOTTOM-LEFT Button", "Popup position is determined as TOP-RIGHT", Guide.Position.TOP_RIGHT);
        guide.addGuideStep(bottom_button, "BOTTOM Button", "Popup position is determined as TOP", Guide.Position.TOP);
        guide.addGuideStep(bottom_right_button, "BOTTOM-RIGHT Button", "Popup position is determined as TOP-LEFT", Guide.Position.TOP_LEFT);
    }
}
