package com.example.splitmirroreffectwithfacedetection

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection

/**
 * Created by Efe Şen on 24,10,2024
 */
object ImageUtils {

    fun splitMirrorFilterWithFaceDetection(originalBitmap: Bitmap, callback: (Bitmap) -> Unit) {
        //  Face Detection
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .enableTracking()
            .build()

        // Generate Input Image
        val image = InputImage.fromBitmap(originalBitmap, 0)

        // Face Detector
        val detector = FaceDetection.getClient(options)

        // Start face detection
        detector.process(image)
            .addOnSuccessListener { faces ->
                if (faces.isNotEmpty()) {
                    val face = faces[0] // first detected face
                    val faceBounds = face.boundingBox

                    // Take the x-position of the face and slide a little to the left to set the crop start point
                    val cropStartX = (faceBounds.centerX() - originalBitmap.width * 0.25).toInt().coerceIn(0, originalBitmap.width / 2)
                    val leftHalfWidth = (originalBitmap.width * 0.5).toInt()

                    // Crop the left half
                    val leftHalfBitmap = Bitmap.createBitmap(originalBitmap, cropStartX, 0, leftHalfWidth, originalBitmap.height)

                    // Mirror effect
                    val matrix = Matrix().apply {
                        preScale(-1f, 1f) // Horizontal mirroring
                    }
                    val mirroredRightHalf = Bitmap.createBitmap(leftHalfBitmap, 0, 0, leftHalfBitmap.width, leftHalfBitmap.height, matrix, true)

                    // Create new bitmap for final image
                    val resultBitmap = Bitmap.createBitmap(originalBitmap.width, originalBitmap.height, originalBitmap.config)

                    // Merge two halves with Canvas
                    val canvas = Canvas(resultBitmap)
                    canvas.drawBitmap(leftHalfBitmap, 0f, 0f, null) // Sol yarıyı çiz
                    canvas.drawBitmap(mirroredRightHalf, leftHalfWidth.toFloat(), 0f, null) // Sağ yarıya yansıtılmış hali çiz

                    // Return result
                    callback(resultBitmap)
                } else {
                    // Return original image if face not found
                    callback(originalBitmap)
                }
            }
            .addOnFailureListener {
                // Return original image in case of error
                callback(originalBitmap)
            }
    }



    fun getResizedBitmap(image: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val ratioBitmap = image.width.toFloat() / image.height.toFloat()
        val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()

        var finalWidth = maxWidth
        var finalHeight = maxHeight
        if (ratioMax > 1) {
            finalWidth = (maxHeight * ratioBitmap).toInt()
        } else {
            finalHeight = (maxWidth / ratioBitmap).toInt()
        }

        return Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true)
    }
}