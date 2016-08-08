package com.lesports.stadium.utils.imageLoader;


import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * @ClassName:  FlushedInputStream   
 * @Description:解决 decoder->decode returned false 问题  
 * @author: 王新年 
 * @date:   2015-12-28 下午5:26:53   
 *
 */
 public class FlushedInputStream extends FilterInputStream {
    public FlushedInputStream(InputStream inputStream) {
        super(inputStream);
    }

    @Override
    public long skip(long n) throws IOException {
        long totalBytesSkipped = 0L;
        while (totalBytesSkipped < n) {
            long bytesSkipped = in.skip(n - totalBytesSkipped);
            if (bytesSkipped == 0L) {
                  int mByte = read();
                  if (mByte < 0) {
                      break;  // we reached EOF
                  } else {
                      bytesSkipped = 1; // we read one byte
                  }
           }
            totalBytesSkipped += bytesSkipped;
        }
        return totalBytesSkipped;
    }
}