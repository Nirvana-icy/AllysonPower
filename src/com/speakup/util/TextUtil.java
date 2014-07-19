package com.speakup.util;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtil {

    /**
     * å°?textä¸?@???äººç??å­?ä½????äº?ï¼???¹é?????è¡¨æ?????å­?ä»¥è¡¨?????¾ç¤º
     * @param text
     * @param context
     * @return
     */
    public static void formatContent(TextView textView) {
        SpannableString spannableString = new SpannableString(textView.getText());
        /*
         * @[^\\s:ï¼?]+[:ï¼?\\s] ??¹é??@???äº?  \\[[^0-9]{1,4}\\] ??¹é??è¡¨æ??
         */
        Pattern pattern = Pattern.compile("@[^\\s:ï¼?]+[:ï¼?\\s]|\\[[^0-9]{1,4}\\]");
        Matcher matcher = pattern.matcher(spannableString);
        while (matcher.find()) {
            String match=matcher.group();
            if(match.startsWith("@")){ //@???äººï?????äº?å­?ä½?
                spannableString.setSpan(new ForegroundColorSpan(0xff0077ff),
                        matcher.start(), matcher.end(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

    }

}
