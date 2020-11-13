package com.malec.cheesetime.ui.customView

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import com.malec.cheesetime.R

class ActionButton(context: Context, attrs: AttributeSet) : CardView(context, attrs) {
    private val buttonRadius: Float

    private val checkedImage: Drawable?
    private val image: Drawable?
    private val checkable: Boolean
    private val shape: Int
    private val size: Int
    private val background: Int
    private val elev: Float

    private var checked = false

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ActionButton)
        checkedImage = a.getDrawable(R.styleable.ActionButton_ab_checkedImage)
        image = a.getDrawable(R.styleable.ActionButton_ab_image)
        checkable = a.getBoolean(R.styleable.ActionButton_ab_checkable, false)
        shape = a.getInt(R.styleable.ActionButton_ab_shape, 0)
        size = a.getInt(R.styleable.ActionButton_ab_size, 0)
        background = a.getColor(R.styleable.ActionButton_ab_backgroundColor, Color.TRANSPARENT)
        elev = a.getDimension(R.styleable.ActionButton_ab_elevation, 0f)
        a.recycle()

        val set = intArrayOf(android.R.attr.selectableItemBackground)
        val superA = context.obtainStyledAttributes(attrs, set)
        val selectableItemBackground = superA.getDrawable(0)
        superA.recycle()

        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_action_button, this, true)

        cardElevation = elev
        buttonRadius = if (size == 0) context.resources.getDimension(R.dimen.action_button_radius)
        else context.resources.getDimension(R.dimen.action_button_radius_small)
        radius = if (shape == 0) buttonRadius else buttonRadius * 0.3F
        isClickable = true
        isFocusable = true
        foreground = selectableItemBackground
        setCardBackgroundColor(background)
        getChildAt(0).background = getDrawable()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val size = (buttonRadius * 2).toInt()
        setMeasuredDimension(size, size)
    }

    private fun getDrawable(): Drawable {
        if (image == null) throw NullPointerException("Attribute ab_uncheckedImage is not set")

        return if (checkable) {
            if (checkedImage == null) throw NullPointerException("Attribute ab_checkedImage is not set")

            if (checked) checkedImage else image
        } else
            image
    }

    override fun performClick(): Boolean {
        checked = !checked
        getChildAt(0).background = getDrawable()
        invalidate()
        return super.performClick()
    }
}