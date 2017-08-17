package ru.geekbrains.catch_the_ball;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class GameWindow extends JFrame {

    private static GameWindow game_window;
    private static long last_frame_time;
    private static Image background;
    private static Image game_over;
    private static Image ball;
    private static float ball_left = 200;
    private static float ball_top = -100;
    public static float ball_v = 200;
    private static int score;

    public static void main(String[] args) throws IOException {

        background = ImageIO.read(GameWindow.class.getResourceAsStream("background.jpg"));
        game_over = ImageIO.read(GameWindow.class.getResourceAsStream("game_over.png"));
        ball = ImageIO.read(GameWindow.class.getResourceAsStream("red_ball.png"));

        game_window = new GameWindow();
        game_window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game_window.setLocation(200, 100);
        game_window.setSize(906, 478);
        game_window.setResizable(false);
        last_frame_time = System.nanoTime();
        GameField game_field = new GameField();
        game_field.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                float ball_right = ball_left + ball.getWidth(null);
                float ball_bottom = ball_top + ball.getHeight(null);
                boolean is_ball =  x >= ball_left && x <= ball_right && y >= ball_top && y <= ball_bottom;
                if (is_ball) {
                    ball_top = -100;
                    ball_left = (int) (Math.random() * (game_field.getWidth() - ball.getWidth(null)));
                    ball_v = ball_v + 20;
                    score++;
                    game_window.setTitle("Ваш счёт: " + score);
                }
            }
        });
        game_window.add(game_field);
        game_window.setVisible(true);
    }

    private static void onRepaint(Graphics g) {

        long current_time = System.nanoTime();
        float delta_time = (current_time - last_frame_time) * 0.000000001f;
        last_frame_time = current_time;

        ball_top = ball_top + ball_v * delta_time;
        g.drawImage(background, 0 , 0, null);
        g.drawImage(ball, (int) ball_left, (int) ball_top, null);
        if (ball_top > game_window.getHeight()) g.drawImage(game_over, 320,160,null);
    }

    private static class GameField extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            onRepaint(g);
            repaint();
        }
    }
}
