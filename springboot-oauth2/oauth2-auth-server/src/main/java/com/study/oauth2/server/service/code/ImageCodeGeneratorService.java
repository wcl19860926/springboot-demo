package com.study.oauth2.server.service.code;

import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class ImageCodeGeneratorService {

	public ImageCode generate() {
		int width = 60;
		int height = 20;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Graphics g = image.getGraphics();

		Random random = new Random();

		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);
		g.setFont(new Font("Times New Roman", Font.ITALIC, 20));
		g.setColor(getRandColor(160, 200));
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}

		String sRand = "";
		for (int i = 0; i < 4; i++) {
			String rand = String.valueOf(random.nextInt(10));
			sRand += rand;
			g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
			g.drawString(rand, 13 * i + 6, 16);
		}

		g.dispose();

		return new ImageCode(image, sRand, 60);
	}

	private Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255) {
			fc = 255;
		}
		if (bc > 255) {
			bc = 255;
		}
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}


	static public class ImageCode {

		private BufferedImage image;
		private String code;
		private LocalDateTime expireTime;

		public ImageCode(BufferedImage image, String code, int expireIn){
			this.image = image;
			this.code = code;
			this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
		}

		public ImageCode(BufferedImage image, String code, LocalDateTime expireTime){
			this.image = image;
			this.code = code;
			this.expireTime = expireTime;
		}

		public BufferedImage getImage() {
			return image;
		}

		public void setImage(BufferedImage image) {
			this.image = image;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public LocalDateTime getExpireTime() {
			return expireTime;
		}

		public void setExpireTime(LocalDateTime expireTime) {
			this.expireTime = expireTime;
		}

	}
}
