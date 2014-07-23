package client.ui;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Video extends Activity {

	private File myRecAudioFile;
	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	private Button btn_video_start;
	private Button btn_video_stop;
	private File dir;
	private MediaRecorder recorder;
	private String SD_CARD_TEMP_DIR = getSDPath() + File.separator + "myvideo" + File.separator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video);
		mSurfaceView = (SurfaceView) findViewById(R.id.videoView);
		Log.v("holder", String.valueOf(mSurfaceView == null));
		mSurfaceHolder = mSurfaceView.getHolder();
		Log.v("holder", String.valueOf(mSurfaceHolder == null));
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		btn_video_start = (Button) findViewById(R.id.video_start);
		btn_video_stop = (Button) findViewById(R.id.video_stop);
		dir = new File(SD_CARD_TEMP_DIR);
		if (!dir.exists()) {
			dir.mkdir();
		}
		recorder = new MediaRecorder();

		btn_video_start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				recorder();
			}
		});

		btn_video_stop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				recorder.stop();
				recorder.reset();
				recorder.release();
				recorder = null;
				
				Intent data=new Intent();  
	            data.putExtra("video_file", myRecAudioFile.getAbsolutePath());
	            //请求代码可以自己设置，这里设置audio=20  
	            setResult(21, data);  
	            //关闭掉这个Activity  
	            finish(); 
			}
		});
	}

	public void recorder() {
		try {
			myRecAudioFile = File.createTempFile("video", ".3gp", dir);// 创建临时文件
			recorder.setPreviewDisplay(mSurfaceHolder.getSurface());// 预览
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 录音源为麦克风
			recorder.setMaxDuration(10000);// 最大期限
			recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA); // 从照相机采集视频
			recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
			recorder.setVideoSize(480, 320);
			recorder.setVideoFrameRate(3); // 每秒3帧
			recorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT); // 设置视频编码方式
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			recorder.setOutputFile(myRecAudioFile.getAbsolutePath());// 保存路径
			recorder.prepare();
			recorder.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		} else {
			return null;
		}
		System.out.println("sd路径是：" + sdDir.toString());
		return sdDir.toString();

	}

}