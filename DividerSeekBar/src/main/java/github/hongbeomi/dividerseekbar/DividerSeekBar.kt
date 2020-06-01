package github.hongbeomi.dividerseekbar

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.SeekBar
import androidx.annotation.*

class DividerSeekBar
@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatSeekBar(context, attrs, defStyleAttr) {

    private val mMinHeight = resources.getDimensionPixelSize(R.dimen.dp_48)
    private val mMinWidth = resources.getDimensionPixelSize(R.dimen.dp_16)
    private var mWidth: Int? = null
    private var mHeight: Int? = null

    companion object {

        @IntDef(ACTIVE_MODE_TARGET, ACTIVE_MODE_MINIMUM, ACTIVE_MODE_MAXIMUM)
        @Retention(AnnotationRetention.SOURCE)
        annotation class ActiveMode

        const val ACTIVE_MODE_TARGET = 0
        const val ACTIVE_MODE_MINIMUM = 1
        const val ACTIVE_MODE_MAXIMUM = 2

        @IntDef(TEXT_LOCATION_TOP, TEXT_LOCATION_BOTTOM)
        @Retention(AnnotationRetention.SOURCE)
        annotation class TextLocation

        const val TEXT_LOCATION_BOTTOM = 0
        const val TEXT_LOCATION_TOP = 1

    }

    private var mTextPaint: Paint? = null
    private var mTextRect: Rect? = null
    private var mTextLocationMode = TEXT_LOCATION_BOTTOM
    private var mTextInterval: Int = 1
        set(value) {
            if (value == 0) {
                throw IntervalZeroException()
            } else {
                field = value
            }
        }
    @ColorInt
    private var mTextColor: Int = resources.getColor(R.color.gray_400)
    private var mTextSize: Float = resources.getDimension(R.dimen.sp_12)

    private var mSeaLinePaint: Paint? = null
    @ColorInt
    private var mSeaLineColor: Int = resources.getColor(R.color.gray_400)
    private var mSeaLineStokeWidth: Float = resources.getDimension(R.dimen.dp_2)

    private var mDividerPaint: Paint? = null
    private var mDividerInterval: Int = 1
        set(value) {
            if (value == 0) {
                throw IntervalZeroException()
            } else {
                field = value
            }
        }
    @ColorInt
    private var mDividerColor: Int = resources.getColor(R.color.gray_400)
    private var mDividerStrokeWidth: Float = resources.getDimension(R.dimen.dp_2)
    private var mThumbDefaultDrawable: Drawable =
        resources.getDrawable(R.drawable.bg_thumb_default)
    private var mThumbActivatedDrawable: Drawable =
        resources.getDrawable(R.drawable.bg_thumb_activated)

    private var mActiveMode = ACTIVE_MODE_MINIMUM
    private var mActivateTargetValue: Int = 0
    private var mActivatedEventSwitch: Boolean = true
    private var mIsActivated: Boolean = false
        set(value) {
            if (mActivatedEventSwitch) {
                field = value
                thumb = if (value) {
                    mOnDividerSeekBarChangeStateListener?.onProgressEnabled(this, progress)
                    mThumbDefaultDrawable
                } else {
                    mOnDividerSeekBarChangeStateListener?.onProgressDisabled(this, progress)
                    mThumbActivatedDrawable
                }
                invalidate()
            }
        }

    private var mOnDividerSeekBarChangeStateListener: OnDividerSeekBarChangeStateListener? = null

    init {
        setPadding(
            resources.getDimensionPixelSize(R.dimen.dp_16),
            0,
            resources.getDimensionPixelSize(R.dimen.dp_16),
            0
        )
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DividerSeekBar)
        try {
            setTypeArray(typedArray)
        } finally {
            typedArray.recycle()
        }
        mIsActivated = getIsActivatedByActiveMode(progress)
        backgroundTintList = null
        background = null
        progressDrawable = null
        setOnDividerSeekBarChangeListener()
    }

    override fun setProgress(progress: Int) {
        super.setProgress(progress)
        mIsActivated = getIsActivatedByActiveMode(progress)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        mHeight = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> mMinHeight.coerceAtMost(heightSize)
            else -> mMinHeight
        }
        mWidth = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> mMinWidth.coerceAtMost(widthSize)
            else -> mMinWidth
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(mWidth ?: mMinWidth, mHeight ?: mMinHeight)
    }

    override fun onDraw(canvas: Canvas) {
        setSeaLinePaint()
        setDividerPaint()
        setTextPaint()
        val halfHeight = height / 2
        var position: Float
        val div = (width - paddingRight - paddingLeft) / max.toFloat()
        for (index in 0..max) {
            if (index % mDividerInterval == 0) {
                position = ((div * index).toInt() + paddingLeft).toFloat()
                setDrawText(canvas, index, position)
                mDividerPaint?.let {
                    canvas.drawLine(
                        position,
                        halfHeight / 4.0f * 3.0f,
                        position,
                        halfHeight / 4.0f * 5.0f,
                        it
                    )
                }
            }
        }
        mSeaLinePaint?.let {
            canvas.drawLine(
                paddingLeft.toFloat(),
                halfHeight.toFloat(),
                (width - paddingRight).toFloat(),
                halfHeight.toFloat(),
                it
            )
        }
        super.onDraw(canvas)
    }

    private fun setTypeArray(a: TypedArray) {
        a.apply {
            mActivatedEventSwitch = getBoolean(
                R.styleable.DividerSeekBar_dividerSetActivatedEvent,
                mActivatedEventSwitch
            )
            mActivateTargetValue =
                getInt(
                    R.styleable.DividerSeekBar_dividerActivatedTargetValue,
                    mActivateTargetValue
                )
            mActiveMode = getInt(R.styleable.DividerSeekBar_dividerActiveMode, mActiveMode)

            mTextLocationMode = getInt(R.styleable.DividerSeekBar_dividerTextLocationMode, mTextLocationMode)
            mTextInterval = getInt(R.styleable.DividerSeekBar_dividerTextInterval, mTextInterval)

            mTextSize = getDimension(
                R.styleable.DividerSeekBar_dividerTextSize,
                mTextSize
            )
            mTextColor = getColor(
                R.styleable.DividerSeekBar_dividerTextColor,
                mTextColor
            )

            mDividerColor = getColor(
                R.styleable.DividerSeekBar_dividerColor,
                mDividerColor
            )
            mDividerStrokeWidth = getDimension(
                R.styleable.DividerSeekBar_dividerStrokeWidth,
                mDividerStrokeWidth
            )

            mSeaLineColor = getColor(
                R.styleable.DividerSeekBar_dividerSeaLineColor,
                mSeaLineColor
            )
            mSeaLineStokeWidth = getDimension(
                R.styleable.DividerSeekBar_dividerSeaLineStrokeWidth,
                mSeaLineStokeWidth
            )

            mThumbDefaultDrawable =
                getDrawable(R.styleable.DividerSeekBar_dividerThumbDefaultDrawable)
                    ?: mThumbDefaultDrawable
            mThumbActivatedDrawable =
                getDrawable(R.styleable.DividerSeekBar_dividerThumbActivatedDrawable)
                    ?: mThumbActivatedDrawable
        }
    }

    private fun setOnDividerSeekBarChangeListener() {
        setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mIsActivated = getIsActivatedByActiveMode(progress)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun initIsActivated() {
        mIsActivated = false
    }

    /**
     * set paint
     */
    private fun setSeaLinePaint() {
        mSeaLinePaint = Paint().apply {
            style = Paint.Style.FILL_AND_STROKE
            color = mSeaLineColor
            strokeWidth = mSeaLineStokeWidth
        }
    }

    private fun setDividerPaint() {
        mDividerPaint = Paint().apply {
            style = Paint.Style.FILL_AND_STROKE
            color = mDividerColor
            strokeWidth = mDividerStrokeWidth
        }
    }

    private fun setTextPaint() {
        mTextRect = Rect()
        mTextPaint = Paint().apply {
            color = mTextColor
            textSize = mTextSize
            isAntiAlias = true
        }
    }

    private fun setDrawText(canvas: Canvas, index: Int, position: Float) {
        if (index % mTextInterval == 0) {
            val stringIndex = index.toString()
            mTextPaint?.apply {
                textAlign = Paint.Align.CENTER
                getTextBounds(stringIndex, 0, stringIndex.length, mTextRect)
            }
            mTextPaint?.let {
                canvas.drawText(
                    stringIndex,
                    position,
                    getTextLocationY(mTextLocationMode),
                    it
                )
            }
        }
    }

    private fun getTextLocationY(@TextLocation mode: Int): Float {
        return when(mode) {
            TEXT_LOCATION_BOTTOM -> height.toFloat() - resources.getDimension(R.dimen.dp_1)
            TEXT_LOCATION_TOP -> height.toFloat() - (height / 5 * 4).toFloat()
            // when other integer, set text location bottom mode
            else -> height.toFloat() - resources.getDimension(R.dimen.dp_1)
        }
    }

    private fun getIsActivatedByActiveMode(progress: Int): Boolean {
        return when(mActiveMode) {
            ACTIVE_MODE_TARGET -> progress == mActivateTargetValue
            ACTIVE_MODE_MAXIMUM -> progress <= mActivateTargetValue
            else -> progress >= mActivateTargetValue
        }
    }

    /**
     * text setting
     */
    fun setTextLocationMode(@TextLocation mode: Int) {
        mTextLocationMode = mode
        invalidate()
    }

    fun setTextInterval(interval: Int) {
        mTextInterval = interval
        invalidate()
    }

    fun setTextColor(@ColorRes textColorId: Int) {
        mTextColor = resources.getColor(textColorId)
        invalidate()
    }

    fun setTextSize(@DimenRes textSizeId: Int) {
        mTextSize = resources.getDimension(textSizeId)
    }

    fun setTextSize(textSize: Float) {
        mTextSize = textSize
    }

    /**
     * seaLine setting
     */
    fun setSeaLineColor(@ColorRes seaLineColorId: Int) {
        mSeaLineColor = resources.getColor(seaLineColorId)
        invalidate()
    }

    fun setSeaLineStrokeWidth(@DimenRes seaLineStrokeWidthId: Int) {
        mSeaLineStokeWidth = resources.getDimension(seaLineStrokeWidthId)
    }

    fun setSeaLineStrokeWidth(seaLineStrokeWidth: Float) {
        mSeaLineStokeWidth = seaLineStrokeWidth
    }

    /**
     * divider setting
     */

    fun setDividerInterval(interval: Int) {
        mDividerInterval = interval
        invalidate()
    }

    fun setDividerColor(@ColorRes dividerColorId: Int) {
        mDividerColor = resources.getColor(dividerColorId)
        invalidate()
    }

    fun setDividerStrokeWidth(@DimenRes dividerStrokeWidthId: Int) {
        mDividerStrokeWidth = resources.getDimension(dividerStrokeWidthId)
        invalidate()
    }

    fun setDividerStrokeWidth(dividerStrokeWidth: Float) {
        mDividerStrokeWidth = dividerStrokeWidth
        invalidate()
    }

    /**
     * thumb setting
     */
    fun setThumbDefaultDrawable(@DrawableRes thumbDefaultDrawableId: Int) {
        mThumbDefaultDrawable = resources.getDrawable(thumbDefaultDrawableId)
        initIsActivated()
    }

    fun setThumbDefaultDrawable(thumbDefaultDrawable: Drawable) {
        mThumbDefaultDrawable = thumbDefaultDrawable
    }

    fun setThumbActivatedDrawable(@DrawableRes thumbActivatedDrawableId: Int) {
        mThumbActivatedDrawable = resources.getDrawable(thumbActivatedDrawableId)
        initIsActivated()
    }

    fun setThumbActivatedDrawable(thumbActivatedDrawable: Drawable) {
        mThumbActivatedDrawable = thumbActivatedDrawable
    }

    /**
     * set active target & mode
     */
    fun setActivateTargetValue(targetValue: Int) {
        mActivateTargetValue = targetValue
    }

    fun setActiveMode(@ActiveMode mode: Int) {
        mActiveMode = mode
    }

    fun setOffActivatedEvent() {
        mActivatedEventSwitch = false
    }

    fun setOnActivatedEvent() {
        mActivatedEventSwitch = true
    }

    interface OnDividerSeekBarChangeStateListener {

        fun onProgressEnabled(dividerSeekBar: DividerSeekBar, progress: Int)

        fun onProgressDisabled(dividerSeekBar: DividerSeekBar, progress: Int)

    }

    fun setOnDividerSeekBarChangeStateListener(listener: OnDividerSeekBarChangeStateListener) {
        mOnDividerSeekBarChangeStateListener = listener
    }

}
