package com.example.circleviewlibrary;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author caijinfu
 * @date 2018/8/16
 * @describe
 */
public class CustomViewGroup extends ViewGroup {

    private int paddingLeft;
    private int paddingRight;
    private int paddingTop;
    private int paddingBottom;
    private int viewsWidth;//所有子View的宽度之和（在该例子中仅代表宽度最大的那个子View的宽度）
    private int viewsHeight;//所有子View的高度之和
    private int viewGroupWidth = 0;//ViewGroup算上padding之后的宽度
    private int viewGroupHeight = 0;//ViewGroup算上padding之后的高度
    private int marginLeft;
    private int marginTop;
    private int marginRight;
    private int marginBottom;

    public CustomViewGroup(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CustomViewGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomViewGroup(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
    }

    /**
     * 此方法会在所有的控件都从xml文件中加载完成后调用
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewsWidth = 0;
        viewsHeight = 0;
        marginLeft = 0;
        marginTop = 0;
        marginRight = 0;
        marginBottom = 0;
        paddingLeft = getPaddingLeft();
        paddingTop = getPaddingTop();
        paddingRight = getPaddingRight();
        paddingBottom = getPaddingBottom();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            viewsHeight += childView.getMeasuredHeight();
            viewsWidth = Math.max(viewsWidth, childView.getMeasuredWidth());
            marginLeft = Math.max(0, lp.leftMargin);//在本例中找出最大的左边距
            marginTop += lp.topMargin;//在本例中求出所有的上边距之和
            marginRight = Math.max(0, lp.rightMargin);//在本例中找出最大的右边距
            marginBottom += lp.bottomMargin;//在本例中求出所有的下边距之和
        }
        /* 用于处理ViewGroup的wrap_content情况 */
        viewGroupWidth = paddingLeft + viewsWidth + paddingRight + marginLeft + marginRight;
        viewGroupHeight = paddingTop + viewsHeight + paddingBottom + marginTop + marginBottom;
        setMeasuredDimension(measureWidth(widthMeasureSpec, viewGroupWidth), measureHeight
                (heightMeasureSpec, viewGroupHeight));
    }

    private int measureWidth(int measureSpec, int viewGroupWidth) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = specSize;
                break;
            case MeasureSpec.AT_MOST:
                /* 将剩余宽度和所有子View + padding的值进行比较，取小的作为ViewGroup的宽度 */
                result = Math.min(viewGroupWidth, specSize);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }

    private int measureHeight(int measureSpec, int viewGroupHeight) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = specSize;
                break;
            case MeasureSpec.AT_MOST:
                /* 将剩余高度和所有子View + padding的值进行比较，取小的作为ViewGroup的高度 */
                result = Math.min(viewGroupHeight, specSize);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            int childCount = getChildCount();
            int mTop = paddingTop;
            for (int j = 0; j < childCount; j++) {
                View childView = getChildAt(j);
                int mLeft = paddingLeft;
                childView.layout(mLeft, mTop, mLeft + childView.getMeasuredWidth(), mTop + childView.getMeasuredHeight());
                mTop += childView.getMeasuredHeight();
            }
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    private static final String TAG = "UnrealView";

}
