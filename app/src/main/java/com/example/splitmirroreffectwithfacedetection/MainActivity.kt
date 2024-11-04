package com.example.splitmirroreffectwithfacedetection

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.splitmirroreffectwithfacedetection.ImageUtils.getResizedBitmap
import com.example.splitmirroreffectwithfacedetection.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val originalBitmap = BitmapFactory.decodeResource(resources, R.drawable.model_girl)
        val resizedBitmap = getResizedBitmap(originalBitmap, 1000, 1000) // Adjust maxWidth and maxHeight as needed
        ImageUtils.splitMirrorFilterWithFaceDetection(resizedBitmap) {
            binding.imageView.setImageBitmap(it)
        }

    }
}