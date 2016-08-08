package com.lesports.stadium.view;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import com.lesports.stadium.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class LEDBoardView extends SurfaceView implements SurfaceHolder.Callback, Runnable{
	
	private Thread thread;
	private SurfaceHolder sfholder;
	private Canvas canvas;
	
	private int mwidth, mheight;
	private float PixPerLED;
	private float LEDToEdge;
	private int LEDBoardWidth;
	private int LEDBoardHeight = 16;
	private int HanZiSize = 32;
	
	private LED[][] LEDArray;
	private byte[] matrixfont;
	private String[] strings;
	
	private int row;
	private boolean threadFlag = true;
	private boolean showtimeflag = false;
	
	private boolean moveorientation = true;
	private boolean hanziorientation = true;
	private int speed = 50;
	private int mcolor = Color.RED;
	private boolean LEDshape = false;
	private String mstring = "";
	
	public LEDBoardView(Context context){
		super(context);
		sfholder = this.getHolder();
		sfholder.addCallback(this);
	}
	
	public void setColor(int color){
		mcolor = color;
	}
	
	public void setSpeed(int v){
		speed = v;
	}
	
	public void setMoveOrientation(boolean orientation){
		moveorientation = orientation;
	}
	
	public void setHanziOrientation(boolean orientation){
		hanziorientation = orientation;
	}
	
	public void setString(String s){
		mstring = s;
	}
	
	public void setLEDshape(boolean shape){
		LEDshape = shape;
	}
	
	public void setLEDBoardHeight(int height){
		LEDBoardHeight = height;
		HanZiSize = LEDBoardHeight*LEDBoardHeight/8;
	}
	
	public void stopRunning(){
		threadFlag = false;
	}
	
	public void draw(){
		canvas = sfholder.lockCanvas();
		canvas.drawColor(Color.BLACK);
		for(int i=0; i<LEDBoardWidth; i++){
			for(int j=0; j<LEDBoardHeight; j++){
				if(moveorientation){
					if(hanziorientation){
						LEDArray[i][j].setCoordinate(LEDBoardWidth-1-i, j);
					}else{
						LEDArray[i][j].setCoordinate(LEDBoardWidth-1-i, LEDBoardHeight-1-j);
					}
				}else{
					if(LEDBoardHeight == 24&&!hanziorientation){
						LEDArray[i][j].setCoordinate(i, LEDBoardHeight-1-j);
					}else{
						LEDArray[i][j].setCoordinate(i, j);
					}
				}
				if(LEDArray[i][j].getState()){
					LEDArray[i][j].LEDON();
				}else{
					LEDArray[i][j].LEDOFF();
				}
			}
		}
		row++;
		if((LEDBoardHeight == 16&&row == LEDBoardWidth+matrixfont.length/2)||
				(LEDBoardHeight == 24&&row == LEDBoardWidth+matrixfont.length/3)){
			row=0;
			if(showtimeflag){
				matrixfont = getMatrixFont(strings);
			}
		}
		if(canvas != null)
			sfholder.unlockCanvasAndPost(canvas);
	}
	
	public void move(){
		int rowtemp = row;
		if(rowtemp >= LEDBoardWidth)
			rowtemp = LEDBoardWidth-1;
		while(rowtemp!=0){
			for(int i=0; i<LEDBoardHeight; i++){
				LEDArray[rowtemp][i].setState(LEDArray[rowtemp-1][i].getState());
			}
			rowtemp--;
		}
		int matrixoffset = row*LEDBoardHeight/8;
		for(int i=0; i<LEDBoardHeight; i++){
		    if(matrixoffset < matrixfont.length){
		    	if((matrixfont[matrixoffset+i/8]&(0x80>>(i-i/8*8)))!=0){
		    		LEDArray[0][i].setState(true);
		    	}else{
		    		LEDArray[0][i].setState(false);
		    	}
		    }else{
		    	LEDArray[0][i].setState(false);
		    }
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(threadFlag){
			long framestart = System.currentTimeMillis();
			move();
			draw();
			long frameend = System.currentTimeMillis();
			try {
				if((frameend-framestart)<=speed){
					Thread.sleep(speed-(frameend-framestart));
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		mwidth = width;
		mheight = height;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		mwidth = this.getWidth();
		mheight = this.getHeight();
		if(mheight/mwidth <= 2){
			PixPerLED = 0.5f*mheight/LEDBoardHeight;
		}else{
			PixPerLED = mwidth/LEDBoardHeight;
		}
		LEDToEdge = mheight%(LEDBoardHeight*PixPerLED)/2;
		LEDBoardWidth = (int)((mheight-LEDToEdge*2)/PixPerLED);
		LEDArray = new LED[LEDBoardWidth][LEDBoardHeight];
		
		canvas = sfholder.lockCanvas();
		for(int i=0; i<LEDBoardWidth; i++){
			for(int j=0; j<LEDBoardHeight; j++){
				LEDArray[i][j] = new LED(canvas, PixPerLED, mcolor);
			}
		}
		if(canvas != null)
			sfholder.unlockCanvasAndPost(canvas);
		
		strings = checkTimeTag(mstring);
		if(showtimeflag){
			matrixfont = getMatrixFont(strings);
		}else{
			matrixfont = getMatrixFont(mstring);
		}
		
		thread = new Thread(this);
		thread.start();
	}
	
	public String[] checkTimeTag(String mstring){
		StringBuilder mstringbuilder = new StringBuilder();
		StringBuilder timetagstringbuilder = new StringBuilder();
		if(mstring.contains("#时间#")){
			showtimeflag = true;
			char[] charOfString = mstring.toCharArray();
			int stringcount = 0;
			boolean endwithtimetag = false;
			boolean timetagconjoint = false;
			String checktimetag = null;
			for(int i=0; i<charOfString.length; ){
				if((i+3)<charOfString.length){
					checktimetag = new String(new char[]{charOfString[i], charOfString[i+1], charOfString[i+2], charOfString[i+3]});
				}else{
					checktimetag = new String();
				}
				if(checktimetag.equals("#时间#")){
					i+=4;
					if(i == 4||timetagconjoint){
						stringcount+=1;
					}else{
						stringcount+=2;
					}
					timetagconjoint = true;
					if(i == charOfString.length){
						endwithtimetag = true;
					}
				}else{
					i++;
					timetagconjoint = false;
					if(i == charOfString.length){
						endwithtimetag = false;
					}
				}
			}
			if(!endwithtimetag){
				stringcount+=1;
			}
			String[] splitstring = new String[stringcount];
			int n = 0;
			for(int i=0; i<charOfString.length; ){
				if((i+3)<charOfString.length){
					checktimetag = new String(new char[]{charOfString[i], charOfString[i+1], charOfString[i+2], charOfString[i+3]});
				}else{
					checktimetag = new String();
				}
				if(checktimetag.equals("#时间#")){
					if(!mstringbuilder.toString().equals("")){
						splitstring[n] = mstringbuilder.toString();
						mstringbuilder = new StringBuilder();
						n++;
					}
					splitstring[n] = timetagstringbuilder.append(charOfString, i, 4).toString();
					timetagstringbuilder = new StringBuilder();
					n++;
					i+=4;
				}else{
					mstringbuilder.append(charOfString[i]);
					i++;
				}
			}
			if(!endwithtimetag){
				splitstring[n] = mstringbuilder.toString();
			}
			return splitstring;
		}else{
			showtimeflag = false;
			return null;
		}
	}
	
	public byte[] getMatrixFont(String[] string){
		byte[][] matrixbuffer = new byte[string.length][];
		byte[] matrixbuffertime = getMatrixFont(getTime());
		int length = 0;
		for(int i=0; i<string.length; i++){
			if(!string[i].equals("#时间#")){
				matrixbuffer[i] = getMatrixFont(string[i]);
				length += matrixbuffer[i].length;
			}else{
				matrixbuffer[i] = matrixbuffertime;
				length += matrixbuffer[i].length;
			}
		}
		byte[] matrix = new byte[length];
		int n = 0;
		for(int i=0; i<string.length; i++){
			for(int j=0; j<matrixbuffer[i].length; j++){
				matrix[n] = matrixbuffer[i][j];
				n++;
			}
		}
		return matrix;
	}
	
	public String getTime(){
		String time = null;
		Calendar mcalendar = Calendar.getInstance();
		String year = Integer.valueOf(mcalendar.get(Calendar.YEAR)).toString();
		String month = Integer.valueOf(mcalendar.get(Calendar.MONTH)+1).toString();
		String day = Integer.valueOf(mcalendar.get(Calendar.DAY_OF_MONTH)).toString();
		String hour = Integer.valueOf(mcalendar.get(Calendar.HOUR_OF_DAY)).toString();
		String minute = Integer.valueOf(mcalendar.get(Calendar.MINUTE)).toString();
		String week = null;
		switch(mcalendar.get(Calendar.DAY_OF_WEEK)){
		case 1:
			week = "星期日";
			break;
		case 2:
			week = "星期一";
			break;
		case 3:
			week = "星期二";
			break;
		case 4:
			week = "星期三";
			break;
		case 5:
			week = "星期四";
			break;
		case 6:
			week = "星期五";
			break;
		case 7:
			week = "星期六";
			break;
		}
		time = year+"年"+month+"月"+day+"日 "+hour+"时"+minute+"分 "+week;
		return time;
	}
	
	public int[] countFont(String string){
		int[] count = new int[2];
		byte[] GBKcode = new byte[string.length()*2];
		try {
			GBKcode = string.getBytes("GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int n = 0;
		for(int i=0; i<GBKcode.length; ){
			if(GBKcode[i]<=127&&GBKcode[i]>=0){
				n++;
				i++;
			}else{
				i+=2;
			}
		}
		count[0] = n;
		count[1] = (GBKcode.length-n)/2;
		return count;
	}
	
	public byte[] getMatrixFont(String string){
		byte[] GBKcode = new byte[string.length()*2];
		int[] countfont = new int[2];
		countfont = countFont(string);
		byte[] matrixbuffer = new byte[countfont[1]*HanZiSize+countfont[0]*HanZiSize/2];
		try {
			if(LEDBoardHeight == 16){
				InputStream inhzk = this.getContext().getAssets().open("HZK16");
				InputStream inasc = this.getContext().getAssets().open("ASC16");
				GBKcode = string.getBytes("GBK");
				int count = 0;
				for(int i=0; i<GBKcode.length; ){
					if(GBKcode[i]<=127&&GBKcode[i]>=0){
						byte[] matrixreadbuffer = new byte[HanZiSize/2];
						int offset = (GBKcode[i]&0xFF)*matrixreadbuffer.length;
						inasc.skip(offset);
						inasc.read(matrixreadbuffer, 0, matrixreadbuffer.length);
						inasc.reset();
						if(moveorientation){
							matrixreadbuffer=MatrixTransformRightToLeftASC16(matrixreadbuffer);
						}else{
							matrixreadbuffer=MatrixTransformLeftToRightASC16(matrixreadbuffer);
						}
						for(int j=0; j<matrixreadbuffer.length; j++){
							matrixbuffer[count+j] = matrixreadbuffer[j];
						}
						count+=matrixreadbuffer.length;
						i++;
					}else{
						byte[] matrixreadbuffer = new byte[HanZiSize];
						int offset = (((GBKcode[i]&0xFF)-0xA1)*94+((GBKcode[i+1]&0xFF)-0xA1))*HanZiSize;
						inhzk.skip(offset);
						inhzk.read(matrixreadbuffer, 0, HanZiSize);
						inhzk.reset();
						if(hanziorientation){
							if(moveorientation){
								matrixreadbuffer=MatrixTransformRightToLeftHZK16(matrixreadbuffer);
							}else{
								matrixreadbuffer=MatrixTransformLeftToRightHZK16(matrixreadbuffer);
							}
						}
						for(int j=0; j<matrixreadbuffer.length; j++){
							matrixbuffer[count+j] = matrixreadbuffer[j];
						}
						count+=HanZiSize;
						i+=2;
					}
				}
			}
			if(LEDBoardHeight == 24){
				InputStream inhzk24 = this.getContext().getAssets().open("HZK24");
				InputStream inasc24 = this.getContext().getAssets().open("ASC24");
				GBKcode = string.getBytes("GBK");
				int count = 0;
				for(int i=0; i<GBKcode.length; ){
					if(GBKcode[i]<=127&&GBKcode[i]>=0){
						byte[] matrixreadbuffer = new byte[HanZiSize*2/3];
						int offset = ((GBKcode[i]&0xFF)-0x20)*matrixreadbuffer.length;
						inasc24.skip(offset);
						inasc24.read(matrixreadbuffer, 0, matrixreadbuffer.length);
						inasc24.reset();
						if(moveorientation){
							matrixreadbuffer = MatrixTransformRightToLeftASC24(matrixreadbuffer);
						}else{
							matrixreadbuffer = MatrixTransformLeftToRightASC24(matrixreadbuffer);
						}
						for(int j=0; j<matrixreadbuffer.length; j++){
							matrixbuffer[count+j] = matrixreadbuffer[j];
						}
						count+=matrixreadbuffer.length;
						i++;
					}else{
						byte[] matrixreadbuffer = new byte[HanZiSize];
						int offset = (((GBKcode[i]&0xFF)-0xA1)*94+((GBKcode[i+1]&0xFF)-0xA1))*HanZiSize;
						inhzk24.skip(offset);
						inhzk24.read(matrixreadbuffer, 0, HanZiSize);
						inhzk24.reset();
						if(!moveorientation){
							matrixreadbuffer = MatrixTransformLeftToRightHZK24(matrixreadbuffer);
						}
						if(!hanziorientation){
							matrixreadbuffer = MatrixTransformTurn90DegreeHZK24(matrixreadbuffer);
						}
						for(int j=0; j<matrixreadbuffer.length; j++){
							matrixbuffer[count+j] = matrixreadbuffer[j];
						}
						count+=HanZiSize;
						i+=2;
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return matrixbuffer;
	}
	
	public byte[] MatrixTransformLeftToRightASC24(byte[] matrix){
		byte[] matrixbuffer = new byte[(int)(HanZiSize/2)];
		for(int i=0; i<36; i++){
			for(int j=0; j<8; j++){
				matrixbuffer[i]|=(matrix[(j+i%3*8)*2+1-(i+12)/24]&(0x01<<(i/3+4-(i+12)/24*8)))>>(i/3+4-(i+12)/24*8)<<(7-j);
			}
		}
		if(!hanziorientation){
			for(int i=0; i<matrixbuffer.length; i+=3){
				byte tempa = matrixbuffer[i];
				byte tempb = matrixbuffer[i+1];
				byte tempc = matrixbuffer[i+2];
				matrixbuffer[i] = 0;
				matrixbuffer[i+1] = 0;
				matrixbuffer[i+2] = 0;
				for(int j=0; j<8; j++){
					matrixbuffer[i] |= (tempc&(0x01<<j))>>j<<(7-j);
					matrixbuffer[i+1] |= (tempb&(0x01<<j))>>j<<(7-j);
					matrixbuffer[i+2] |= (tempa&(0x01<<j))>>j<<(7-j);
				}
			}
		}
		return matrixbuffer;
	}
	
	public byte[] MatrixTransformRightToLeftASC24(byte[] matrix){
		byte[] matrixbuffer = new byte[(int)(HanZiSize/2)];
		for(int i=0; i<36; i++){
			for(int j=0; j<8; j++){
				matrixbuffer[i]|=(matrix[(j+i%3*8)*2+i/24]&(0x80>>(i/3-i/24*8)))<<(i/3-i/24*8)>>j;
			}
		}
		if(!hanziorientation){
			for(int i=0; i<matrixbuffer.length; i+=3){
				byte tempa = matrixbuffer[i];
				byte tempb = matrixbuffer[i+1];
				byte tempc = matrixbuffer[i+2];
				matrixbuffer[i] = 0;
				matrixbuffer[i+1] = 0;
				matrixbuffer[i+2] = 0;
				for(int j=0; j<8; j++){
					matrixbuffer[i] |= (tempc&(0x01<<j))>>j<<(7-j);
					matrixbuffer[i+1] |= (tempb&(0x01<<j))>>j<<(7-j);
					matrixbuffer[i+2] |= (tempa&(0x01<<j))>>j<<(7-j);
				}
			}
		}
		return matrixbuffer;
	}
	
	public byte[] MatrixTransformLeftToRightHZK24(byte[] matrix){
		byte[] matrixbuffer = new byte[matrix.length];
		for(int i=0; i<matrix.length; i++){
			matrixbuffer[i] = matrix[matrix.length-((int)(i/3)+1)*3+i%3];
		}
		return matrixbuffer;
	}
	
	public byte[] MatrixTransformTurn90DegreeHZK24(byte[] matrix){
		byte[] matrixbuffer = new byte[matrix.length];
		for(int i=0; i<72; i++){
			for(int j=0; j<8; j++){
				matrixbuffer[i]|=(matrix[(j+i%3*8)*3+(i/24)]&(0x80>>(i/3-i/24*8)))<<(i/3-i/24*8)>>j;
			}
		}
		return matrixbuffer;
	}
	
	public byte[] MatrixTransformRightToLeftASC16(byte[] matrix){
		byte[] matrixbuffer = new byte[matrix.length];
		for(int i=0; i<16; i++){
			for(int j=0; j<8; j++){
				matrixbuffer[i]|=((matrix[j+i%2*8]&(0x80>>(i/2)))<<(i/2)>>j);
			}
		}
		if(!hanziorientation){
			for(int i=0; i<matrixbuffer.length; i+=2){
				byte tempa = matrixbuffer[i+1];
				byte tempb = matrixbuffer[i];
				matrixbuffer[i]=0;
				matrixbuffer[i+1]=0;
				for(int j=0; j<8; j++){
					matrixbuffer[i+1]|=((tempb&(0x80>>j))<<j>>(7-j));
					matrixbuffer[i]|=((tempa&(0x80>>j))<<j>>(7-j));
				}
			}
		}
		return matrixbuffer;
	}
	
	public byte[] MatrixTransformLeftToRightASC16(byte[] matrix){
		byte[] matrixbuffer = new byte[matrix.length];
		for(int i=0; i<16; i++){
			for(int j=0; j<8; j++){
				matrixbuffer[i]|=(matrix[j+i%2*8]&(0x01<<(i/2)))>>(i/2)<<(7-j);
			}
		}
		return matrixbuffer;
	}
	
	public byte[] MatrixTransformRightToLeftHZK16(byte[] matrix){
		byte[] matrixbuffer = new byte[matrix.length];
		for(int i=0; i<32; i++){
			for(int j=0; j<8; j++){
				matrixbuffer[i]|=(matrix[(j+i%2*8)*2+(i/16)]&(0x80>>(i/2-i/16*8)))<<(i/2-i/16*8)>>j;
			}
		}
		return matrixbuffer;
	}
	
	public byte[] MatrixTransformLeftToRightHZK16(byte[] matrix){
		byte[] matrixbuffer = new byte[matrix.length];
		for(int i=0; i<32; i++){
			for(int j=0; j<8; j++){
				matrixbuffer[i]|=(matrix[(j+i%2*8)*2+1-(i/16)]&(0x01<<(i/2-i/16*8)))>>(i/2-i/16*8)<<(7-j);
			}
		}
		return matrixbuffer;
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		thread = null;
		threadFlag = false;
	}
	
	public class LED {
		
		private int LEDX;
		private int LEDY;
		private boolean LEDState = false;
		private int mcolor;
		
		private Paint mpaint = new Paint();
		private Canvas mcanvas;
		private float LEDSize;
		
		public LED(Canvas canvas, float size, int color){
			mcanvas = canvas;
			LEDSize = size;
			mcolor = color;
			mpaint.setColor(mcolor);
			mpaint.setAntiAlias(true);
		}
		
		public LED(Canvas canvas, float size){
			this(canvas, size, Color.RED);
		}
		
		public void setCoordinate(int x, int y){
			LEDX = x;
			LEDY = y;
		}
		
		public void setState(boolean led){
			LEDState = led;
		}
		
		public void setColor(int color){
			mcolor = color;
		}
		
		public boolean getState(){
			return LEDState;
		}
		
		public void drawLED(){
			float cx = (mwidth-LEDBoardHeight*LEDSize)/2+LEDSize/2+(LEDBoardHeight-LEDY-1)*LEDSize;
			float cy = LEDToEdge+LEDSize/2+LEDX*LEDSize;
			if(LEDshape){
				mcanvas.drawCircle(cx, cy, LEDSize/2, mpaint);
			}else{
				if(LEDBoardHeight == 16)
					mcanvas.drawRect(cx-LEDSize/2+1, cy-LEDSize/2+1, cx+LEDSize/2-1, cy+LEDSize/2-1, mpaint);
				if(LEDBoardHeight == 24)
					mcanvas.drawRect(cx-LEDSize/2+0.5f, cy-LEDSize/2+0.5f, cx+LEDSize/2-0.5f, cy+LEDSize/2-0.5f, mpaint);
			}
		}
		
		public void LEDON(){
			mpaint.setColor(mcolor);
			drawLED();
		}
		
		public void LEDOFF(){
			mpaint.setColor(getResources().getColor(R.color.darkgray));
			drawLED();
		}
	}
}