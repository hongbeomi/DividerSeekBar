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
            setOnDividerSeekBarChangeStateListener(object : DividerSeekBar.OnDividerSeekBarChangeStateListener {
                override fun onProgressEnabled(dividerSeekBar: DividerSeekBar, progress: Int) {
                    Toast.makeText(context, "$progress", Toast.LENGTH_SHORT).show()
                }
                override fun onProgressDisabled(dividerSeekBar: DividerSeekBar, progress: Int) {
                    Toast.makeText(context, "$progress", Toast.LENGTH_SHORT).show()
                }
            })
            setTextSize(R.dimen.sp_12)
            setSeaLineStrokeWidth(R.dimen.dp_1)
            setDividerStrokeWidth(R.dimen.dp_1)
            setActivateTargetValue(3)
            setThumbActivatedDrawable(R.drawable.bg_thumb_activated)
            setThumbDefaultDrawable(R.drawable.bg_thumb_default)
        }
    }

}