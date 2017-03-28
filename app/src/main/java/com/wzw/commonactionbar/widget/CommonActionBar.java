package com.wzw.commonactionbar.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzw.commonactionbar.R;

/**
 * 一个通用的ActionBar
 * Created by Henry on 2017/3/27.
 */

public class CommonActionBar extends RelativeLayout {
    private static final int LEFT_BUTTON_ID = R.id.left_button;
    private static final int RIGHT_BUTTON_ID = R.id.right_button;
    private SparseArray<View> mButtons = new SparseArray<>(2);
    private TextView mTvTitle;
    private float mTitleSize = 20;
    private float mBtnTextSize = mTitleSize;
    private int mBtnTextColor = 16;
    private int mBtnPadding = 10;
    private int mActionBarHeight = 48;
    private int mTitleColor = getResources().getColor(android.R.color.white);

    public CommonActionBar(Context context) {
        this(context,null);
    }

    public CommonActionBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CommonActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.action_bar);
        String title =  typedArray.getString(R.styleable.action_bar_title_text);
        //生成标题
        addView(buildTitle(title));
        parseTypedArray(typedArray,LEFT_BUTTON_ID,R.styleable.action_bar_left_button);
        parseTypedArray(typedArray,RIGHT_BUTTON_ID,R.styleable.action_bar_right_button);
        typedArray.recycle();
    }

    private void parseTypedArray(TypedArray typedArray, int id, int styleableId) {
        int resId = typedArray.getResourceId(styleableId, 0);
        //reference---在xml里指定了按钮为图片
        if(resId > 0){
            addView(buildImageButton(id,resId));
        }else {
            //String---在xml里指定了按钮为文字
            CharSequence text = typedArray.getText(styleableId);
            if(text != null && !text.equals("")){
                addView(buildTextButton(id,text));
            }//既不是图片也不是文本就不生成这个按钮
        }
    }

    /**
     * 生成中间的标题
     * @param title
     * @return
     */
    private View buildTitle(String title) {
        mTvTitle = new TextView(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(CENTER_IN_PARENT);
        mTvTitle.setLayoutParams(params);
        mTvTitle.setText(title);
        mTvTitle.setPadding(mBtnPadding,mBtnPadding,mBtnPadding,mBtnPadding);
        mTvTitle.setGravity(Gravity.CENTER);
        mTvTitle.setSingleLine();
        mTvTitle.setEllipsize(TextUtils.TruncateAt.END);
        mTvTitle.setTextSize(mTitleSize);
        mTvTitle.setTextColor(mTitleColor);
        return mTvTitle;
    }

    /**
     * 生成图片按钮
     * @param id
     * @param resId
     * @return
     */
    private View buildImageButton(int id, int resId) {
        ImageView imageView = new ImageView(getContext());
        imageView.setId(id);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        setButtonRule(params,id);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setImageResource(resId);
        mButtons.put(id,imageView);
        return imageView;
    }

    /**
     * 生成文字按钮
     * @param id
     * @param text
     * @return
     */
    private View buildTextButton(int id, CharSequence text) {
        TextView textView= new TextView(getContext());
        textView.setId(id);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, mActionBarHeight);
        setButtonRule(params,id);
        textView.setLayoutParams(params);
        textView.setText(text);
        textView.setPadding(mBtnPadding,mBtnPadding,mBtnPadding,mBtnPadding);
        textView.setGravity(Gravity.CENTER);
        mTvTitle.setSingleLine();
        mTvTitle.setEllipsize(TextUtils.TruncateAt.END);
        textView.setTextSize(mBtnTextSize);
        textView.setTextColor(mBtnTextColor);
        mButtons.put(id,textView);
        return textView;
    }

    private void setButtonRule(LayoutParams params, int id) {
        switch (id){
            case R.id.left_button:
                params.addRule(ALIGN_PARENT_LEFT);
                break;
            case R.id.right_button:
                params.addRule(ALIGN_PARENT_RIGHT);
            default:
                params.addRule(CENTER_VERTICAL);
                params.leftMargin = mBtnPadding;
                params.rightMargin = mBtnPadding;
                break;
        }
    }

    /**
     * 修改标题
     * @param title
     */
    public void setTitle(String title){
        mTvTitle.setText(title);
    }

    /**
     * 修改按钮上的图标
     * @param id
     * @param resId
     */
    public void setButton(int id, int resId){
        View view = mButtons.get(id);
        if(view != null){
            view.setBackgroundResource(resId);
        }
    }

    /**
     * 修改按钮上的文字
     * @param id
     * @param text
     */
    public void setButton(int id, String text){
        View view = mButtons.get(id);
        if(view != null){
            ((TextView)view).setText(text);
        }
    }

    /**
     * 显示按钮
     * @param id
     */
    public void show(int id){
        setButtonVisible(id,true);
    }

    /**
     * 隐藏按钮
     * @param id
     */
    public void hide(int id){
        setButtonVisible(id,false);
    }


    private void setButtonVisible(int id, boolean visible) {
        View view = mButtons.get(id);
        if(view != null){
            view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }
    }

    /**
     * 设置按钮的点击事件
     * @param id
     * @param listener
     */
    public void setOnClickListener(int id, OnClickListener listener){
        View view = mButtons.get(id);
        if(view != null){
            view.setOnClickListener(listener);
        }
    }

}
