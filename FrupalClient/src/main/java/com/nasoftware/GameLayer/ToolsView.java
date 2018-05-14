package com.nasoftware.GameLayer;

import com.nasoftware.LogicLayer.MovementService;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class ToolsView extends JPanel {
    void render(LinkedList<String> tools) {
        this.removeAll();
        JLabel titleLabel = new JLabel();
        titleLabel.setText("Your tools:");
        titleLabel.setBounds(20, 0, 150, 30);
        titleLabel.setHorizontalAlignment(JLabel.LEFT);
        titleLabel.setForeground(Color.BLUE);
        this.add(titleLabel);

        int i=0;
        for(String x:tools) {
            JLabel jLabel = new JLabel();
            jLabel.setText(x);
            jLabel.setBounds(50,30 + 30*i, 150, 30);
            jLabel.setHorizontalAlignment(JLabel.LEFT);
            this.add(jLabel);
            try {
                BufferedImage image = this.getImage(x);
                JLabel imageLabel = new JLabel(new ImageIcon(image));
                imageLabel.setBounds(20, 30 + 30*i + 5, 20, 20);
                this.add(imageLabel);
            } catch (IOException e) {
                e.printStackTrace();
            }
            JButton useButton = new JButton();
            useButton.setBounds(150, 30 + 30*i, 30, 30);
            useButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    MovementService.getMovementService().userTool(jLabel.getText());
                    GameViewController.recoverListener();
                }
            });
            this.add(useButton);
            ++i;
        }
        this.repaint();
    }

    private BufferedImage getImage(String name) throws IOException {
        switch (name) {
            case "Binoculars": return ImageIO.read(new File("Resources/binoculars.png"));
            case "Boat": return ImageIO.read(new File("Resources/boat.png"));
            case "Rock": return ImageIO.read(new File("Resources/rock.png"));
            case "Jack Hammer": return ImageIO.read(new File("Resources/jackHammer.png"));
            case "Hammer and Chisel": return ImageIO.read(new File("Resources/hammerAndChisel.png"));
            case "Chain Saw": return ImageIO.read(new File("Resources/chainSaw.png"));
            case "Axe": return ImageIO.read(new File("Resources/axe.png"));
            case "Shears": return ImageIO.read(new File("Resources/shears.png"));
            case "Pruning Saw": return ImageIO.read(new File("Resources/pruningSaw.png"));
        }
        return null;
    }
}
