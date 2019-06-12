package com.cldt.graphic.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.imageio.ImageIO;
 
public class ImageDraw {
    /**
     * �����������ƹ��ͼƬ��
     */
    public static void draw(String imagePath,String path,String content){
        //��ȡͼƬ�ļ����õ�BufferedImage����
        BufferedImage bimg;
        try {
            bimg = ImageIO.read(new FileInputStream(imagePath));
        
        //�õ�Graphics2D ����
        Graphics2D g2d=(Graphics2D)bimg.getGraphics();
        //������ɫ�ͻ��ʴ�ϸ
        g2d.setColor(Color.BLUE);
        g2d.setStroke(new BasicStroke(3));
        g2d.setFont(new Font("TimesRoman", Font.BOLD, 80));
        
        //����ͼ��������
        g2d.drawString(content, 150, 468);
        //������ͼƬ
        ImageIO.write(bimg, "JPG",new FileOutputStream(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}