package com.lesports.stadium.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class writeDateToSdCard {
	
	
	public static boolean writeDateTosdcard(String path,String filename,byte[] bytes)
	{
		boolean b = false;
		try {
			File file=new File(path,filename);
			FileOutputStream output=new FileOutputStream(file);
			output.write(bytes);
			output.close();
			b=true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return b;
		
	}

}
