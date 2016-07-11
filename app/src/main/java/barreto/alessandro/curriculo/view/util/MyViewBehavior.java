package barreto.alessandro.curriculo.view.util;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;

import barreto.alessandro.curriculo.R;

/**
 * Created by Alessandro on 08/07/2016.
 */
public class MyViewBehavior extends CoordinatorLayout.Behavior<View> {

    private static final int UNSPECIFIED_INT = Integer.MAX_VALUE;
    private static final float UNSPECIFIED_FLOAT = Float.MAX_VALUE;

    /**
     * Depend on the dependency view height
     */
    private static final int DEPEND_TYPE_HEIGHT = 0;

    /**
     * Depend on the dependency view width
     */
    private static final int DEPEND_TYPE_WIDTH = 1;

    /**
     * Depend on the dependency view x position
     */
    private static final int DEPEND_TYPE_X = 2;

    /**
     * Depend on the dependency view y position
     */
    private static final int DEPEND_TYPE_Y = 3;

    private int mDependType;
    private int mDependViewId;

    private int mDependTargetX;
    private int mDependTargetY;
    private int mDependTargetWidth;
    private int mDependTargetHeight;

    private int mDependStartX;
    private int mDependStartY;
    private int mDependStartWidth;
    private int mDependStartHeight;

    private int mStartX;
    private int mStartY;
    private int mStartWidth;
    private int mStartHeight;
    private int mStartBackgroundColor;
    private float mStartAlpha;
    private float mStartRotateX;
    private float mStartRotateY;

    public int targetX;
    public int targetY;
    public int targetWidth;
    public int targetHeight;
    public int targetBackgroundColor;
    public float targetAlpha;
    public float targetRotateX;
    public float targetRotateY;

    private int mAnimationId;
    private Animation mAnimation;

    private boolean isPrepared;

    public MyViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);


        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CoordinatorView);
        mDependViewId = a.getResourceId(R.styleable.CoordinatorView_dependsOn, 0);
        mDependType = a.getInt(R.styleable.CoordinatorView_dependType, DEPEND_TYPE_WIDTH);
        mDependTargetX = a.getDimensionPixelOffset(R.styleable.CoordinatorView_dependTargetX, UNSPECIFIED_INT);
        mDependTargetY = a.getDimensionPixelOffset(R.styleable.CoordinatorView_dependTargetY, UNSPECIFIED_INT);
        mDependTargetWidth = a.getDimensionPixelOffset(R.styleable.CoordinatorView_dependTargetWidth, UNSPECIFIED_INT);
        mDependTargetHeight = a.getDimensionPixelOffset(R.styleable.CoordinatorView_dependTargetHeight, UNSPECIFIED_INT);
        targetX = a.getDimensionPixelOffset(R.styleable.CoordinatorView_targetX, UNSPECIFIED_INT);
        targetY = a.getDimensionPixelOffset(R.styleable.CoordinatorView_targetY, UNSPECIFIED_INT);
        targetWidth = a.getDimensionPixelOffset(R.styleable.CoordinatorView_targetWidth, UNSPECIFIED_INT);
        targetHeight = a.getDimensionPixelOffset(R.styleable.CoordinatorView_targetHeight, UNSPECIFIED_INT);
        targetBackgroundColor = a.getColor(R.styleable.CoordinatorView_targetBackgroundColor, UNSPECIFIED_INT);
        targetAlpha = a.getFloat(R.styleable.CoordinatorView_targetAlpha, UNSPECIFIED_FLOAT);
        targetRotateX = a.getFloat(R.styleable.CoordinatorView_targetRotateX, UNSPECIFIED_FLOAT);
        targetRotateY = a.getFloat(R.styleable.CoordinatorView_targetRotateY, UNSPECIFIED_FLOAT);
        mAnimationId = a.getResourceId(R.styleable.CoordinatorView_animation, 0);
        a.recycle();
    }

    private void prepare(CoordinatorLayout parent, View child, View dependency) {
        mDependStartX = (int) dependency.getX();
        mDependStartY = (int) dependency.getY();
        mDependStartWidth = dependency.getWidth();
        mDependStartHeight = dependency.getHeight();
        mStartX = (int) child.getX();
        mStartY = (int) child.getY();
        mStartWidth = child.getWidth();
        mStartHeight = child.getHeight();
        mStartAlpha = child.getAlpha();
        mStartRotateX = child.getRotationX();
        mStartRotateY = child.getRotationY();

        if (child.getBackground() instanceof ColorDrawable) {
            mStartBackgroundColor = ((ColorDrawable) child.getBackground()).getColor();
        }

        if (mAnimationId != 0) {
            mAnimation = AnimationUtils.loadAnimation(child.getContext(), mAnimationId);
            mAnimation.initialize(child.getWidth(), child.getHeight(), parent.getWidth(), parent.getHeight());
        }

        if (Build.VERSION.SDK_INT > 16 && parent.getFitsSystemWindows() && targetY != UNSPECIFIED_INT) {
            int result = 0;
            int resourceId = parent.getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = parent.getContext().getResources().getDimensionPixelSize(resourceId);
            }
            targetY += result;
        }

        isPrepared = true;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {

        return dependency.getId() == mDependViewId;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {

        if (!isPrepared) {
            prepare(parent, child, dependency);
        }
        updateView(child, dependency);
        return false;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        boolean bool = super.onLayoutChild(parent, child, layoutDirection);
        if (isPrepared) {
            updateView(child, parent.getDependencies(child).get(0));
        }
        return bool;
    }


    public void updateView(View child, View dependency) {
        float percent = 0;
        float start = 0;
        float current = 0;
        float end = UNSPECIFIED_INT;
        switch (mDependType) {
            case DEPEND_TYPE_WIDTH:
                start = mDependStartWidth;
                current = dependency.getWidth();
                end = mDependTargetWidth;
                break;
            case DEPEND_TYPE_HEIGHT:
                start = mDependStartHeight;
                current = dependency.getHeight();
                end = mDependTargetHeight;
                break;
            case DEPEND_TYPE_X:
                start = mDependStartX;
                current = dependency.getX();
                end = mDependTargetX;
                break;
            case DEPEND_TYPE_Y:
                start = mDependStartY;
                current = dependency.getY();
                end = mDependTargetY;
                break;
        }

        if (end != UNSPECIFIED_INT) {
            percent = Math.abs(current - start) / Math.abs(end - start);
        }
        updateViewWithPercent(child, percent > 1 ? 1 : percent);
    }

    public void updateViewWithPercent(View child, float percent) {

        if (mAnimation == null) {
            float newX = targetX == UNSPECIFIED_INT ? 0 : (targetX - mStartX) * percent;
            float newY = targetY == UNSPECIFIED_INT ? 0 : (targetY - mStartY) * percent;

            if (targetWidth != UNSPECIFIED_INT || targetHeight != UNSPECIFIED_INT) {
                float newWidth = mStartWidth + ((targetWidth - mStartWidth) * percent);
                float newHeight = mStartHeight + ((targetHeight - mStartHeight) * percent);

                child.setScaleX(newWidth / mStartWidth);
                child.setScaleY(newHeight / mStartHeight);

                newX -= (mStartWidth - newWidth) / 2;
                newY -= (mStartHeight - newHeight) / 2;
            }


            child.setTranslationX(newX);
            child.setTranslationY(newY);

            if (targetAlpha != UNSPECIFIED_FLOAT) {
                child.setAlpha(mStartAlpha + (targetAlpha - mStartAlpha) * percent);
            }

            if (targetBackgroundColor != UNSPECIFIED_INT && mStartBackgroundColor != 0) {
                ArgbEvaluator evaluator = new ArgbEvaluator();
                int color = (int) evaluator.evaluate(percent, mStartBackgroundColor, targetBackgroundColor);
                child.setBackgroundColor(color);
            }


            if (targetRotateX != UNSPECIFIED_FLOAT) {
                child.setRotationX(mStartRotateX + (targetRotateX - mStartRotateX) * percent);
            }
            if (targetRotateY != UNSPECIFIED_FLOAT) {
                child.setRotationX(mStartRotateY + (targetRotateY - mStartRotateY) * percent);
            }
        } else {

            mAnimation.setStartTime(0);
            mAnimation.restrictDuration(100);
            Transformation transformation = new Transformation();
            mAnimation.getTransformation((long) (percent * 100), transformation);

            MyBehaviorAnimation animation = new MyBehaviorAnimation(transformation);
            child.startAnimation(animation);
        }

        child.requestLayout();
    }


    private static class MyBehaviorAnimation extends Animation {

        private Transformation mTransformation;

        public MyBehaviorAnimation(Transformation transformation) {
            mTransformation = transformation;

            setDuration(0);
            setFillAfter(true);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            t.compose(mTransformation);
            super.applyTransformation(interpolatedTime, t);
        }
    }


}
