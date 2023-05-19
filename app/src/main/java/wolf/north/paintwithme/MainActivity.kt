package wolf.north.paintwithme

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get

class MainActivity : AppCompatActivity() {

    private var drawingView: DrawingView? = null
    private var mImageButtonCurrentPaint: ImageView? = null


    val openGaleryLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        //LONGEST KOTLIN METHOD I EVER SEEN LOL X2
        //THIS TIME WE DONT ASK FOR PERMISSION BUT WE SETUP OUR LAUNCHER TO REGISTER ACTIVITY THAT WILL BE OPENING APP - GALERY AND PICKING PHOTO
        //THEN REPLACING IMAGEBACKGROUND WITH IT AS DATA
        result ->
        if(result.resultCode == RESULT_OK && result.data != null){
            val imageBackground: ImageView = findViewById(R.id.imgv_background)
            imageBackground.setImageURI(result.data?.data)
        }

    }


    //LONGEST KOTLIN METHOD I EVER SEEN LOL
    //WE ATTACH TO requestPermission VALUE A METHOD THAT MAKES A ARRAY OF STRINGS AND PUT PERMISSIONS THAT WE NEED IN APPLICATION
    //IT HAVE ATTACHED VALUE "permissions" THAT CAN BE USED LATER, WE USE FOR EACH TO GET VALUES OF PERMISSIONS (key - for example storage permission, value - granted or no)
    //THEN SIMPLE IF STATEMENT TO SEE IF USER ALLOWED TO USE STORAGE, YES = OPEN GALLERY, NO = DON'T BOTHER ME BRO, IDK
    val requestedPermission: ActivityResultLauncher<Array<String>> = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
        permissions ->
        permissions.entries.forEach{
            val permissionName = it.key
            val permissionValue = it.value
            
            if(permissionValue)  {    // IS GRANTED
                //THEN SHOW TOAST AND PICK IMAGE FROM GALERY
                Toast.makeText(this, "Permission granted, read storage files.", Toast.LENGTH_SHORT).show()
                val pickImageIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)  // INTENT TO GET INTO ANOTHER APP (OUR GALERY)
                openGaleryLauncher.launch(pickImageIntent) // USER OUR SETUP ACTIVITYRESULTLAUNCHER WITH LOADED PHOTO AS DATA FROM GALERY, CHANGE OUR BACKGROUND PHOTO TO DATA

            }else{ //IS NOT GRANTED
                if(permissionName==Manifest.permission.READ_EXTERNAL_STORAGE){
                    Toast.makeText(this, "You declined permission grant, use settings for futher storage permission.", Toast.LENGTH_SHORT).show()
                }
            }
    }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawingView = findViewById(R.id.drawing_view)
        drawingView?.setBrushSize(20.toFloat()) //DEFAULT BRUSH SIZE ON START

        val linearLayoutOfColors: LinearLayout = findViewById(R.id.linear_palletOfColors)

        mImageButtonCurrentPaint = linearLayoutOfColors[1] as ImageButton

        //GALLERY BUTTON FUNCTIONALITY
        val imgBttnGalery: ImageButton = findViewById(R.id.imgbttn_imageFromGalerySelector)
        imgBttnGalery.setOnClickListener(){
            requestStoragePermission()
        }

        //BRUSH BUTTON FUNCTIONALITY
        val imgBttnBrushSelector : ImageButton = findViewById(R.id.imgbttn_brushSizeSelector)
        imgBttnBrushSelector.setOnClickListener {
            displayBrushSizeDialog()
        }

        val imgBttnUndoSelector : ImageButton = findViewById(R.id.imgbttn_undoSelector)
        imgBttnUndoSelector.setOnClickListener {
            undoLastDrawingChange()
        }

    }

    private fun undoLastDrawingChange() {
        TODO("Not yet implemented")
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
        //THIS BUILD IN FUNCTION ALLOWS US TO SELECT CURRENT IMAGEBUTTON WITH COLOR
        //AND GET TAG FROM THIS BUTTON THEN CHANGE COLOR OF DRAWINGVIEW TO THIS TAG
        //setImageDrawable IS METHOD THAT ALLOWS US TO CHANGE COLOR IMAGEBUTTON SO IT WILL LOOK LIKE ITS SELECTED
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

    private fun requestStoragePermission(){
        //FUNCTION THAT IS JUST FUNCTIONALITY OF ASKING FOR PERMISION BY SHOWING REQUEST ON SCREEN
        //SHOW RATIONAL DIALOG IS ANOTHER FUNCTION THAT JUST CRATING A DIALOG, WE USE TITLE AND MESSAGE TO SHOW IT ON SCREEN
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            showRationaleDialog("PaintWithMe", "PaintWithMe need to ACCES your external storage, allow?")
        }else{
            requestedPermission.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
        }
    }

    private fun showRationaleDialog(title: String, message: String) {
        //SIMPLE FUNCTION THAT IS JUST A DIALOG BUILDED WITH BUILDER
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title).setMessage(message).setPositiveButton("Cancel"){
            dialog, _ -> dialog.dismiss()
        }
        builder.create().show()
    }
}