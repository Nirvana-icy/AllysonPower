package com.speakup.util;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtil {

    /**
     * �?text�?@???人�??�?�????�?�???��?????表�?????�?以表?????�示
     * @param text
     * @param context
     * @return
     */
    public static void formatContent(TextView textView) {
        SpannableString spannableString = new SpannableString(textView.getText());
        /*
         * @[^\\s:�?]+[:�?\\s] ??��??@???�?  \\[[^0-9]{1,4}\\] ??��??表�??
         */
        Pattern pattern = Pattern.compile("@[^\\s:�?]+[:�?\\s]|\\[[^0-9]{1,4}\\]");
        Matcher matcher = pattern.matcher(spannableString);
        while (matcher.find()) {
            String match=matcher.group();
            if(match.startsWith("@")){ //@???人�?????�?�?�?
                spannableString.setSpan(new ForegroundColorSpan(0xff0077ff),
                        matcher.start(), matcher.end(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

    }

}
