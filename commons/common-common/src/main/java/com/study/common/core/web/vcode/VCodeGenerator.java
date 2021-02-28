package com.study.common.core.web.vcode;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Random;


public class VCodeGenerator {

//	public static final int WIDTH = 100;// 生成的图片的宽度
//	public static final int HEIGHT = 45;// 生成的图片的高度

    private static final String VCODE_KEY = "checkcode";
    public static final String TEXT_BASE64 = "text/base64";
    public static final String UTF_8 = "UTF-8";
    public static final String JPG = "jpg";
    /**
     * 产生随机数的种子
     */
    public static final int RANDOM_SEED = 80;

    private static final Logger logger = LoggerFactory.getLogger(VCodeGenerator.class);

    /**
     * 将两张图片进行合成
     *
     * @param istreamBg   大背景图片
     * @param istreamLogo 阴影小图标
     * @param x,y         小图片嵌入大背景的左上角坐标
     */
    public static BufferedImage mergePic(InputStream istreamBg, InputStream istreamLogo, int x, int y) {
        try {
            Image bgSrc = javax.imageio.ImageIO.read(istreamBg);
            Image logoSrc = javax.imageio.ImageIO.read(istreamLogo);
            int bgWidth = bgSrc.getWidth(null);
            int bgHeight = bgSrc.getHeight(null);
            int logoWidth = logoSrc.getWidth(null);
            int logoHeight = logoSrc.getHeight(null);
            BufferedImage tag = new BufferedImage(bgWidth, bgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = tag.createGraphics();
            g2d.drawImage(bgSrc, 0, 0, bgWidth, bgHeight, null);
            // 透明度设置开始
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
            g2d.drawImage(logoSrc, x * bgWidth / 100, y, logoWidth, logoHeight, null);
            // 透明度设置
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            return tag;
        } catch (Exception e) {
            logger.error("", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 产生随机数，范围在0到100之间，即背影logo的x坐标百分比
     */
    public static int genRandomNum() {
        Random random = new Random();
        int res = random.nextInt(RANDOM_SEED) + 5;
        return res;
    }


    private static final int CHECK_CODE_OFFSET = 2;


}
