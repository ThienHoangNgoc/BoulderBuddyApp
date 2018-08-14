package andorid_dev_2017.navigation_drawer;

import android.graphics.Bitmap;

public class NewEntryGridViewItem {


    Bitmap bitmap;

    public NewEntryGridViewItem(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
