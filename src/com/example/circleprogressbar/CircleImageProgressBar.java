package com.example.circleprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
/**
 * �Զ����ͼƬ��Բ��progressbar
 * @author zhenfei.wang
 *
 */
public class CircleImageProgressBar extends View {

	/**
	 * ���ȵ����ֵ�͵�ǰ����
	 */
	private int maxProgress = 100;
	private int progress = 0;
	/**
	 * ͼƬ
	 */
	private Bitmap mSrc;
	/**
	 * �ؼ��Ŀ��
	 */
	private int mWidth;
	/**
	 * �ؼ��ĸ߶�
	 */
	private int mHeight;
	/**
	 * Բ�α��ߵĿ��
	 */
	private int strokeWidth;
	/**
	 * progress����ɫ�ͱ�����ɫ
	 */
	private int progressColor;
	private int backgroundColor = 0xFFC4D6D6;

	public CircleImageProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CircleImageProgressBar(Context context) {
		this(context, null);
	}

	/**
	 * ��ʼ��һЩ�Զ���Ĳ���
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public CircleImageProgressBar(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.CanvasView, defStyleAttr, 0);

		int n = a.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.CanvasView_src:
				mSrc = BitmapFactory.decodeResource(getResources(),
						a.getResourceId(attr, 0));
				break;
			case R.styleable.CanvasView_strokeWidth:
				strokeWidth = a.getDimensionPixelSize(attr, (int) TypedValue
						.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0f,
								getResources().getDisplayMetrics()));// ���Բ�α߿�Ŀ��
				break;

			case R.styleable.CanvasView_bordercolor:
				progressColor = a.getInt(attr, 0);// ���Բ�α߿����ɫ
				break;

			}
		}
		a.recycle();
	}

	/**
	 * ����ؼ��ĸ߶ȺͿ��
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		/**
		 * ���ÿ��
		 */
		int specMode = MeasureSpec.getMode(widthMeasureSpec);
		int specSize = MeasureSpec.getSize(widthMeasureSpec);

		if (specMode == MeasureSpec.EXACTLY)// match_parent , accurate
		{
			mWidth = specSize;
		} else if (specMode == MeasureSpec.AT_MOST)// wrap_content
		{
			// ��ͼƬ�����Ŀ�
			int desireByImg = getPaddingLeft() + getPaddingRight()
					+ mSrc.getWidth();
			mWidth = Math.min(desireByImg, specSize);
		}

		/***
		 * ���ø߶�
		 */

		specMode = MeasureSpec.getMode(heightMeasureSpec);
		specSize = MeasureSpec.getSize(heightMeasureSpec);
		if (specMode == MeasureSpec.EXACTLY)// match_parent , accurate
		{
			mHeight = specSize;
		} else if (specMode == MeasureSpec.AT_MOST)// wrap_content
		{
			int desire = getPaddingTop() + getPaddingBottom()
					+ mSrc.getHeight();
			mHeight = Math.min(desire, specSize);
		}
		setMeasuredDimension(mWidth + strokeWidth * 2, mHeight + strokeWidth
				* 2);

	}

	/**
	 * ����
	 */
	@Override
	protected void onDraw(Canvas canvas) {

		int min = Math.min(mWidth, mHeight);
		/**
		 * ���������һ�£���С��ֵ����ѹ��
		 */
		mSrc = Bitmap.createScaledBitmap(mSrc, min, min, false);
		
		if (strokeWidth != 0) {
			Paint paint = new Paint();
			paint.setColor(backgroundColor);
			paint.setAntiAlias(true);
			paint.setStyle(Paint.Style.STROKE); // ���ƿ���Բ
			paint.setStrokeWidth(strokeWidth);
			
			/**
			 * �Ȼ�progress����
			 */
			canvas.drawCircle(min / 2 + strokeWidth, min / 2 + strokeWidth, min
					/ 2 + strokeWidth / 2, paint);
			
			paint.setColor(progressColor);
			float arc = (float) ((progress*1.0/maxProgress)*360);
			canvas.drawArc(new RectF(strokeWidth/2, strokeWidth/2, (int)(min+strokeWidth*1.5),  (int)(min+strokeWidth*1.5)),
					-90, arc, true, paint);
		}
		canvas.drawBitmap(createCircleImage(mSrc, min), strokeWidth,
				strokeWidth, null);

	}

	/**
	 * ����ԭͼ�ͱ䳤����Բ��ͼƬ
	 * 
	 * @param source
	 * @param min
	 * @return
	 */
	private Bitmap createCircleImage(Bitmap source, int min) {
		final Paint paint = new Paint();
		paint.setColor(Color.YELLOW);
		paint.setAntiAlias(true);
		Bitmap target = Bitmap.createBitmap(min, min, Config.ARGB_8888);
		/**
		 * ����һ��ͬ����С�Ļ���
		 */
		Canvas canvas = new Canvas(target);
		/**
		 * ���Ȼ���Բ��
		 */
		canvas.drawCircle(min / 2, min / 2, min / 2, paint);// �Ȼ��ľ�����״
		/**
		 * ʹ��SRC_IN���ο������˵��
		 */
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		/**
		 * ����ͼƬ
		 */
		canvas.drawBitmap(source, 0, 0, paint);// �󻭵������
		return target;
	}

	
	public int getMaxProgress() {
		return maxProgress;
	}

	public void setMaxProgress(int maxProgress) {
		this.maxProgress = maxProgress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
		this.invalidate();
	}

	/**
	 * �ǣգ��̵߳���
	 */
	public void setProgressNotInUiThread(int progress) {
		this.progress = progress;
		this.postInvalidate();
	}
}
