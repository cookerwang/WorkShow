package me.utils;
import android.content.Context;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
public class StringStyleUtils {
    /**
     <style name="ViaTextAppearance">
     <item name="android:textSize">12sp</item>
     <item name="android:textColor">@color/md_grey_400</item>
     </style>
     */
    public static SpannableString format(Context context, String text, int style) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new TextAppearanceSpan(context, style), 0, text.length(),
                0);
        return spannableString;
    }
}
