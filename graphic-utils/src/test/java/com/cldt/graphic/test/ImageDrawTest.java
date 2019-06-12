package com.cldt.graphic.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import com.cldt.graphic.utils.QRCodeUtil;

public class ImageDrawTest {

    public static void main(String[] args) {
//        ImageDraw.draw("D:/test/input.png", "D:/test/terget/1.png", "BGSas258");

        try {
            // ���ɶ�ά��
            BufferedImage qRcodeImg = QRCodeUtil.createQRCode("http://b.lakala.com/cl_wx/newloan_uat/index.html?merAccCode=YDSJD1-YFQ-25E2A1D9B733455A8BE");
            // ����ά�������ҳ����
            ImageIO.write(qRcodeImg, "png", new File("D:/test/terget/qrcode1.png"));
            
            System.out.println(QRCodeUtil.decode("D:/test/terget/qrcode1.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
