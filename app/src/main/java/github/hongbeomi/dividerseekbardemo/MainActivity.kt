package github.hongbeomi.dividerseekbardemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import github.hongbeomi.dividerseekbar.DividerSeekBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dividerSeekBar_test.apply {
            max = 10
            setTextLocationMode(DividerSeekBar.TEXT_LOCATION_TOP)
//            setTextInterval(10)
            setTextColor(R.color.black)
            setTextSize(R.dimen.sp_12)
//            setSeaLineColor(R.color.light_blue_600)
            setSeaLineStrokeWidth(R.dimen.dp_1)
//            setDividerInterval(10)
//            setDividerColor(R.color.light_blue_600)
            setDividerStrokeWidth(R.dimen.dp_1)
            setThumbDefaultDrawable(R.drawable.bg_thumb_default)
            setThumbActivatedDrawable(R.drawable.bg_thumb_activated)
            setActiveMode(DividerSeekBar.ACTIVE_MODE_MINIMUM)
            setActivateTargetValue(3)
            setOnDividerSeekBarChangeStateListener(
                object : DividerSeekBar.OnDividerSeekBarChangeStateListener {
                    override fun onProgressEnabled(dividerSeekBar: DividerSeekBar, progress: Int) {
                        textView_test.apply {
                            text = "$progress :: ON!"
                            setTextColor(resources.getColor(R.color.light_blue_600))
                        }
                    }
                    override fun onProgressDisabled(dividerSeekBar: DividerSeekBar, progress: Int) {
                        textView_test.apply {
                            text = "$progress :: OFF!"
                            setTextColor(resources.getColor(R.color.black))
                        }
                    }
                })
        }
    }

}