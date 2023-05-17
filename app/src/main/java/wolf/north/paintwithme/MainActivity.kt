package wolf.north.paintwithme

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.get

class MainActivity : AppCompatActivity() {

    private var drawingView: DrawingView? = null
    private var mImageButtonCurrentPaint: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawingView = findViewById(R.id.drawing_view)
        drawingView?.setBrushSize(20.toFloat()) //DEFAULT BRUSH SIZE ON START

        val linearLayoutOfColors: LinearLayout = findViewById(R.id.linear_palletOfColors)

        mImageButtonCurrentPaint = linearLayoutOfColors[1] as ImageButton


        val imgBttnBrushSelector : ImageButton = findViewById(R.id.imgbttn_brushSizeSelector)
        imgBttnBrushSelector.setOnClickListener {
            displayBrushSizeDialog()
        }
    }

    //THIS METHOD IS DIALOG WINDOW DISPLAYED AFTER CLICKING ON BRUSH ICON,
    //FOR EACH BUTTON (small,medium,big) WE USE METHOD setBrushSize AND CHANGE ITS VALUE
    //Dialog.dismiss() is important as Dialog.show() to make dialog works proper
    private fun displayBrushSizeDialog(){
        val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.dialog_brush_size) // SET HOW DIALOG SHOULD LOOK (CREATED LAYOUT)
        brushDialog.setTitle("Brush size: ")
        brushDialog.show()
        val smallImgBttn : ImageView = brushDialog.findViewById(R.id.imgbttn_brush_small)
        smallImgBttn.setOnClickListener {
            drawingView?.setBrushSize(10.toFloat()) //CHANGING BRUSH SIZE
            brushDialog.dismiss() // HIDE DIALOG AFTER CLICKING ON SMALL BRUSH SIZE
        }
        val mediumImgBttn : ImageView = brushDialog.findViewById(R.id.imgbttn_brush_medium)
        mediumImgBttn.setOnClickListener {
            drawingView?.setBrushSize(20.toFloat()) //CHANGING BRUSH SIZE
            brushDialog.dismiss() // HIDE DIALOG AFTER CLICKING ON MEDIUM BRUSH SIZE
        }
        val bigImgBttn : ImageView = brushDialog.findViewById(R.id.imgbttn_brush_big)
        bigImgBttn.setOnClickListener {
            drawingView?.setBrushSize(30.toFloat()) //CHANGING BRUSH SIZE
            brushDialog.dismiss() // HIDE DIALOG AFTER CLICKING ON BIG BRUSH SIZE
        }

    }

    fun paintColorClicked(view: View){
        if(view !== mImageButtonCurrentPaint){
            val imageButton = view as ImageButton
            val colorTag = imageButton.tag.toString()
            drawingView?.setColor(colorTag)

            imageButton.setImageDrawable(
                ContextCompat.getDrawable(this,R.drawable.pallet_pressed)
            )

            mImageButtonCurrentPaint?.setImageDrawable(
                ContextCompat.getDrawable(this,R.drawable.pallet_normal)
            )
            mImageButtonCurrentPaint = view
        }
    }
}