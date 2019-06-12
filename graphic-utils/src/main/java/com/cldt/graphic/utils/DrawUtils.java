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
 * Java ͼƬ����
 */
public class DrawUtils {
 
    private static float jPEGcompression = 0.75f;// ͼƬ��������
 
    /**
     * @Description : ����ά��ͼƬ���������ɵ�һ��ͼƬ��
     * @Param : originalImg ԭͼ
     * @Param : qrCodeImg ��ά���ַ
     * @Param : shareDesc ͼƬ����
     * @return : java.lang.String
     * @Author : houzhenghai
     * @Date : 2018/8/15
     */
    public static String generateImg(String originalImg, String qrCodeImg, String shareDesc) throws Exception {
        // ����ԭͼͼƬ
        BufferedImage imageLocal = ImageIO.read(new FileInputStream(originalImg)); //ImageIO.read(new URL(originalImg));
        // �����û��Ķ�ά��
        BufferedImage imageCode = ImageIO.read(new FileInputStream(qrCodeImg)); //ImageIO.read(new URL(qrCodeImg));
        // ��ԭͼƬΪģ��
        Graphics2D g = imageLocal.createGraphics();
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        g.setComposite(ac);
        g.setBackground(Color.WHITE);
        // ��ģ��������û���ά��(��ַ,��߾�,�ϱ߾�,ͼƬ���,ͼƬ�߶�,δ֪)
        g.drawImage(imageCode, 100, imageLocal.getHeight() - 190, 160, 158, null);
        // �����ı���ʽ
        g.setFont(new Font("΢���ź�", Font.PLAIN, 40));
        g.setColor(Color.red);
        // �������ֳ��ȣ�������е�x������
        g.drawString(shareDesc, imageLocal.getWidth() - 330, imageLocal.getHeight() - 530);
 
        // �����ı���ʽ
        g.setFont(new Font("΢���ź�", Font.PLAIN + Font.BOLD, 16));
        g.setColor(Color.WHITE);
        // �������ֳ��ȣ�������е�x������
        String caewm = "������ά��";
        g.drawString(caewm, 105, imageLocal.getHeight() - 10);
 
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        saveAsJPEG(imageLocal, out);
//        out.close();
        //������ͼƬ
        ImageIO.write(imageLocal, "JPG",new FileOutputStream("D:/test/terget/2.png"));
        return null;
    }
 
    /**
     * ��JPEG���뱣��ͼƬ
     * 
     * @param image_to_save
     *            Ҫ�����ͼ��ͼƬ
     * @param fos
     *            �ļ������
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
        System.out.println("��ʼ��" + starttime);
        try {
            String originalImg = "http://xxxxxx.com/images/original.jpg";
            String qrCodeImg = "http://xxxxxx.com/images/qrcode.jpg";
            String shareDesc = "ԭ��9999.99Ԫ";
            String img = generateImg(originalImg, qrCodeImg, shareDesc);
            System.out.println("�������,url=" + img);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("������" + (System.currentTimeMillis() - starttime) / 1000);
    }
 
}
