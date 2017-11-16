package com.example.xy.scanningview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by xy on 2017/10/13.
 */

public class ScanningView extends View {

    private Context mContext;

    private Paint paintLine;//画圆

    private Paint paintIcon;//画中间的图

    private Paint paintScan;//画扫描
    private Shader scanShader;//扫描渲染shader
    private Matrix matrix = new Matrix();//旋转需要的矩阵
    private int scanAngle = 0;      //扫描旋转的角度
    private boolean mRun = false;

    private Bitmap centerBitmap;

    private float lineWidth = 5f;
    private float[] circleProportion = {1 / 8f, 2 / 8f, 3 / 8f, 4 / 8f};

    private int width, height;

    public ScanningView(Context context) {
        this(context, null);
    }

    public ScanningView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScanningView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        init();
        post(run);
    }

    private Runnable run = new Runnable() {
        @Override
        public void run() {
            if (mRun) {
                scanAngle = (scanAngle + 1) % 360;
                matrix.postRotate(1, width / 2, height / 2);
                invalidate();
            }
            postDelayed(run,10);
        }
    };

    private void init() {
        paintLine = new Paint();
        paintLine.setColor(ContextCompat.getColor(mContext, R.color.colorWhite));
        paintLine.setAntiAlias(true);
        paintLine.setStrokeWidth(lineWidth);
        paintLine.setStyle(Paint.Style.STROKE);

        paintIcon = new Paint();
        paintIcon.setColor(ContextCompat.getColor(mContext, R.color.colorWhite));
        paintIcon.setAntiAlias(true);

        paintScan = new Paint();
        paintScan.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureSize(widthMeasureSpec), measureSize(heightMeasureSpec));
        width = getMeasuredWidth();
        height = getMeasuredHeight();

        centerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.device_searching);
        scanShader = new SweepGradient(width / 2, height / 2,
                new int[]{Color.TRANSPARENT, ContextCompat.getColor(mContext, R.color.colorDivier)}, null);
    }

    private int measureSize(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = 300;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCircle(canvas);
        drawIcon(canvas);
        drawCross(canvas);
        drawScan(canvas);
    }

    private void drawCircle(Canvas canvas) {
        paintLine.setStrokeWidth(lineWidth / 2);
        canvas.drawCircle(width / 2, height / 2, width * circleProportion[1] - lineWidth / 4, paintLine);
        canvas.drawCircle(width / 2, height / 2, width * circleProportion[2] - lineWidth / 4, paintLine);
        paintLine.setStrokeWidth(lineWidth);
        canvas.drawCircle(width / 2, height / 2, width * circleProportion[3] - lineWidth / 4, paintLine);
    }

    private void drawIcon(Canvas canvas) {
        canvas.drawBitmap(centerBitmap, null, new Rect((int) (width / 2 - width * circleProportion[0]),
                        (int) (height / 2 - height * circleProportion[0]),
                        (int) (width / 2 + width * circleProportion[0]),
                        (int) (height / 2 + height * circleProportion[0])),
                paintIcon);
    }

    private void drawCross(Canvas canvas) {
        paintLine.setStrokeWidth(lineWidth / 2);
        canvas.drawLine(0, height / 2, width, height / 2, paintLine);
        canvas.drawLine(width / 2, 0, width / 2, height, paintLine);
    }

    private void drawScan(Canvas canvas) {
        canvas.save();
        paintScan.setShader(scanShader);
        canvas.concat(matrix);
        canvas.drawCircle(width / 2, height / 2, width * circleProportion[3], paintScan);
        canvas.restore();
    }

    public void setRun(boolean run) {
        this.mRun = run;
    }
}
