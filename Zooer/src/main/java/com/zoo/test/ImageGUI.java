package com.zoo.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JDialog;

public class ImageGUI extends JComponent {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final Dimension SIZE=new Dimension(320, 240);
    private Dimension size=SIZE;
    private BufferedImage image;
    private JDialog dialog;
    private boolean autoSize=true;

    public ImageGUI() {

    }

    public static ImageGUI of() {
    	return new ImageGUI();
    }
    
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        if(image == null)  {
            g2d.setPaint(Color.BLACK);
            g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
        } else {
            g2d.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
        }
    }

    public ImageGUI createWin(String title) {
    	return createWin(title, SIZE);
    }

    public ImageGUI createWin(String title, Dimension size) {
    	dialog = new JDialog();
    	dialog.setTitle(title);
    	dialog.getContentPane().setLayout(new BorderLayout());
    	dialog.getContentPane().add(this, BorderLayout.CENTER);
    	dialog.setLocationRelativeTo(null);
    	dialog.setSize(size==null?SIZE:size);
    	dialog.setVisible(true);
    	dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        return this;
    }

    public ImageGUI imshow(BufferedImage image) {
        this.image = image;
        if (autoSize && image!=null) {
        	size=new Dimension(Math.max(image.getWidth(),SIZE.width), Math.max(image.getHeight(),SIZE.height));
        	dialog.setSize(size);
		}
        if (image!=null) {
        	this.repaint();
		}
        return this;
    }

    public JDialog getDialog() {
    	return this.dialog;
    }
    
    public ImageGUI autoSize() {
    	this.autoSize=true;
    	return this;
    }
    
    public ImageGUI fixSize() {
    	this.autoSize=false;
    	return this;
    }
    
    public ImageGUI fixSize(Dimension size) {
    	this.autoSize=false;
    	if (size!=null) {
			this.size=size;
		}
    	return this;
    }
    
    
    
}
