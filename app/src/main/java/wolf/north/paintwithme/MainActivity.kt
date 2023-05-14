package wolf.north.paintwithme

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class MainActivity : AppCompatActivity() {

    private var drawingView: DrawingView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawingView = findViewById(R.id.drawing_view)
        drawingView?.setBrushSize(20.toFloat()) //DEFAULT BRUSH SIZE ON START
    }

    private fun displayBrushSizeDialog(){
        val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.dialog_brush_size) // SET HOW DIALOG SHOULD LOOK (CREATED LAYOUT)
        brushDialog.setTitle("Brush size: ")
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
            brushDialog.dismiss() // HIDE DIALOG AFTER CLICKING ON MEDIUM BRUSH SIZE
        }

    }


}