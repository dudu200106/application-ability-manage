package com.dsj.csp.manage.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * @author Du Shun Chang
 * @version 1.0
 * @date 2024/1/29 14:56
 * @Todo:
 */
public class ImgError {
    public static boolean isImageCorrupted(String imagePath) {
        try {
//            File imageFile = new File(imagePath);
            URL url = new URL(imagePath);
            BufferedImage bufferedImage = ImageIO.read(url);
            // 如果能够成功读取图像，且图像的宽度和高度大于0，则认为图像未损坏
            return bufferedImage == null || bufferedImage.getWidth() <= 0 || bufferedImage.getHeight() <= 0;
        } catch (IOException e) {
            // 发生异常表示图像文件损坏
            return true;
        }
    }

    public static void main(String[] args) {
        isImageCorrupted("http://106.227.92.123:9000/manager/20240129/5110626f-5ddc-4ea0-9acd-c7f3d2ca02ad.png");
    }
}
