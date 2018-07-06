package com.sx;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


import javax.imageio.ImageIO;

import com.swetake.util.Qrcode;

public class CreateQRcode {
	public static void main(String[] args) throws Exception{
		int version=9;  //版本   //公式 v=(4*(n-1）+21）*方格长度
		int imagesize = 163;
		
		
		Qrcode s=new Qrcode(); 
	    s.setQrcodeErrorCorrect('H');
	    s.setQrcodeEncodeMode('B');
	    s.setQrcodeVersion(version);
	    String content="http://www.dijiaxueshe.com"; 
	    byte[] data=content.getBytes("utf-8");  //汉字转格式需要抛出异常
	    boolean[][] qrdata=s.calQrcode(data);
	    //设置缓冲区
	    BufferedImage bufferedImage=new BufferedImage(imagesize, imagesize, BufferedImage.TYPE_INT_RGB);
	    
	    //图片绘制 
	    Graphics2D gs=bufferedImage.createGraphics();
	    
	    //二维码绘画
	    gs.setBackground(Color.WHITE);
	   
	   //清除画布
	   gs.clearRect(0,0, imagesize, imagesize);
	   
	   int startR=255, startG=0,startB=255;
	   int endR=0, endG=0,endB=255;
	   
	   int pixoff=2;
	    
	    for(int i=0;i<qrdata.length;i++){
	    	for(int j=0;j<qrdata.length;j++){
	    	if(qrdata[i][j]){
	    		int a=startR+(endR-startR)*(i+j)/2/qrdata.length;
	    		int b=startG+(endG-startG)*(i+j)/2/qrdata.length;
	    		int c=startB+(endB-startB)*(i+j)/2/qrdata.length;
	    		Color color=new Color(a,b,c);
	    		gs.setColor(color);
	    		
	    		gs.fillRect(i*3+pixoff, j*3+pixoff,3, 3);//后两位代表方格长度
	    		
	    	}
	      }
	    }
	    BufferedImage logo=scale("D:/1.jpg",60,60,true);

	    int o=(imagesize-logo.getHeight())/2;
	    gs.drawImage(logo,o,o,60,60,null);
	    
	    
	    gs.dispose();
	    bufferedImage.flush();
	    try{
	    	ImageIO.write(bufferedImage,"png",new File("D:/qrcode.png"));
	    }catch(IOException e){
	    	e.printStackTrace();
	    	System.out.println("有问题");
	    }
	    System.out.println("成功生成二维码");
	    }
	private static BufferedImage scale(String logoPath, int width, int height, boolean hasFiller) throws IOException {
		// TODO Auto-generated method stub
		
		double ratio=0.0;
		File file = new File(logoPath);
		BufferedImage srcImage =ImageIO.read(file);
		Image destImage = srcImage.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
		
		if((srcImage.getHeight()>height)||srcImage.getWidth()>width){
			if(srcImage.getHeight()>srcImage.getWidth()){
				ratio = (new Integer(height)).doubleValue()/srcImage.getHeight();
			}else{
				ratio = (new Integer(width)).doubleValue()/srcImage.getWidth();
			}
			AffineTransformOp op =new AffineTransformOp(AffineTransform.getScaleInstance(ratio,ratio),null);
			destImage = op.filter(srcImage, null);
		}
		 
		if(hasFiller){
			BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
			Graphics2D graphic = image.createGraphics();
			graphic.setColor(Color.white);
			graphic.fillRect(0, 0, width, height);
			if(width== destImage.getWidth(null)){
				graphic.drawImage(destImage, 0, (height-destImage.getHeight(null))/2, destImage.getWidth(null),
						destImage.getHeight(null),Color.white,null);			
			}else{
				graphic.drawImage(destImage,(width-destImage.getWidth(null))/2, 0,destImage.getWidth(null),
						destImage.getHeight(null),Color.white,null);			
			}
			graphic.dispose();
			destImage=image;
		}
		return (BufferedImage) destImage;
	}

}