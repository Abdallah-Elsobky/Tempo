package iti.student.finalproject.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.toDrawable

object DrawableHelper {
    fun resizeDrawable(
        context: Context,
        drawableResId: Int,
        newWidth: Int,
        newHeight: Int,
        tint: Int
    ): Drawable {
        val bitmap = createBitmap(newWidth, newHeight)
        val canvas = Canvas(bitmap)
        val drawable = ContextCompat.getDrawable(context, drawableResId)!!.mutate()
        drawable.setBounds(0, 0, newWidth, newHeight)
        drawable.setTint(tint)
        drawable.draw(canvas)
        return bitmap.toDrawable(context.resources)
    }
}