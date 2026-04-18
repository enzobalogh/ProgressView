package com.example.avalicaon1_mobile;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class ProgressView extends View {

    private Paint backgroundPaint;
    private Paint progressPaint;
    private Paint textPaint;
    private Paint titlePaint;
    private RectF arcRect;

    private int progressColor = 0xFF4CAF50;
    private int backgroundCircleColor = 0xFFE0E0E0;
    private int textColor = 0xFF000000;
    private int titleTextColor = 0xFF666666;
    private String title = "Progresso semanal";
    private int maxValue = 100;
    private int currentProgress = 0;
    private int targetProgress = 0;

    private boolean isPercentMode = true;
    private ValueAnimator animator;

    private static final float STROKE_WIDTH = 24f;
    private static final float START_ANGLE = -90f;

    public ProgressView(Context context) {
        super(context);
        init(context, null);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ProgressView);
            progressColor = a.getColor(R.styleable.ProgressView_progressColor, progressColor);
            backgroundCircleColor = a.getColor(R.styleable.ProgressView_backgroundCircleColor, backgroundCircleColor);
            textColor = a.getColor(R.styleable.ProgressView_textColor, textColor);
            titleTextColor = a.getColor(R.styleable.ProgressView_titleTextColor, titleTextColor);
            title = a.getString(R.styleable.ProgressView_title);
            if (title == null) title = "Progresso semanal";
            maxValue = a.getInteger(R.styleable.ProgressView_maxValue, maxValue);
            currentProgress = a.getInteger(R.styleable.ProgressView_progress, currentProgress);
            targetProgress = currentProgress;
            a.recycle();
        }

        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(STROKE_WIDTH);
        backgroundPaint.setColor(backgroundCircleColor);
        backgroundPaint.setStrokeCap(Paint.Cap.ROUND);

        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(STROKE_WIDTH);
        progressPaint.setColor(progressColor);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(textColor);

        titlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setColor(titleTextColor);

        arcRect = new RectF();

        setOnClickListener(v -> toggleMode());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float padding = STROKE_WIDTH / 2 + 8;
        arcRect.set(padding, padding, w - padding, h - padding);

        float textSize = Math.min(w, h) * 0.2f;
        textPaint.setTextSize(textSize);
        titlePaint.setTextSize(textSize * 0.5f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int defaultSize = 200;
        int width = resolveSize(defaultSize, widthMeasureSpec);
        int height = resolveSize(defaultSize, heightMeasureSpec);
        int size = Math.min(width, height);
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float sweepAngle = (float) currentProgress / maxValue * 360f;

        canvas.drawArc(arcRect, 0, 360, false, backgroundPaint);

        canvas.drawArc(arcRect, START_ANGLE, sweepAngle, false, progressPaint);

        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;

        String progressText;
        if (isPercentMode) {
            int percent = maxValue > 0 ? (currentProgress * 100) / maxValue : 0;
            progressText = percent + "%";
        } else {
            progressText = currentProgress + "/" + maxValue;
        }

        Paint.FontMetrics fm = textPaint.getFontMetrics();
        float textY = centerY - (fm.ascent + fm.descent) / 2;
        canvas.drawText(progressText, centerX, textY, textPaint);

        float titleY = textY + textPaint.getTextSize() * 0.8f;
        canvas.drawText(title, centerX, titleY, titlePaint);
    }

    private void toggleMode() {
        isPercentMode = !isPercentMode;
        invalidate();
    }

    public void setProgress(int value) {
        setProgress(value, true);
    }

    public void setProgress(int value, boolean animate) {
        targetProgress = Math.max(0, Math.min(value, maxValue));

        if (animate) {
            if (animator != null && animator.isRunning()) {
                animator.cancel();
            }

            animator = ValueAnimator.ofInt(currentProgress, targetProgress);
            animator.setDuration(500);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.addUpdateListener(animation -> {
                currentProgress = (int) animation.getAnimatedValue();
                invalidate();
            });
            animator.start();
        } else {
            currentProgress = targetProgress;
            invalidate();
        }
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
        if (currentProgress > maxValue) {
            currentProgress = maxValue;
        }
        invalidate();
    }

    public void setProgressColor(int color) {
        this.progressColor = color;
        progressPaint.setColor(color);
        invalidate();
    }

    public void setTextColor(int color) {
        this.textColor = color;
        textPaint.setColor(color);
        invalidate();
    }

    public void setTitle(String title) {
        this.title = title;
        invalidate();
    }

    public int getProgress() {
        return currentProgress;
    }

    public int getMaxValue() {
        return maxValue;
    }
}