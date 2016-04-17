
package cs4720.studyhome;
import android.graphics.drawable.Drawable;

public class AppDetail implements Comparable{
    CharSequence label;
    String name;
    CharSequence type;
    Drawable icon;

    @Override
    public int compareTo(Object another) {
        AppDetail ad = (AppDetail) another;
        return (this.name).compareTo(ad.name);
    }
}