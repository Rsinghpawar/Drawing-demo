package com.example.drawingdemo

import android.content.Context
import android.graphics.*
import android.os.Environment
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import java.io.FileOutputStream


class PaintView (context: Context, attrs: AttributeSet?) : View( context, attrs ) {

    companion object {
        //initial color
        private var PAINT_COLOR = -13172557
        private var BRUSH_SIZE = 25F
    }

    private lateinit var paths : HashMap<Path, Paint>

    private lateinit var undonePaths : HashMap<Path, Paint>

    private var currentColor = -16524603

    //drawing path
    private var drawPath: Path? = null

    //defines what to draw
    private var canvasPaint: Paint? = null

    //defines how to draw
    private var drawPaint: Paint? = null

    

    //canvas - holding pen, holds your drawings
//and transfers them to the view
    private var drawCanvas: Canvas? = null

    //canvas bitmap
    private var canvasBitmap: Bitmap? = null

    //brush size
    private var currentBrushSize = 0f  //brush size
    private var lastBrushSize = 0f

//    fun PaintView(context : Context) {}

    init {
        initializeVariables()
    }

    private fun initializeVariables() {
        Log.d("color", "clled")
        currentBrushSize = 25F
        lastBrushSize = currentBrushSize
        drawPath = Path()
        drawPaint = Paint()
        drawPaint!!.color = currentColor
        drawPaint!!.strokeWidth = currentBrushSize
        drawPaint!!.style = Paint.Style.STROKE
        drawPaint!!.strokeJoin = Paint.Join.ROUND
        drawPaint!!.strokeCap = Paint.Cap.ROUND
        paths = HashMap<Path,Paint>()
        undonePaths = HashMap<Path, Paint>()
        canvasPaint = Paint(Paint.DITHER_FLAG)
    }

    fun setColor(color :Int) {
        PAINT_COLOR = color
    }

    fun setStroke (stroke : Float) {
        BRUSH_SIZE = stroke
    }


    override fun onSizeChanged(
        w: Int,
        h: Int,
        oldw: Int,
        oldh: Int
    ) {
        //create canvas of certain device size.
        super.onSizeChanged(w, h, oldw, oldh)
        //create Bitmap of certain w,h
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        //apply bitmap to graphic to start drawing.
        drawCanvas = Canvas(canvasBitmap!!)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                drawPath!!.moveTo(touchX, touchY)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                Log.d("touch", "Moving {$touchX} , { $touchY }")
                drawPath!!.lineTo(touchX, touchY)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
               // drawPath!!.lineTo(touchX, touchY)
                drawCanvas!!.drawPath(drawPath!!, drawPaint!!)
                paths.put(drawPath!!, drawPaint!!)
                Log.d("pathsSize", "${paths.size}")
                drawPath = Path()
                invalidate()
            }
            else -> return false
        }
        //redraw
       // invalidate()
        return true
    }

    fun clear() {
        drawCanvas!!.drawColor(Color.WHITE)
        paths.clear()
        undonePaths.clear()
        invalidate()
        Log.d("Cls", "CLR")
    }

    override fun onDraw(canvas: Canvas) {

        drawPaint!!.strokeWidth = BRUSH_SIZE
        drawPaint!!.color = PAINT_COLOR // for changing color

        //canvas.drawBitmap(canvasBitmap!!, 0f, 0f, canvasPaint)

        for ( p in paths  ) {
            canvas.drawBitmap(canvasBitmap!!, 0f, 0f, p.value)
            canvas.drawPath(p.key, p.value)
        }
        Log.d("pathsSizeOnDraw", "${paths.size}")
        //Toast.makeText(context, paths.size.toString(), Toast.LENGTH_SHORT).show()
        canvas.drawPath(drawPath!!, drawPaint!!)
    }

    fun saveImage ()  {
        val fileName = "${Environment.getExternalStorageDirectory()}/test.png"
        val stream = FileOutputStream(fileName)
        canvasBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.close()
    }

    fun undo () {
        Log.d("pathsSizeUndo", "${paths.size}")
        if ( paths.size > 0 ) {
            //undonePaths.put(paths!!.remove(paths!!.size - 1), )
        }else {
            Toast.makeText(context, "nothing to UNDO", Toast.LENGTH_SHORT).show()
        }
        invalidate()
    }

    fun redo () {
        if ( undonePaths.size > 0 ) {
           // paths.add(undonePaths.removeAt(undonePaths.size - 1))
        }else {
            Toast.makeText(context, "nothing to REDO", Toast.LENGTH_SHORT).show()
        }
        invalidate()
    }
}