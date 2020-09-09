package com.avengers.employeedirectory.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.util.Log
import android.util.LruCache
import coil.bitmap.BitmapPool
import coil.size.Size
import coil.transform.Transformation
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class CenterOnFaceTransformation @Inject constructor(
    private val cache: LruCache<String, ByteArray>): Transformation {

    companion object {
        private const val TAG = "CenterOnFaceTransform"
    }

    override fun key(): String = CenterOnFaceTransformation::class.java.name

    override suspend fun transform(pool: BitmapPool, input: Bitmap, size: Size): Bitmap {
        // High-accuracy landmark detection and face classification
        val inputByteArray = input.toByteArray()
        val inputByteArrayHash = inputByteArray.toList().hashCode().toString()
        val cachedTransformation = cache.get(inputByteArrayHash)
        if (cachedTransformation != null) {
            return BitmapFactory
                .decodeByteArray(cachedTransformation, 0, cachedTransformation.size)
        }
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
                    output = Bitmap.createBitmap(
                        input,
                        square.left,
                        square.top,
                        square.width(),
                        square.height()
                    )
                    val imageByteArray = output.toByteArray()
                    cache.put(inputByteArrayHash, imageByteArray)
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

    private fun Bitmap.toByteArray() : ByteArray {
        val stream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private fun largestSquare(input: Bitmap, boundingBox: Rect): Rect {
        val centerX = boundingBox.exactCenterX()
        val centerY = boundingBox.exactCenterY()

        val halfWidth = Collections.min(
            listOf(
                centerX,
                centerY,
                input.width.toFloat() - centerX,
                input.height.toFloat() - centerY
            )
        )
        val width = halfWidth * 2
        val left = (centerX - halfWidth).toInt()
        val top = (centerY - halfWidth).toInt()
        val right = left + width.toInt()
        val bottom = top + width.toInt()
        return Rect(
            left,
            top,
            right,
            bottom
        )
    }
}