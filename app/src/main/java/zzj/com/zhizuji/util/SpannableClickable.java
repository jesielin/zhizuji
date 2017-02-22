package zzj.com.zhizuji.util;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import zzj.com.zhizuji.MyApp;
import zzj.com.zhizuji.R;

/**
 * @author yiw
 * @Description:
 * @date 16/1/2 16:32
 */
public abstract class SpannableClickable extends ClickableSpan implements View.OnClickListener {

    private int DEFAULT_COLOR_ID = R.color.default_url_text;
    /**
     * text颜色
     */
    private int textColor ;

    public SpannableClickable() {
        this.textColor = MyApp.getContext().getResources().getColor(DEFAULT_COLOR_ID);
    }

    public SpannableClickable(int textColor){
        this.textColor = textColor;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);

        ds.setColor(textColor);
        ds.setUnderlineText(false);
        ds.clearShadowLayer();
    }
}
