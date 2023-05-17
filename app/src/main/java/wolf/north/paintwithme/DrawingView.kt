package wolf.north.paintwithme

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
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
    private val mSavedPaths = ArrayList<CustomPath>()

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
        canvas = Canvas(mCanvasBitmap!!)
    }


    //BUILD IN VIEW METHOD THAT IS CALLED EVERYIME WE WANT TO DRAW ON SCREEN BY TOUCH
    //USES CANVAS PARAMETER THAT IS STRUCTURED WITH OUR BITMAP, POSITION OF OUR CLICK (left,top)
    //AND THIRD PARAMETER AS mCanvasPaint WHERE WE STYLED OUR PAINT (COLOR, STYLE, STROKES)
    //ITS SAFER TO USE NOT-NULL CANVAS AS PARAMETER, BUT IF IT THROWS ERRORS, CHANGE TO Canvas? NULL-SAFE TYPE
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(mCanvasBitmap!!, 0f, 0f, mCanvasPaint)

        for(path in mSavedPaths){
            mDrawPaint!!.strokeWidth = path.thicknessOfBrush
            mDrawPaint!!.color = path.color
            canvas.drawPath(path, mDrawPaint!!) // THIS VALUE IS THE THING THAT WE DRAW ON SCREEN
        }

        if(!mDrawPath!!.isEmpty) {
            mDrawPaint!!.strokeWidth = mDrawPath!!.thicknessOfBrush
            mDrawPaint!!.color = mDrawPath!!.color

            canvas.drawPath(mDrawPath!!, mDrawPaint!!) // THIS VALUE IS THE THING THAT WE DRAW ON SCREEN
        }

    }

    //IMPORTANT METHOD OF VIEW WHERE WE CLICK, IT GETS POSITION OF IT AND DRAW ON IT
    override fun onTouchEvent(event: MotionEvent): Boolean {

        val touchPositionX = event.x  //TOUCH POSTION IN X VECTOR
        val touchPositionY = event.y  // TOUCH POSTION IN Y VECTOR

        when(event.action){
            MotionEvent.ACTION_DOWN -> {
                mDrawPath?.color = color
                mDrawPath?.thicknessOfBrush = mBrushSize
                //SETTING UP OUR DRAWIN PATCH (LANE WE DRAW ON BITMAP, COLOR AND THICKNESS

                mDrawPath?.reset()
                //CLEAR ANY EXISTING LINES WHEN WE DRAW ON IT
                mDrawPath?.moveTo(touchPositionX,touchPositionY)
            }

            MotionEvent.ACTION_MOVE -> {
                        mDrawPath?.lineTo(touchPositionX,touchPositionY)
            }

            MotionEvent.ACTION_UP -> {
                mSavedPaths.add(mDrawPath!!)
                mDrawPath = CustomPath(color,mBrushSize)

            }
            else -> return false
        }
        invalidate()
        return true
    }

    fun setBrushSize(newSize: Float){
        mBrushSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, newSize, resources.displayMetrics)

        mDrawPaint!!.strokeWidth = mBrushSize
    }

    fun setColor(newColor: String){
        color = Color.parseColor(newColor)
        mDrawPaint!!.color = color
    }

    internal inner class CustomPath(var color: Int, var thicknessOfBrush: Float) : Path() {

    }

}