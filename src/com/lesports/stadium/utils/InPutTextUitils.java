package com.lesports.stadium.utils;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.Toast;

public class InPutTextUitils implements InputFilter{

	private int mMax=15;
	private Toast mErrorToast;
	private Context mContext;
	public InPutTextUitils(int max,Context context) {
		this.mContext=context;
        mMax = max;
    }
	@Override
	public CharSequence filter(CharSequence source, int start, int end,
			Spanned dest, int dstart, int dend) {
		// TODO Auto-generated method stub
            int keep = mMax - (dest.length() - (dend - dstart));
            if (keep ==( end - start)) {
                return null; // keep original
            } else {
                keep += start;
                if (mErrorToast == null) {
                    mErrorToast = Toast.makeText(mContext,"字数已超出限制",
                            Toast.LENGTH_SHORT);
                }
                mErrorToast.show();
                if (Character.isHighSurrogate(source.charAt(keep - 1))) {
                    --keep;
                    if (keep == start) {
                        return "";
                    }
                }
                return source.subSequence(start, keep);
            }
	}

}
