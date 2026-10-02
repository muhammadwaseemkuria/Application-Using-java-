package org.example;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DrawGraph {

    Canvas canvas;
    NetworkGraph networkGraph;
    AnimationTimer animationTimer;
    double zoomLevel;
    double offsetX;
    double offsetY;

    private final double ZOOM_INTENSITY = 0.1;
    private final double MIN_ZOOM = 0.2;
    private final double MAX_ZOOM = 5.0;

    public DrawGraph(Canvas canvas, NetworkGraph networkGraph){
        this.canvas = canvas;
        this.networkGraph = networkGraph;

        zoomLevel  =1;
        offsetX = 0;
        offsetY = 0;

        interactions();
        animationTimer = new AnimationTimer(){

            @Override
            public void handle(long l) {
                draw();
            }
        };

    }

    public void start(){
        animationTimer.start();
    }
    public void stop(){
        animationTimer.stop();
    }

    public void draw(){
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());

        gc.save();

        gc.setStroke(Color.GRAY);
        gc.setLineWidth(1);


        gc.translate(offsetX, offsetY);
        gc.scale(zoomLevel,zoomLevel);



        for(Vertex vertex: networkGraph.adjacencyList.values()){
            for(Edges edge: vertex.connection){
                Vertex target = networkGraph.adjacencyList.get(edge.getIp());
                if(target != null){
                    gc.strokeLine(vertex.x, vertex.y, target.x, target.y);
                }
            }
        }

        for(Vertex vertex: networkGraph.adjacencyList.values()){
            gc.setFill(Color.BLUE);
            gc.fillOval(vertex.x -15, vertex.y -15, 30,30);

            gc.setFill(Color.BLACK);
            gc.fillText(vertex.getIp(), vertex.x - 20, vertex.y - 20);
        }

        gc.restore();

        nodeRepulsion();
        edgeAttraction();

    }

    private void nodeRepulsion() {
        for (Vertex v1 : networkGraph.adjacencyList.values()) {
            for (Vertex v2 : networkGraph.adjacencyList.values()) {
                if (v1 == v2) continue;

                double dx = v2.x - v1.x;
                double dy = v2.y - v1.y;
                double distance = Math.sqrt(dx * dx + dy * dy);

                if(distance == 0)
                    continue;

                double force = 5/ distance;
                if (distance < 100) { // Repel if too close
                    v1.x -= (dx /distance) *force;
                    v1.y -= (dy/distance) * force;
                }
            }
        }
    }

    private void edgeAttraction() {

        for(Vertex vertex : networkGraph.adjacencyList.values()) {

            for(Edges edge : vertex.connection) {

                Vertex target =
                        networkGraph.adjacencyList.get(edge.getIp());

                if(target == null) continue;

                double dx = target.x - vertex.x;
                double dy = target.y - vertex.y;

                vertex.x += dx * 0.0001;
                vertex.y += dy * 0.0001;
            }
        }
    }


    public void interactions(){

        canvas.setOnScroll(event -> {
            double temp = event.getDeltaY();
            double zoomFactor = 1.05;
            if(temp> 0){
                zoomLevel *= zoomFactor;

            }
            else{
                zoomLevel /= zoomFactor;
            }

            zoomLevel  = Math.max(MIN_ZOOM, Math.min(MAX_ZOOM,zoomLevel));

            event.consume();


        });

        double[] mouseX = new double[1];
        double[] mouseY = new double[1];

        canvas.setOnMousePressed(event->{
            mouseX[0] = event.getX();
            mouseY[0] = event.getY();
        });

        canvas.setOnMouseDragged(event->{
            double x = event.getX() - mouseX[0];
            double y = event.getY() - mouseY[0];

            offsetX += x;
            offsetY += y;

            mouseX[0] = event.getX();
            mouseY[0] = event.getY();

        });


    }




}
