package com.nerdinfusions.sjwalls.extensions.views
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.view.postDelayed
import coil.load
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.nerdinfusions.sjwalls.extensions.context.drawable
import com.nerdinfusions.sjwalls.extensions.context.preferences
import com.nerdinfusions.sjwalls.extensions.resources.hasContent
import com.nerdinfusions.sjwalls.ui.animations.SaturatingImageViewTarget

private const val CROSSFADE_DURATION = 200

private fun ImageView.buildSaturatingTarget(
    block: SaturatingImageViewTarget.() -> Unit
): SaturatingImageViewTarget = SaturatingImageViewTarget(this).apply(block)

private fun ImageView.buildRequestBuilder(
    thumbnail: Drawable?,
    cropAsCircle: Boolean,
    saturate: Boolean,
    extra: ((drawable: Drawable?) -> Unit)? = null
): ImageRequest.Builder.() -> Unit = {
    fallback(thumbnail)
    placeholder(thumbnail)
    error(thumbnail)

    if (thumbnail == null) crossfade(CROSSFADE_DURATION)
    else crossfade(false)

    if (cropAsCircle) transformations(CircleCropTransformation())

    val saturationTarget = buildSaturatingTarget {
        shouldActuallySaturate = saturate
        addListener { extra?.invoke(it) }
    }

    target(saturationTarget)
    listener(saturationTarget)
}

private fun ImageView.internalLoadFrames(
    url: String?,
    thumbnail: Drawable?,
    cropAsCircle: Boolean,
    saturate: Boolean,
    extra: ((drawable: Drawable?) -> Unit)? = null
) {
    load(url, builder = buildRequestBuilder(thumbnail, cropAsCircle, saturate, extra))
}

fun ImageView.loadFramesPic(
    url: String,
    thumbnailUrl: String? = url,
    placeholder: Drawable? = null,
    forceLoadFullRes: Boolean = false,
    cropAsCircle: Boolean = false,
    saturate: Boolean = true,
    onImageLoaded: ((drawable: Drawable?) -> Unit)? = null
) {
    val shouldLoadThumbnail = thumbnailUrl?.let { it.hasContent() && it != url } ?: false
    if (shouldLoadThumbnail) {
        if (context.preferences.shouldLoadFullResPictures || forceLoadFullRes) {
            internalLoadFrames(thumbnailUrl, placeholder, cropAsCircle, saturate) {
                onImageLoaded?.invoke(it)
                internalLoadFrames(url, it, cropAsCircle, false, onImageLoaded)
            }
        } else {
            internalLoadFrames(thumbnailUrl, placeholder, cropAsCircle, saturate, onImageLoaded)
        }
    } else {
        internalLoadFrames(url, placeholder, cropAsCircle, saturate, onImageLoaded)
    }
}

fun ImageView.loadFramesPicResPlaceholder(
    url: String,
    thumbnailUrl: String? = url,
    placeholderName: String? = "",
    forceLoadFullRes: Boolean = false,
    cropAsCircle: Boolean = false,
    saturate: Boolean = true,
    onImageLoaded: ((drawable: Drawable?) -> Unit)? = null
) = loadFramesPic(
    url, thumbnailUrl,
    context.drawable(placeholderName),
    forceLoadFullRes, cropAsCircle, saturate,
    onImageLoaded
)

fun ImageView.startAnimatable() {
    postDelayed(IMAGEVIEW_ANIMATABLE_DELAY) { (drawable as? Animatable)?.start() }
}

private const val IMAGEVIEW_ANIMATABLE_DELAY = 75L
