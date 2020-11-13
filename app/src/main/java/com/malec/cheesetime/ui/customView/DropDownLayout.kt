package com.malec.cheesetime.ui.customView

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.malec.cheesetime.R


class DropDownLayout(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private var dropDownParamsLayout: LinearLayout? = null
    private val dropDownButton: View

    private var animator: ValueAnimator? = null

    private val titleMarginStart: Float

    private val droppedDownInit: Boolean
    private var droppedDown: Boolean

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.DropDownLayout, 0, 0)
        val titleText = a.getString(R.styleable.DropDownLayout_titleText)
        titleMarginStart = a.getDimension(R.styleable.DropDownLayout_titleMarginStart, 0f)
        val contentOrientation = a.getInt(R.styleable.DropDownLayout_contentOrientation, 1)
        val contentGravity = a.getInt(R.styleable.DropDownLayout_contentGravity, 0x11)
        droppedDown = a.getBoolean(R.styleable.DropDownLayout_droppedDown, false)
        a.recycle()

        orientation = HORIZONTAL

        droppedDownInit = droppedDown

        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_drop_down, this, true)

        dropDownParamsLayout = findViewById<LinearLayout>(R.id.dropDownParamsLayout).apply {
            orientation = contentOrientation
            gravity = contentGravity
            isVisible = droppedDown
        }

        dropDownButton = findViewById<View>(R.id.dropDownButton).apply {
            background = if (droppedDown)
                ContextCompat.getDrawable(context, R.drawable.icon_arrow_down)
            else
                ContextCompat.getDrawable(context, R.drawable.icon_arrow_right)

            setOnClickListener { v ->
                if (!droppedDownInit)
                    droppedDown = !droppedDown

                animator?.cancel()
                val startAngle = if (droppedDown) 0f else 90f * if (droppedDownInit) -1 else 1
                val endAngle = if (droppedDown) 90f * if (droppedDownInit) -1 else 1 else 0f
                animator = ObjectAnimator.ofFloat(v, View.ROTATION, startAngle, endAngle).apply {
                    duration = 200
                    interpolator = LinearInterpolator()
                    addUpdateListener { valueAnimator ->
                        v.invalidate()
                        if (valueAnimator.animatedValue as Float == endAngle)
                            dropDownParamsLayout?.isVisible = droppedDown
                    }
                }
                animator?.start()

                if (droppedDownInit)
                    droppedDown = !droppedDown
            }
        }

        findViewById<TextView>(R.id.dropDownTitleText).apply {
            text = titleText
            setOnClickListener {
                dropDownButton.performClick()
            }
        }
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (dropDownParamsLayout == null) {
            if (child?.id == R.id.dropDownLayout)
                (params as LayoutParams?)?.leftMargin = titleMarginStart.toInt()
            super.addView(child, index, params)
        } else
            dropDownParamsLayout?.addView(child, index, params)
    }
}