package tv.giss.roulette

import android.animation.Animator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator

import kotlin.collections.List

/**
 * Created by chevil on 22/01/21.
 */

final class RouletteView(context : Context, attrs : AttributeSet?) : View( context, attrs ) {

    private var range : RectF = RectF()
    private var archPaint : Paint
    private var padding : Int = 0
    private var radius : Int = 0
    private var center : Int = 0
    private var actual : Int = 0
    private var mMoving : Boolean = false
    private lateinit var mRouletteSections : List<RouletteSection>

    init {
        //arc paInt object
        archPaint = Paint()
        archPaint.setAntiAlias(true)
        archPaint.setDither(true)
    }

    /**
     * Get the angle of the target
     *
     * @return Number of angle
     */
    private fun getAngleOfIndexTarget(target : Int) : Float {
        return (360 / mRouletteSections.size.toFloat()) * target
    }

    /**
     * Function to add roulette sections
     *
     * @param rouletteSections : Roulette section list
     */
    public fun addRouletteSections(rouletteSections: List<RouletteSection>) {
        mRouletteSections = rouletteSections
        invalidate()
    }

    /**
     * Function to draw roulette background
     *
     * @param canvas : Canvas 
     */
    private fun drawRouletteBackground(canvas : Canvas) {
        var backgroundPainter : Paint = Paint()
        backgroundPainter.setAntiAlias(true)
        backgroundPainter.setDither(true)
        backgroundPainter.setColor(Color.BLACK)
        canvas.drawCircle(center.toFloat(), center.toFloat(), center.toFloat(), backgroundPainter)
    }

    /**
     * Function to draw image in the center of arc
     *
     * @param canvas    : Canvas to draw
     * @param tempAngle : Temporary angle
     * @param bitmap    : Bitmap to draw
     */
    private fun drawImage(canvas : Canvas, tempAngle : Float, bitmap : Bitmap) {
        var imgWidth : Int = (radius/10)
        var angle : Double = ((tempAngle + 360 / mRouletteSections.size.toFloat() / 2) * Math.PI / 180)
        //calculate x and y
        var x : Int = Math.round((center + ( radius - 150 ) / 2 * Math.cos(angle))).toInt()
        var y : Int = Math.round((center + ( radius - 150 ) / 2 * Math.sin(angle))).toInt()
        // create rect to draw
        var rect : Rect = Rect(x-imgWidth/2, y-imgWidth/2, x+imgWidth/2, y+imgWidth/2)
        // rotate main bitmap
        var px : Float = rect.exactCenterX()
        var py : Float = rect.exactCenterY()
        var matrix : Matrix = Matrix()
        matrix.postTranslate(-bitmap.getWidth().toFloat() / 2, -bitmap.getHeight().toFloat() / 2)
        matrix.postRotate(tempAngle + 120)
        matrix.postTranslate(px, py)
        var bitPaint : Paint = Paint()
        bitPaint.setAntiAlias(true)
        bitPaint.setDither(true)
        bitPaint.setFilterBitmap(true)
        canvas.drawBitmap(bitmap, matrix, bitPaint)
        matrix.reset();
    }


    /**
     * Function to rotate n sections
     *
     * @param n : number od sections to rotate
     * @param clockwise : rotate clock-wise or not
     */
    public fun rotate(n : Int, clockwise : Boolean, callback : (res : Int?) -> Unit) {

        var rotation : Float 
        if (mMoving) return;

        if ( clockwise )
           rotation = n.toFloat()*(360.0f / mRouletteSections.size.toFloat()) + 3*360;
        else
           rotation = -n.toFloat()*(360.0f / mRouletteSections.size.toFloat()) - 3*360;

        actual = (actual+n) % mRouletteSections.size

        Log.v( this::class.simpleName, "Rotation angle : " + rotation )
        var DEFAULT_ROTATION_TIME : Long = 1000;
        animate().setInterpolator(DecelerateInterpolator())
                .setDuration(DEFAULT_ROTATION_TIME)
                .rotationBy(rotation)
                .setListener( object : Animator.AnimatorListener {
                    public override fun onAnimationStart(animation : Animator) {
                       mMoving = true
                    }

                    public override fun onAnimationEnd(animation : Animator) {
                       mMoving = false
                       clearAnimation()
                       callback.invoke(actual)
                    }

                    public override fun onAnimationCancel(animation : Animator) {
                    }

                    public override fun onAnimationRepeat(animation : Animator) {
                    }
                })
                .start();
    }

    /**
     * overriding View's onDraw function
     *
     * @param canvas : Canvas
     */
    protected override fun onDraw(canvas : Canvas) {
        super.onDraw(canvas)
        drawRouletteBackground(canvas)

        var tempAngle : Float = 0.0f
        var sweepAngle : Float = 360.0f / mRouletteSections.size.toFloat()

        for (i in 0..(mRouletteSections.size-1)) {
            archPaint.setColor(mRouletteSections.get(i).color.toInt())
            canvas.drawArc(range, tempAngle, sweepAngle, true, archPaint)
            drawImage(canvas, tempAngle, mRouletteSections.get(i).bitmap)
            tempAngle += sweepAngle
        }

    }

    /**
     * overriding View's onMeasure function to get roulette radius and padding
     *
     * @param widthMeasure : Measured width
     * @param heightMeasure : Measured height
     */
    protected override fun onMeasure(widthMeasure : Int, heightMeasure : Int) {
        super.onMeasure(widthMeasure, heightMeasure)

        var width : Int = Math.min(getMeasuredWidth(), getMeasuredHeight())
        var DEFAULT_PADDING : Int = 5
        padding = if ( getPaddingLeft() == 0 ) DEFAULT_PADDING else getPaddingLeft()
        radius = width - padding * 2
        center = width / 2
        setMeasuredDimension(width, width)
        range = RectF(padding.toFloat(), padding.toFloat(), (padding + radius).toFloat(), (padding + radius).toFloat())
    }

}
