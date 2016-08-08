package com.lesports.stadium.cache;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CachedInputStream extends FilterInputStream {
	private FileOutputStream fos;
	
	/**
	 * 对输入流进行缓存处理
	 * @param is 源输入流
	 * @param cachedFile 目标缓存文件
	 * @throws FileNotFoundException 抛出异常：当文件对象不能创建时
	 */
	public CachedInputStream(InputStream is, File cachedFile) throws FileNotFoundException {
		super(is);
		if (cachedFile.exists()) {
			cachedFile.delete();
		}
		fos = new FileOutputStream(cachedFile);
	}
	
	@Override
	public int read(byte[] bytes, int byteOffset, int byteCount) throws IOException {
		int count = super.read(bytes, byteOffset, byteCount);
		if(count!=-1){
			fos.write(bytes, byteOffset, count);
		} else {
			fos.flush();
		}
		return count;
	}

	@Override
	public int read() throws IOException {
		int count = super.read();
		if(count!=-1){
			fos.write(count);
		} else {
			fos.flush();
		}
		return count;
	}
	
	@Override
	public void close() throws IOException {
		super.close();
		if(fos!=null){
			fos.close();
		}
	}
}
