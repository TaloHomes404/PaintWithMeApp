package wolf.north.paintwithme

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

//CLASS THAT WE ARE GOING TO USE IN OUR PAINTING APPLICATION, INHERITANCE FROM VIEW CLASS
//ALLOWING US TO DRAW SOMETHING ON SCREEN, IT HAVE PARAMETERS LIKE CONTEXT AND ATTRIBUTES
//THAT ALLOWS CANVA LIBRARY TO USE ARGUMENTS LIKE COLOR, THICKNESS OF BRUSH
//INNER CLASS CUSTOMPATH IS INHERITANCE FROM PATH BUILD-IN CLASS, ALLOWING US TO DRAW WITH OUR SHAPE
class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var mDrawPath: CustomPath? = null
    private var mCanvasBitmap: Bitmap? = null
    private var mDrawPaint: Paint? = null
    private var mCanvasPaint: Paint? = null
    private var mBrushSize: Float = 0.toFloat()
    private var color = Color.BLACK
    private var canvas: Canvas? = null

    init{
        setUpDrawingScreen()
    }

    private fun setUpDrawingScreen(){
        mDrawPaint = Paint()
        mDrawPath = CustomPath(color, mBrushSize)
        mDrawPaint!!.color = color
        mDrawPaint!!.style = Paint.Style.STROKE
        mDrawPaint!!.strokeJoin = Paint.Join.ROUND
        mDrawPaint!!.strokeCap = Paint.Cap.ROUND
        mCanvasPaint = Paint(Paint.DITHER_FLAG)
        mBrushSize = 20.toFloat()
    }

    //BUILD IN VIEW METHOD THAT ALLOWS US TO DRAW ON BITMAP WE CREATED
    //PARAMETERS LIKE WIDTH AND HEIGHT ALLOWS US TO SETUP BITMAP ON THESE POSITIONS (X,Y)
    //onSizeChanged - CHECK GOOGLE DOCUMENTATION LATER
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCanvasBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888 )
        canvas = Canvas (mCanvasBitmap!!)
    }

    //BUILD IN VIEW METHOD THAT IS CALLED EVERYIME WE WANT TO DRAW ON SCREEN BY TOUCH
    //USES CANVAS PARAMETER THAT IS STRUCTURED WITH OUR BITMAP, POSITION OF OUR CLICK (left,top)
    //AND THIRD PARAMETER AS mCanvasPaint WHERE WE STYLED OUR PAINT (COLOR, STYLE, STROKES)
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(mCanvasBitmap!!, 0f, 0f, mCanvasPaint)
    }

    internal inner class CustomPath(var color: Int, var thicknessOfBrush: Float) : Path() {

    }

}