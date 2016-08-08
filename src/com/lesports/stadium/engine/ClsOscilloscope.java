package com.lesports.stadium.engine;

import java.util.ArrayList;
import java.util.Date;

import android.media.AudioRecord;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.TextView;

import com.lesports.stadium.R;
import com.lesports.stadium.activity.RecordActivity;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.view.MyVisualizerView;
import com.lesports.stadium.view.VisualizerView;

/**
 * 
 * ***************************************************************
 * 
 * @ClassName: ClsOscilloscope
 * 
 * @Desc : 根据当前录音绘制音频 写在子线程，以免造成卡机现象
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 王新年
 * 
 * @data:2016-2-2 下午2:47:06
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
public class ClsOscilloscope {

	private ArrayList<Integer> inBuf = new ArrayList<Integer>();
	private boolean isRecording = false;// 录音线程控制标记

	/**
	 * 将字节转换成整形
	 */
	public int rateX = 4;

	/**
	 * Y轴缩小的比例
	 */
	public int rateY = 0;

	/**
	 * Y轴基线
	 */
	public int baseLine = 0;

	private AudioRecord audioRecord;

	int recBufSize;

	int sampleRateInHz = 44100;

	/**
	 * 两次绘图间隔的时间
	 */
	int draw_time = 350;
	int zDraw_time = 180;

	/**
	 * 为了节约绘画时间，每三个像素画一个数据
	 */
	int divider = 2;

	long c_time,l_time;
	/**
	 * 自定义控件
	 */
	private VisualizerView mBaseVisualizerView;
	/**
	 * 自定义控件
	 */
	private MyVisualizerView myviesu;
	/**
	 * 用于显示声音分贝大小的控件
	 */
	private TextView rel_voice_size, rel_voice_text;
	private int lastSize = 0;
	private RecordActivity recordactity;
	private boolean startrecord = false;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if(!startrecord)
					recordactity.createDialog(2);
				break;

			default:
				break;
			}
		};
	};

	/**
	 * 开始
	 * 
	 * @param audioRecord
	 * @param recBufSize
	 * @param sfv
	 * @param mPaint
	 */
	public void Start(AudioRecord audioRecord, int recBufSize,
			VisualizerView mBaseVisualizerView, RecordActivity recordactity,
			MyVisualizerView myviesu, TextView rel_voice_size,
			TextView rel_voice_text) {
		this.audioRecord = audioRecord;
		isRecording = true;
		this.mBaseVisualizerView = mBaseVisualizerView;
		this.myviesu = myviesu;
		this.recBufSize = recBufSize;
		this.rel_voice_size = rel_voice_size;
		this.rel_voice_text = rel_voice_text;
		this.recordactity = recordactity;

		new RecordTask(audioRecord, recBufSize).execute();
	}

	/**
	 * 停止
	 */
	public void Stop() {
		isRecording = false;
	}

	/**
	 * 异步录音程序
	 * 
	 * @author Li Shaoqing
	 * 
	 */
	class RecordTask extends AsyncTask<Object, Object, Object> {

		private int recBufSize;
		private AudioRecord audioRecord;
		private long lstTime = 0;
		private long lastrecord = 0;

		public RecordTask(AudioRecord audioRecord, int recBufSize) {
			this.audioRecord = audioRecord;
			this.recBufSize = recBufSize;
		}

		@Override
		protected Object doInBackground(Object... params) {
			try {
				byte[] buffer = new byte[recBufSize];
				try {
					handler.sendEmptyMessageDelayed(0, 100);
					audioRecord.startRecording(); // 开始录制
				} catch (Exception e) {
					// 权限拒绝
					recordactity.createDialog(2);
				}
				startrecord = true;
				while (isRecording) {
					// 从MIC保存数据到缓冲区
					int readsize = audioRecord.read(buffer, 0, recBufSize);
					if (readsize < 0) {
						// 权限拒绝
						long time = new Date().getTime();
						if(lastrecord==0){
							lastrecord = time;
						}else{
							if(time-lastrecord>2000)
								recordactity.createDialog(2);
						}
					}
					recordactity.dissmissDialog();
					lastrecord = 0;
					// if (mBaseVisualizerView != null){
					// mBaseVisualizerView.onFftDataCapture(buffer);
					// }
					synchronized (inBuf) {
						// 添加数据
						int len = readsize / rateX;
						long v = 0;
						for (int i = 0; i < len; i += rateX) {
							short shot = (short) ((0x0000 | buffer[i + 1]) << 8 | buffer[i]);
							v += shot * shot;
						}
						// 平方和除以数据总长度，得到音量大小。
						double mean = v / (double) len;
						int volume = (int) (10 * Math.log10(mean));
						// 添加数据
						inBuf.add(volume);
					}
					long time = new Date().getTime();
					if (time - c_time >= draw_time) {
						publishProgress();
						c_time = time;
					}
					if(time-l_time>=zDraw_time){
						l_time = time;
						if (mBaseVisualizerView != null) {
							mBaseVisualizerView.onFftDataCapture(buffer);
						}
					}
				}
				audioRecord.stop();
			} catch (Throwable t) {
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Object... values) {
			// long time = new Date().getTime();
			// if (time - c_time >= draw_time) {
			ArrayList<Integer> buf = new ArrayList<Integer>();
			synchronized (inBuf) {
				if (inBuf.size() == 0)
					return;
				while (inBuf.size() > myviesu.getWidth() / divider) {
					inBuf.remove(0);
				}
				buf = (ArrayList<Integer>) inBuf.clone();// 保存
			}
			if (myviesu != null) {
				int voiceSize = buf.get(buf.size() - 1);
				// long nowTime = System.currentTimeMillis();
				int voiceResidual = voiceSize - lastSize;
				if (Math.abs(voiceResidual) > 5) {
					// lstTime = System.currentTimeMillis();
					GlobalParams.MAX_VOICE = GlobalParams.MAX_VOICE< voiceSize?voiceSize:GlobalParams.MAX_VOICE;
					rel_voice_size.setText(voiceSize + "");
					if (voiceSize <= 30)
						rel_voice_text.setText(R.string.voice_sm);
					else if (voiceSize <= 50)
						rel_voice_text.setText(R.string.voice_m);
					else if (voiceSize <= 70)
						rel_voice_text.setText(R.string.voice_mm);
					else if (voiceSize <= 90)
						rel_voice_text.setText(R.string.voice_b);
					else
						rel_voice_text.setText(R.string.voice_bb);
					lastSize = voiceSize;
				}
				myviesu.getAll(buf);
			}
			// c_time = time;
			// c_time = new Date().getTime();
			// }
			super.onProgressUpdate(values);
		}

	}
}
