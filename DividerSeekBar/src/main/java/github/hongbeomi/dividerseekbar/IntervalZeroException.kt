package github.hongbeomi.dividerseekbar

import java.lang.Exception

class IntervalZeroException: Exception() {
    override val message: String?
        get() = "Interval Can't be Zero"
}