package com.example.food_order.utils.extension

import android.content.Context
import android.content.res.Resources
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.TypedValue
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.food_order.R
import kotlin.collections.forEach

fun View.clickAnimation() {
    try {
        if (!isAttachedToWindow) {
            return
        }

        context ?: return
        clickAnimation(context, this)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun clickAnimation(mContext: Context?, view: View) {
    if (mContext != null) {
        val myAnim = AnimationUtils.loadAnimation(mContext, R.anim.bounce)
        view.startAnimation(myAnim)
    }
}

fun TextView.makeExpandable(fullText: String, maxLinesCollapsed: Int) {
    val seeMoreText = " ... See more"
    val seeLessText = " See less"

    post {
        // Nếu text không dài hơn số dòng tối đa thì không cần làm gì cả
        if (lineCount <= maxLinesCollapsed) {
            text = fullText
            return@post
        }
        //Tạo trạng thái mở rộng

        val spannableExpanded = SpannableString(fullText + seeLessText)
        val collapseSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {

                // Khi bấm "See less", quay lại trạng thái thu gọn
                maxLines = maxLinesCollapsed
                text = createCollapsedSpannable(
                    fullText,
                    maxLinesCollapsed,
                    seeMoreText,
                    spannableExpanded
                )
                movementMethod = LinkMovementMethod.getInstance()
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isFakeBoldText = true
                ds.color =
                    ContextCompat.getColor(context, R.color.white)
            }
        }
        spannableExpanded.setSpan(
            collapseSpan,
            fullText.length,
            spannableExpanded.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        //Tạo trạng thái thu gọn
        text = createCollapsedSpannable(fullText, maxLinesCollapsed, seeMoreText, spannableExpanded)
        movementMethod = LinkMovementMethod.getInstance()
        maxLines = maxLinesCollapsed
    }
}


// Hàm trợ giúp để tạo SpannableString cho trạng thái thu gọn
private fun TextView.createCollapsedSpannable(
    fullText: String,
    maxLinesCollapsed: Int,
    seeMoreText: String,
    spannableExpanded: SpannableString
): SpannableString {

    val lastCharIndex = layout.getLineEnd(maxLinesCollapsed - 1)
    val truncatedText = fullText.substring(0, lastCharIndex - seeMoreText.length) + seeMoreText
    val spannableCollapsed = SpannableString(truncatedText)
    val expandSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {

            // Khi bấm "See more", chuyển sang trạng thái mở rộng
            maxLines = Integer.MAX_VALUE
            text = spannableExpanded
            movementMethod = LinkMovementMethod.getInstance()
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.isFakeBoldText = true
            ds.color = ContextCompat.getColor(context, R.color.white)
        }
    }
    spannableCollapsed.setSpan(
        expandSpan,
        truncatedText.indexOf(seeMoreText),
        truncatedText.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return spannableCollapsed
}

//Hàm đổi đơn vị px sang dp
fun Resources.dpToPx(dp: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        this.displayMetrics
    ).toInt()
}

//Hàm set color

fun TextView.setTextColorRes(colorResId: Int) {
    setTextColor(ContextCompat.getColor(context, colorResId))
}

fun setTextColorForButton(buttons: List<TextView>, colorResId: Int) {
    buttons.forEach { it.setTextColorRes(colorResId) }
}

val View.isShow: Boolean
    get() = this.isVisible

val View.isHide: Boolean
    get() = this.isInvisible

fun View.hide() {
    if (!isInvisible) {
        this.visibility = View.INVISIBLE
    }
}

fun View.show() {
    if (!isVisible) {
        this.visibility = View.VISIBLE
    }
}

fun View.gone() {
    if (!isGone) {
        this.visibility = View.GONE
    }
}

fun View.showOrGone(isShow: Boolean) {
    if (isShow) {
        show()
    } else {
        gone()
    }
}

fun View.showOrHide(isShow: Boolean) {
    if (isShow) {
        show()
    } else {
        hide()
    }
}