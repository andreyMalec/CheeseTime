package com.malec.cheesetime.ui.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.malec.cheesetime.R
import kotlin.math.max

class CircularDiagram(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val fillColor: Int
    private val strokeColor: Int
    private val backgroundColor: Int
    private var value = 1f
    private var maxValue = 1f

    private var percent = 1f;

    private val paint: Paint
    private val strokePaint: Paint
    private val backgroundPaint: Paint
    private val arc = RectF(0f, 0f, 0f, 0f)

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.CircularDiagram)
        fillColor = a.getColor(
            R.styleable.CircularDiagram_fillColor,
            context.getColor(R.color.backgroundLight)
        )
        strokeColor = a.getColor(
            R.styleable.CircularDiagram_strokeColor,
            context.getColor(R.color.colorPrimaryDark)
        )
        backgroundColor = a.getColor(
            R.styleable.CircularDiagram_backgroundColor,
            context.getColor(R.color.colorShadowLight)
        )
        setValue(a.getFloat(R.styleable.CircularDiagram_value, 1f))
        setMaxValue(a.getFloat(R.styleable.CircularDiagram_maxValue, 1f))
        a.recycle()

        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.FILL
        paint.color = fillColor

        strokePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        strokePaint.style = Paint.Style.STROKE
        strokePaint.strokeWidth = 2f
        strokePaint.color = strokeColor

        backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        backgroundPaint.style = Paint.Style.FILL
        backgroundPaint.color = backgroundColor
    }

    fun setValue(value: Float) {
        this.value = value
        invalidate()
    }

    fun setMaxValue(maxValue: Float) {
        this.maxValue = maxValue
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        percent = value / maxValue
        if (percent > 1)
            percent = 1f

        canvas?.translate(1f, 1f)
        canvas?.drawArc(arc, 0f, 360f, true, backgroundPaint)
        canvas?.drawArc(arc, 270f, 360 * percent, true, paint)
        canvas?.drawArc(arc, 270f, 360 * percent, true, strokePaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val measuredWidth = MeasureSpec.getSize(widthMeasureSpec)
        val measuredHeight = MeasureSpec.getSize(heightMeasureSpec)
        val circleDiameter = max(measuredWidth, measuredHeight) - 2f
        arc.right = circleDiameter
        arc.bottom = circleDiameter
    }
}