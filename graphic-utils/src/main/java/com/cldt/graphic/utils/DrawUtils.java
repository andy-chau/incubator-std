package com.cldt.graphic.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
 
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
 
//import net.sf.json.JSONObject;
// 
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.mime.MultipartEntity;
//import org.apache.http.entity.mime.content.ContentBody;
//import org.apache.http.entity.mime.content.InputStreamBody;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.util.EntityUtils;
 
/**
 * Java 图片生成
 */
public class DrawUtils {
 
    private static float jPEGcompression = 0.75f;// 图片清晰比率
 
    /**
     * @Description : 将二维码图片和文字生成到一张图片上
     * @Param : originalImg 原图
     * @Param : qrCodeImg 二维码地址
     * @Param : shareDesc 图片文字
     * @return : java.lang.String
     * @Author : houzhenghai
     * @Date : 2018/8/15
     */
    public static String generateImg(String originalImg, String qrCodeImg, String shareDesc) throws Exception {
        // 加载原图图片
        BufferedImage imageLocal = ImageIO.read(new FileInputStream(originalImg)); //ImageIO.read(new URL(originalImg));
        // 加载用户的二维码
        BufferedImage imageCode = ImageIO.read(new FileInputStream(qrCodeImg)); //ImageIO.read(new URL(qrCodeImg));
        // 以原图片为模板
        Graphics2D g = imageLocal.createGraphics();
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        g.setComposite(ac);
        g.setBackground(Color.WHITE);
        // 在模板上添加用户二维码(地址,左边距,上边距,图片宽度,图片高度,未知)
        g.drawImage(imageCode, 100, imageLocal.getHeight() - 190, 160, 158, null);
        // 设置文本样式
        g.setFont(new Font("微软雅黑", Font.PLAIN, 40));
        g.setColor(Color.red);
        // 计算文字长度，计算居中的x点坐标
        g.drawString(shareDesc, imageLocal.getWidth() - 330, imageLocal.getHeight() - 530);
 
        // 设置文本样式
        g.setFont(new Font("微软雅黑", Font.PLAIN + Font.BOLD, 16));
        g.setColor(Color.WHITE);
        // 计算文字长度，计算居中的x点坐标
        String caewm = "长按二维码";
        g.drawString(caewm, 105, imageLocal.getHeight() - 10);
 
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        saveAsJPEG(imageLocal, out);
//        out.close();
        //保存新图片
        ImageIO.write(imageLocal, "JPG",new FileOutputStream("D:/test/terget/2.png"));
        return null;
    }
 
    /**
     * 以JPEG编码保存图片
     * 
     * @param image_to_save
     *            要处理的图像图片
     * @param fos
     *            文件输出流
     * @throws IOException
     */
    private static void saveAsJPEG(BufferedImage imageToSave, ByteArrayOutputStream fos) throws IOException {
        ImageWriter imageWriter = ImageIO.getImageWritersBySuffix("jpg").next();
        ImageOutputStream ios = ImageIO.createImageOutputStream(fos);
        imageWriter.setOutput(ios);
        if (jPEGcompression >= 0 && jPEGcompression <= 1f) {
            // new Compression
            JPEGImageWriteParam jpegParams = (JPEGImageWriteParam) imageWriter.getDefaultWriteParam();
            jpegParams.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
            jpegParams.setCompressionQuality(jPEGcompression);
 
        }
        // new Write and clean up
        ImageIO.setUseCache(false);
        imageWriter.write(new IIOImage(imageToSave, null, null));
        ios.close();
        imageWriter.dispose();
    }
 
    /**
     * test
     * 
     * @param args
     * @throws
     */
    public static void main(String[] args) {
        long starttime = System.currentTimeMillis();
        System.out.println("开始：" + starttime);
        try {
            String originalImg = "http://xxxxxx.com/images/original.jpg";
            String qrCodeImg = "http://xxxxxx.com/images/qrcode.jpg";
            String shareDesc = "原价9999.99元";
            String img = generateImg(originalImg, qrCodeImg, shareDesc);
            System.out.println("生成完毕,url=" + img);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("结束：" + (System.currentTimeMillis() - starttime) / 1000);
    }
 
}
