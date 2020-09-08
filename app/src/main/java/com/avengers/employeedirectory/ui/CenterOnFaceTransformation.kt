package com.avengers.employeedirectory.ui

import android.graphics.Bitmap
import android.graphics.Rect
import android.util.Log
import coil.bitmap.BitmapPool
import coil.size.Size
import coil.transform.Transformation
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class CenterOnFaceTransformation(): Transformation {
    companion object {
        private const val TAG = "CenterOnFaceTransform"
    }

    override fun key(): String = CenterOnFaceTransformation::class.java.name

    override suspend fun transform(pool: BitmapPool, input: Bitmap, size: Size): Bitmap {
        // High-accuracy landmark detection and face classification

        val highAccuracyOpts = FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .build()

        val detector = FaceDetection.getClient(highAccuracyOpts)
        val image = InputImage.fromBitmap(input, 0)

        return suspendCancellableCoroutine<Bitmap> { continuation ->
            detector.process(image).addOnSuccessListener { faces ->
                val biggest = faces.maxByOrNull { it.boundingBox.height() }
                val output: Bitmap

                if (biggest != null) {
                    val boundingBox = biggest.boundingBox
                    val square = largestSquare(input, boundingBox)
                    output = Bitmap.createBitmap(input, square.left, square.top, square.width(), square.height())
                    continuation.resume(output)
                } else {
                    continuation.resume(input)
                }

            }.addOnFailureListener { e ->
                Log.e(TAG, "transform: ERROR: $e")
                continuation.resumeWithException(e)
            }
        }
    }

    private fun largestSquare(input: Bitmap, boundingBox: Rect): Rect {
        val centerX = boundingBox.exactCenterX()
        val centerY = boundingBox.exactCenterY()

        val halfWidth = Collections.min(
                listOf(centerX,
                        centerY,
                        input.width.toFloat() - centerX,
                        input.height.toFloat() - centerY)
        )
        val width = halfWidth * 2
        val left = (centerX - halfWidth).toInt()
        val top = (centerY - halfWidth).toInt()
        val right = left + width.toInt()
        val bottom = top + width.toInt()
        return Rect( left,
                top,
                right,
                bottom)
    }
}