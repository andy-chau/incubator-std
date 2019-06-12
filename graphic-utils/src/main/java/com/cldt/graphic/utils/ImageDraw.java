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
     * 其中是生成推广的图片的
     */
    public static void draw(String imagePath,String path,String content){
        //读取图片文件，得到BufferedImage对象
        BufferedImage bimg;
        try {
            bimg = ImageIO.read(new FileInputStream(imagePath));
        
        //得到Graphics2D 对象
        Graphics2D g2d=(Graphics2D)bimg.getGraphics();
        //设置颜色和画笔粗细
        g2d.setColor(Color.BLUE);
        g2d.setStroke(new BasicStroke(3));
        g2d.setFont(new Font("TimesRoman", Font.BOLD, 80));
        
        //绘制图案或文字
        g2d.drawString(content, 150, 468);
        //保存新图片
        ImageIO.write(bimg, "JPG",new FileOutputStream(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}