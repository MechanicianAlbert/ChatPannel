package com.albertech.inputdemo.chatoperator.view;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import com.albertech.inputdemo.R;
import com.albertech.inputdemo.chatoperator.func.emoji.EmojiUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EmojiEditor extends EditText {

    private String mLastContent;


    public EmojiEditor(Context context) {
        super(context);
    }

    public EmojiEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmojiEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        SpannableString ss = new SpannableString(text);
//        Pattern pattern = Pattern.compile("\\[微笑\\]");
        Pattern pattern = Pattern.compile(EmojiUtil.getEmojiPattern());
        Matcher matcher = pattern.matcher(ss);
        while (matcher.find()){
            String code = matcher.group();
            int drawableRes = EmojiUtil.getDrawableResByCode(code);
            if (drawableRes != 0){
                Drawable drawable = getContext().getResources().getDrawable(drawableRes);
                int size = getContext().getResources().getDimensionPixelSize(R.dimen.input_pannel_emoji_icon_size);
                drawable.setBounds(0,0, size, size);
                ImageSpan span = new ImageSpan(drawable);
                ss.setSpan(span,matcher.start(),matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            }
        }
        Log.e("AAA", mLastContent + " " + text);
        if (!TextUtils.equals(mLastContent, text)) {
            mLastContent = String.valueOf(text);
            setText(ss);
            setSelection(ss.length());
        }
    }

}
