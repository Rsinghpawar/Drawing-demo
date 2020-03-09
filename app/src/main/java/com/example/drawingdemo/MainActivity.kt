package com.example.drawingdemo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.select_brush_size.*
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val canvas = PaintView(this, null)

        clr.setOnClickListener{
            paintView.clear()
        }

        purple.setOnClickListener{
            canvas.setColor(ContextCompat.getColor(this, R.color.colorPrimary) )
        }

        cyan.setOnClickListener {
            canvas.setColor(ContextCompat.getColor(this, R.color.colorAccent) )
            }

        blue.setOnClickListener {
            canvas.setColor(ContextCompat.getColor(this, R.color.colorPrimaryDark) )
        }

        eraser.setOnClickListener {
            canvas.setColor(ContextCompat.getColor(this, R.color.white) )
           // PaintView.PAINT_COLOR = ContextCompat.getColor(this, R.color.white)
        }

        brushSize.setOnClickListener{
            if ( selectSize.visibility == View.GONE ) {
                selectSize.visibility = View.VISIBLE
                size.requestFocus()
            } else
                selectSize.visibility = View.GONE
        }

        ok.setOnClickListener {
            if ( size.text.toString().toFloat() in 1..50 && size.text.isNotBlank() ) {
                canvas.setStroke(size.text.toString().toFloat())
               // PaintView.BRUSH_SIZE = size.text.toString().toFloat()
                selectSize.visibility = View.GONE

                val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(ok.windowToken, 0)
            }
            else
                Toast.makeText(this, "Size must be in range of 1 to 50" , Toast.LENGTH_SHORT).show()
        }

        save.setOnClickListener {
            Toast.makeText(this, "Pending", Toast.LENGTH_SHORT).show()
            //canvas.
        }

    }
}
