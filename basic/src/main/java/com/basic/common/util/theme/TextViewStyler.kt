package com.basic.common.util.theme

import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import android.widget.EditText
import android.widget.TextView
import com.widget.R
import com.basic.common.extension.getColorCompat
import com.basic.common.widget.LsEditText
import com.basic.common.widget.LsTextView
import com.basic.data.PreferencesConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TextViewStyler @Inject constructor(
    private val prefs: PreferencesConfig,
    private val colors: Colors,
    val fontProvider: FontProvider
) {

    companion object {
        const val COLOR_THEME = 0
        const val COLOR_PRIMARY_ON_THEME = 1
        const val COLOR_SECONDARY_ON_THEME = 2
        const val COLOR_TERTIARY_ON_THEME = 3

        const val SIZE_PRIMARY = 0
        const val SIZE_SECONDARY = 1
        const val SIZE_TERTIARY = 2
        const val SIZE_TOOLBAR = 3
        const val SIZE_DIALOG = 4
        const val SIZE_EMOJI = 5

        const val FONT_REGULAR = 0
        const val FONT_MEDIUM = 1
        const val FONT_SEMI = 2
        const val FONT_BOLD = 3
        const val KAUSHAN_REGULAR = 4

        fun applyEditModeAttributes(textView: TextView, attrs: AttributeSet?) {
            textView.run {
                var colorAttr = 0
                var textSizeAttr = 0

                when (this) {
                    is LsTextView -> context.obtainStyledAttributes(attrs, R.styleable.LsTextView)?.run {
                        colorAttr = getInt(R.styleable.LsTextView_textColor, -1)
                        textSizeAttr = getInt(R.styleable.LsTextView_textSize, -1)
                        recycle()
                    }

                    is LsEditText -> context.obtainStyledAttributes(attrs, R.styleable.LsEditText)?.run {
                        colorAttr = getInt(R.styleable.LsEditText_textColor, -1)
                        textSizeAttr = getInt(R.styleable.LsEditText_textSize, -1)
                        recycle()
                    }

                    else -> return
                }
                setTextColor(when (colorAttr) {
                    COLOR_PRIMARY_ON_THEME -> context.getColorCompat(R.color.textPrimaryDark)
                    COLOR_SECONDARY_ON_THEME -> context.getColorCompat(R.color.textSecondaryDark)
                    COLOR_TERTIARY_ON_THEME -> context.getColorCompat(R.color.textTertiaryDark)
                    COLOR_THEME -> context.getColorCompat(R.color.tools_theme)
                    else -> currentTextColor
                })

                textSize = when (textSizeAttr) {
                    SIZE_PRIMARY -> 16f
                    SIZE_SECONDARY -> 14f
                    SIZE_TERTIARY -> 12f
                    SIZE_TOOLBAR -> 20f
                    SIZE_DIALOG -> 18f
                    SIZE_EMOJI -> 32f
                    else -> textSize / paint.density
                }
            }
        }
    }

    fun applyAttributes(textView: TextView, attrs: AttributeSet?) {
        var colorAttr: Int
        var textSizeAttr: Int
        var textFontAttr: Int

        when (textView) {
            is LsTextView -> textView.context.obtainStyledAttributes(attrs, R.styleable.LsTextView).run {
                colorAttr = getInt(R.styleable.LsTextView_textColor, -1)
                textSizeAttr = getInt(R.styleable.LsTextView_textSize, -1)
                textFontAttr = getInt(R.styleable.LsTextView_textFont, 0)
                recycle()
            }

            is LsEditText -> textView.context.obtainStyledAttributes(attrs, R.styleable.LsEditText).run {
                colorAttr = getInt(R.styleable.LsEditText_textColor, -1)
                textSizeAttr = getInt(R.styleable.LsEditText_textSize, -1)
                textFontAttr = getInt(R.styleable.LsEditText_textFont, 0)
                recycle()
            }

            else -> return
        }

        when (colorAttr) {
            COLOR_THEME -> textView.setTextColor(colors.theme().theme)
            COLOR_PRIMARY_ON_THEME -> textView.setTextColor(colors.theme().textPrimary)
            COLOR_SECONDARY_ON_THEME -> textView.setTextColor(colors.theme().textSecondary)
            COLOR_TERTIARY_ON_THEME -> textView.setTextColor(colors.theme().textTertiary)
        }

        setTextSize(textView, textSizeAttr)
        setTextFont(textView, textFontAttr)

        if (textView is EditText) {
            val drawable = textView.resources.getDrawable(R.drawable.cursor).apply { setTint(colors.theme().theme) }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                textView.textCursorDrawable = drawable
            }
        }
    }

    /**
     * @see SIZE_PRIMARY
     * @see SIZE_SECONDARY
     * @see SIZE_TERTIARY
     * @see SIZE_TOOLBAR
     */
    private fun setTextSize(textView: TextView, textSizeAttr: Int) {
        val textSizePref = prefs.textSize.get()
        when (textSizeAttr) {
            SIZE_PRIMARY -> textView.textSize = when (textSizePref) {
                PreferencesConfig.TEXT_SIZE_SMALL -> 14f
                PreferencesConfig.TEXT_SIZE_NORMAL -> 16f
                PreferencesConfig.TEXT_SIZE_LARGE -> 18f
                PreferencesConfig.TEXT_SIZE_LARGER -> 20f
                else -> 16f
            }

            SIZE_SECONDARY -> textView.textSize = when (textSizePref) {
                PreferencesConfig.TEXT_SIZE_SMALL -> 12f
                PreferencesConfig.TEXT_SIZE_NORMAL -> 14f
                PreferencesConfig.TEXT_SIZE_LARGE -> 16f
                PreferencesConfig.TEXT_SIZE_LARGER -> 18f
                else -> 14f
            }

            SIZE_TERTIARY -> textView.textSize = when (textSizePref) {
                PreferencesConfig.TEXT_SIZE_SMALL -> 10f
                PreferencesConfig.TEXT_SIZE_NORMAL -> 12f
                PreferencesConfig.TEXT_SIZE_LARGE -> 14f
                PreferencesConfig.TEXT_SIZE_LARGER -> 16f
                else -> 12f
            }

            SIZE_TOOLBAR -> textView.textSize = when (textSizePref) {
                PreferencesConfig.TEXT_SIZE_SMALL -> 18f
                PreferencesConfig.TEXT_SIZE_NORMAL -> 20f
                PreferencesConfig.TEXT_SIZE_LARGE -> 22f
                PreferencesConfig.TEXT_SIZE_LARGER -> 26f
                else -> 20f
            }

            SIZE_DIALOG -> textView.textSize = when (textSizePref) {
                PreferencesConfig.TEXT_SIZE_SMALL -> 16f
                PreferencesConfig.TEXT_SIZE_NORMAL -> 18f
                PreferencesConfig.TEXT_SIZE_LARGE -> 20f
                PreferencesConfig.TEXT_SIZE_LARGER -> 24f
                else -> 18f
            }

            SIZE_EMOJI -> textView.textSize = when (textSizePref) {
                PreferencesConfig.TEXT_SIZE_SMALL -> 28f
                PreferencesConfig.TEXT_SIZE_NORMAL -> 32f
                PreferencesConfig.TEXT_SIZE_LARGE -> 36f
                PreferencesConfig.TEXT_SIZE_LARGER -> 40f
                else -> 32f
            }
        }
    }

    private fun setTextFont(textView: TextView, textFontAttr: Int) {
        fontProvider.get(textFontAttr) { typeFace ->
            textView.setTypeface(typeFace, textView.typeface?.style ?: Typeface.NORMAL)
        }
    }

}