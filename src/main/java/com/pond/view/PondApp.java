package com.pond.view;

import com.pond.model.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PondApp extends Application {
    private TPond pond = new TPond();
    private Canvas canvas;
    private GraphicsContext gc;
    private Label statusLabel;
    private boolean simulationRunning = false;

    @Override
    public void start(Stage stage) {
        initPond();

        BorderPane root = new BorderPane();
        canvas = new Canvas(800, 600);
        gc = canvas.getGraphicsContext2D();
        root.setCenter(canvas);

        HBox controls = new HBox(10);
        Button startBtn = new Button("▶ Старт");
        Button stopBtn = new Button("⏹ Стоп");
        Button stepBtn = new Button("⏭ Шаг");
        Button resetBtn = new Button("🔄 Сброс"); // НОВОЕ
        statusLabel = new Label("Готов");

        startBtn.setOnAction(e -> simulationRunning = true);
        stopBtn.setOnAction(e -> simulationRunning = false);
        stepBtn.setOnAction(e -> stepSimulation());
        resetBtn.setOnAction(e -> resetPond()); // НОВОЕ

        controls.getChildren().addAll(startBtn, stopBtn, stepBtn, resetBtn, statusLabel);
        root.setBottom(controls);

        Scene scene = new Scene(root, 900, 700);
        stage.setTitle("🦈 Экосистема водоема | ВГТУ 2026 | Лыков Е.И.");
        stage.setScene(scene);
        stage.show();

        // ✅ ПЛАВНАЯ АНИМАЦИЯ 30 FPS
        new AnimationTimer() {
            long lastUpdate = 0;
            @Override
            public void handle(long now) {
                render();
                updateStatus();

                // ✅ ОБНОВЛЕНИЕ КАЖДЫЕ 33мс (30 FPS)
                if (simulationRunning && now - lastUpdate > 33_000_000) {
                    pond.update();
                    lastUpdate = now;
                }
            }
        }.start();
    }

    private void initPond() {
        pond = new TPond(); // НОВОЕ: пересоздание

        // Растения (8 шт)
        pond.addPlant(100, 100); pond.addPlant(300, 150); pond.addPlant(500, 200);
        pond.addPlant(700, 250); pond.addPlant(150, 350); pond.addPlant(450, 400);
        pond.addPlant(650, 450); pond.addPlant(200, 500);

        // Мальки (5 шт)
        pond.addLivingEntity(new THerbivore(180, 180));
        pond.addLivingEntity(new THerbivore(380, 280));
        pond.addLivingEntity(new THerbivore(580, 180));
        pond.addLivingEntity(new THerbivore(280, 380));
        pond.addLivingEntity(new THerbivore(480, 480));

        // Щуки (3 шт)
        pond.addLivingEntity(new TPredator(420, 120, pond));
        pond.addLivingEntity(new TPredator(620, 320, pond));
        pond.addLivingEntity(new TPredator(220, 420, pond));
    }

    private void resetPond() {
        simulationRunning = false;
        initPond();
    }

    private void render() {
        gc.setFill(Color.hsb(195, 0.4, 0.95));
        gc.fillRect(0, 0, 800, 600);

        for (TPlant plant : pond.getPlants()) {
            if (plant.isAlive()) {
                gc.setFill(plant.getHealth() > 70 ? Color.FORESTGREEN : Color.LIME);
                gc.fillOval(plant.getX()-12, plant.getY()-12, 24, 24);
                gc.setFill(Color.WHITE);
                gc.setFont(javafx.scene.text.Font.font(10));
                gc.fillText((plant.getHealth()/10)+"", plant.getX()-8, plant.getY()+3);
            }
        }

        for (TLivingEntity entity : pond.getLivingEntities()) {
            if (entity instanceof THerbivore herb && herb.isAlive()) {
                gc.setFill(herb.getHealth() > 50 ? Color.ORANGE : Color.YELLOW);
                gc.fillOval(herb.getX()-10, herb.getY()-10, 20, 20);
                gc.setFill(Color.BLACK);
                gc.setFont(javafx.scene.text.Font.font(12));
                gc.fillText("🐟", herb.getX()-8, herb.getY()+6);
            }
        }

        for (TLivingEntity entity : pond.getLivingEntities()) {
            if (entity instanceof TPredator pred && pred.isAlive()) {
                gc.setFill(Color.CRIMSON);
                // Ромб
                double[] xPoints = {pred.getX(), pred.getX()+15, pred.getX(), pred.getX()-15};
                double[] yPoints = {pred.getY()-15, pred.getY(), pred.getY()+15, pred.getY()};
                gc.fillPolygon(xPoints, yPoints, 4);
                gc.setFill(Color.WHITE);
                gc.setFont(javafx.scene.text.Font.font(14));
                gc.fillText("🦈", pred.getX()-10, pred.getY()+6);
            }
        }
    }

    private void updateStatus() {
        long plantsAlive = pond.getPlants().stream().filter(TLivingEntity::isAlive).count();
        long herbsAlive = pond.getLivingEntities().stream()
                .filter(e -> e instanceof THerbivore).map(e -> (THerbivore)e).filter(TLivingEntity::isAlive).count();
        long predsAlive = pond.getLivingEntities().stream()
                .filter(e -> e instanceof TPredator).map(e -> (TPredator)e).filter(TLivingEntity::isAlive).count();

        statusLabel.setText(String.format("🌿%d | 🐟%d | 🦈%d | ⏱ %d тиков",
                plantsAlive, herbsAlive, predsAlive, pond.getTickCount()));
    }

    private void stepSimulation() {
        pond.update();
        render();
        updateStatus();
    }
}
