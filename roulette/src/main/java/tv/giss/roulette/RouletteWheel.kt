package tv.giss.roulette

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView

import kotlin.collections.List;

/**
 * Created by chevil on 22/01/21.
 */

class RouletteWheel(context : Context, attrs : AttributeSet?) : FrameLayout(context, attrs), View.OnTouchListener {

    var roulette : RouletteView
    var nbSections : Int = -1
    var x1 : Double = -1.0
    var x2 : Double = -1.0
    var y1 : Double = -1.0
    var y2 : Double = -1.0
    var moveAngle : Double = 0.0
    var forward : Boolean = false

    init {
       inflate(getContext(), R.layout.roulette_layout, this);
       setOnTouchListener(this)
       roulette = findViewById( R.id.roulette )
    }

    /**
     * Function to add sections to the wheel
     *
     * @param wheelSections Roulette sections
     */
    public fun addRouletteSections(rouletteSections : List <RouletteSection>) {
       roulette.addRouletteSections(rouletteSections)
       nbSections = rouletteSections.size
    }

    /**
     * Function to rotate wheel to a section target
     *
     * @param n : number of sections to rotate
     * @param clockwise : clockwise or not
     */
    public fun rotate(n : Int, clockwise : Boolean) {
        roulette.rotate(n, clockwise)
    }

    public override fun onTouch(v : View, event : MotionEvent) : Boolean {

        if ( nbSections <= 0 ) return false;

        when (event.getAction()) {
            MotionEvent.ACTION_DOWN -> {
                x1 = event.getX().toDouble()
                y1 = event.getY().toDouble()
            }
            MotionEvent.ACTION_UP -> {
                x2 = event.getX().toDouble()
                y2 = event.getY().toDouble()
                if ( Math.abs(y2-y1) > Math.abs(x2-x1) )
                {
                   if (y2-y1 > 0.0 )
                     forward = true;
                   else
                     forward = false;
                }
                else
                {
                   if (x2-x1 > 0.0 )
                     forward = true;
                   else
                     forward = false;
                }
                rotate((0..(nbSections-1)).random(), forward)
            }
        }
        return true
    }
}
