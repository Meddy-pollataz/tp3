package fr.univ_amu.iut.exercice7;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import static javafx.beans.binding.Bindings.when;

public class Ball {

    private final DoubleProperty positionX;
    private final DoubleProperty positionY;
    private final DoubleProperty velocityX; //en pixel par nanosecond
    private final DoubleProperty velocityY; //en pixel par nanosecond
    private final DoubleProperty radius;
    private final Pane parent;
    private Circle ball = new Circle();


    private BooleanExpression isBouncingOffVerticalWall;
    private BooleanExpression isBouncingOffHorizontalWall;

    private NumberBinding bounceOffVerticalWall;
    private NumberBinding bounceOffHorizontalWall;

    public Ball(Pane parent) {
        positionX = new SimpleDoubleProperty(20);
        positionY = new SimpleDoubleProperty(40);
        velocityX = new SimpleDoubleProperty(150E-9);
        velocityY = new SimpleDoubleProperty(100E-9);
        radius = new SimpleDoubleProperty(20);
        this.parent=parent;
        parent.getChildren().addAll(ball);
        createBindings();

    }

    private void createBindings() {
        ball.radiusProperty().bind(radius);
        ball.centerXProperty().bind(positionX);
        ball.centerYProperty().bind(positionY);

        isBouncingOffVerticalWall = positionX.lessThan(radius).or(positionX.greaterThan(parent.widthProperty().subtract(radius)));
        isBouncingOffHorizontalWall = positionY.lessThan(radius).or(positionY.greaterThan(parent.heightProperty().subtract(radius)));

        bounceOffVerticalWall = Bindings.when(isBouncingOffVerticalWall).then(velocityX.negate()).otherwise(velocityX);
        bounceOffHorizontalWall = Bindings.when(isBouncingOffHorizontalWall).then(velocityY.negate()).otherwise(velocityY);
    }




    public void move(long elapsedTimeInNanoseconds) {
        velocityY.set(bounceOffHorizontalWall.doubleValue());
        velocityX.set(bounceOffVerticalWall.doubleValue());

        positionX.set(positionX.get() + velocityX.get()*elapsedTimeInNanoseconds);
        positionY.set(positionY.get() + velocityY.get()*elapsedTimeInNanoseconds);
    }
    }
