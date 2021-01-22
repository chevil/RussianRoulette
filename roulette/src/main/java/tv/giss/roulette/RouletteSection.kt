package tv.giss.roulette;

import android.graphics.Bitmap;

/**
 * Created by chevil on 22/01/21.
 */

public class RouletteSection(icolor : Int, ibitmap : Bitmap) {

    var color : Int = 0;
    var bitmap : Bitmap;

    init {
        this.color = icolor;
        this.bitmap = ibitmap;
    }

}
