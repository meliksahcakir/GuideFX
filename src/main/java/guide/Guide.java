package guide;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class Guide {
    public enum Position{
        TOP,
        LEFT,
        BOTTOM,
        RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        TOP_LEFT,
        TOP_RIGHT,
        CENTER
    }

    private ArrayList<GuideStep> steps;

    private int mCurrentStep = 0;

    private static Guide instance;
    private GuideListener listener;
    private boolean showing = false;
    private boolean opacityChangeEnabled = false;

    private Color defaultBackgroundColor = Color.color(0.6,0.2,0.2,0.8);

    private Guide() {
        steps = new ArrayList<>();
    }

    public static Guide getInstance(){
        if(instance == null){
            instance = new Guide();
        }else{
            if(!instance.steps.isEmpty()){
                GuideStep step =  instance.steps.get(instance.mCurrentStep);
                if(step != null){
                    step.tooltip.hide();
                }
            }
        }
        return instance;
    }

    public void setListener(GuideListener listener) {
        this.listener = listener;
    }

    public boolean isOpacityChangeEnabled() {
        return opacityChangeEnabled;
    }

    public void setOpacityChangeEnabled(boolean opacityChangeEnabled) {
        this.opacityChangeEnabled = opacityChangeEnabled;
    }

    public boolean isShowing() {
        return showing;
    }

    public void addGuideStep(Node target, String title, String message, Position position){
        addGuideStep(target, title, message, position, defaultBackgroundColor);
    }

    public void addGuideStep(Node target, String title, String message, Position position, Color backgroundColor){
        if(!showing){
            Label titleLabel = new Label(title);
            titleLabel.setFont(new Font("Calibri", 16));
            titleLabel.setStyle("-fx-text-fill: white;");
            Text messageText = new Text(message);
            messageText.setFont(new Font("Calibri",16));
            messageText.setFill(Color.WHITE);
            messageText.setWrappingWidth(200);
            GuideStep step = new GuideStep(target, titleLabel, messageText, position, backgroundColor);
            steps.add(step);
        }else{
            System.out.println("You should add steps before calling show method.");
        }
    }

    public void addGuideStep(Node target, Node titleNode, Node messageNode, Position position){
        if(!showing){
            GuideStep step = new GuideStep(target, titleNode, messageNode, position, defaultBackgroundColor);
            steps.add(step);
        }else{
            System.out.println("You should add steps before calling show method.");
        }
    }

    public void addGuideStep(Node target, Node titleNode, Node messageNode, Position position, Color backgroundColor){
        if(!showing){
            GuideStep step = new GuideStep(target, titleNode, messageNode, position, backgroundColor);
            steps.add(step);
        }else{
            System.out.println("You should add steps before calling show method.");
        }
    }

    public int getSize(){
        return steps.size();
    }

    public void show(){
        showing = true;
        if(!steps.isEmpty()){
            steps.get(mCurrentStep).tooltip.hide();
            mCurrentStep = 0;
            steps.get(0).show();
        }
        if(listener != null){
            listener.onStepShowed(-1,0);
        }
    }

    public void next(){
        if(mCurrentStep == steps.size() - 1){
            if(listener != null){
                listener.onStepShowed(mCurrentStep,-1);
            }
            steps = new ArrayList<>();
            mCurrentStep = 0;
            showing = false;
        }else{
            mCurrentStep++;
            steps.get(mCurrentStep).show();
            if(listener != null){
                listener.onStepShowed(mCurrentStep-1,mCurrentStep);
            }
        }
    }

    public void prev(){
        if(mCurrentStep == 0){
            steps = new ArrayList<>();
            if(listener != null){
                listener.onStepShowed(-1,0);
            }
        }else{
            mCurrentStep--;
            steps.get(mCurrentStep).show();
            if(listener != null){
                listener.onStepShowed(mCurrentStep+1,mCurrentStep);
            }
        }
    }

    class GuideStep {
        Position position;
        Node target;
        Tooltip tooltip;
        Button left;
        Button right;
        HBox hBox;
        VBox vBox;
        Color backgroundColor;
        Node titleNode;
        Node messageNode;

        GuideStep(Node target, Node titleNode, Node messageNode, Position position, Color backgroundColor){
            this.target = target;
            this.titleNode = titleNode;
            this.messageNode = messageNode;
            this.position = position;
            this.backgroundColor = backgroundColor;
            tooltip = new Tooltip();
            vBox = new VBox();
            vBox.setSpacing(10);
            hBox = new HBox();
            hBox.setSpacing(10);
            hBox.setAlignment(Pos.CENTER);
            left = new Button();
            FontAwesomeIconView view = new FontAwesomeIconView(FontAwesomeIcon.ARROW_LEFT);
            view.setSize("16");
            view.setFill(Color.WHITE);
            left.setGraphic(view);
            left.setStyle("-fx-background-color: transparent;");
            right = new Button();
            view = new FontAwesomeIconView(FontAwesomeIcon.ARROW_RIGHT);
            view.setSize("16");
            view.setFill(Color.WHITE);
            right.setGraphic(view);
            right.setStyle("-fx-background-color: transparent;");
            hBox.getChildren().addAll(left, titleNode, right);
            vBox.getChildren().addAll(hBox, messageNode);
            String rgba = String.format("rgba(%d,%d,%d,%.2f);",(int)(255 * backgroundColor.getRed()),(int)(255 * backgroundColor.getBlue()), (int)(255 * backgroundColor.getGreen()), backgroundColor.getOpacity());
            tooltip.setStyle("-fx-background-color: " + rgba);
            left.setOnMouseClicked(event -> {
                target.setEffect(null);
                tooltip.hide();
                prev();
            });
            right.setOnMouseClicked(event -> {
                target.setEffect(null);
                tooltip.hide();
                next();
            });
            tooltip.setGraphic(vBox);
        }

        void show(){
            if(mCurrentStep == 0){
                left.setVisible(false);
            }else if(mCurrentStep == steps.size() - 1){
                FontAwesomeIconView view = new FontAwesomeIconView(FontAwesomeIcon.CHECK);
                view.setSize("16");
                view.setFill(Color.WHITE);
                right.setGraphic(view);
            }
            tooltip.show(target,0,0);
            final Scene scene = target.getScene();
            final Point2D windowCoord = new Point2D(scene.getWindow().getX(), scene.getWindow().getY());
            final Point2D sceneCoord = new Point2D(scene.getX(), scene.getY());
            final Point2D nodeCoord = target.localToScene(0.0, 0.0);
            double dx = windowCoord.getX() + sceneCoord.getX() + nodeCoord.getX();
            double dy = windowCoord.getY() + sceneCoord.getY() + nodeCoord.getY();
            tooltip.hide();
            int offset = 5;
            double tooltipWidth = tooltip.getWidth();
            double tooltipHeight = tooltip.getHeight();
            double targetHeight = target.getLayoutBounds().getHeight();
            double targetWidth = target.getLayoutBounds().getWidth();
            if(position == Position.BOTTOM_LEFT){
                dx -= tooltipWidth + offset;
            }else if(position == Position.BOTTOM_RIGHT){
                dx += targetWidth + offset;
            }
            else if(position == Position.TOP){
                dx += targetWidth / 2 - tooltipWidth/2;
                dy -= tooltipHeight + offset;
            }else if(position == Position.BOTTOM){
                dx += targetWidth / 2 - tooltipWidth/2;
                dy += targetHeight + offset;
            }else if(position == Position.CENTER){
                dy += targetHeight / 2 - tooltipHeight/2;
                dx += targetWidth / 2 - tooltipWidth/2;
            }else if(position == Position.TOP_LEFT){
                dx -= tooltipWidth + offset;
                dy -= tooltipHeight + offset;
                dy += targetHeight;
            }else if(position == Position.TOP_RIGHT){
                dx += targetWidth + offset;
                dy -= tooltipHeight + offset;
                dy += targetHeight;
            }else if(position == Position.LEFT){
                dx -= tooltipWidth + offset;
                dy += targetHeight / 2 - tooltipHeight/2;
            }else if(position == Position.RIGHT){
                dx += targetWidth + offset;
                dy += targetHeight / 2 - tooltipHeight/2;
            }
            tooltip.show(target,dx,dy);
        }
    }
    public interface GuideListener{
        void onStepShowed(int oldIndex, int newIndex);
    }
}
