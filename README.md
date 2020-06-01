<h1 align="center">DividerSeekBar</h1></br>

<h4 align="center">📐 customizable seebar with separator.</h4>

<img src="https://github.com/hongbeomi/DividerSeekBar/blob/master/demo/bg-divider-seek-bar.png" width="100%"></img>

</br>

## 🚀 how to include to your project?

![license](https://img.shields.io/github/license/hongbeomi/DividerSeekBar?color=blue&logo=apache) ![android CI](https://img.shields.io/github/workflow/status/hongbeomi/DividerSeekBar/Android%20CI?label=Android%20CI&logo=github) ![api](https://img.shields.io/badge/API-21%2B-darkgreen.svg?style=flat&logo=android)[![](https://jitpack.io/v/hongbeomi/DividerSeekBar.svg)](https://jitpack.io/#hongbeomi/DividerSeekBar)

### Gradle

Add it in your **root** build.gradle at the end of repositories:

```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

And add a this code to your **module**'s `build.gradle` file.

```groovy
dependencies {
    implementation "com.github.hongbeomi:DividerSeekBar:v1.0.3"
}
```

</br>

## 👀 Usage

### Basic Example for Kotlin

```kotlin
val dividerSeekBar = DividerSeekBar(this).apply {
  max = 100
  setTextLocationMode(DividerSeekBar.TEXT_LOCATION_BOTTOM)
  setTextInterval(10)
  setTextColor(R.color.black)
  setTextSize(R.dimen.sp_12)
  setSeaLineColor(R.color.light_blue_600)
  setSeaLineStrokeWidth(R.dimen.dp_1)
  setDividerInterval(10)
  setDividerColor(R.color.light_blue_600)
  setDividerStrokeWidth(R.dimen.dp_1)
  setThumbDefaultDrawable(R.drawable.bg_thumb_default)
  setThumbActivatedDrawable(R.drawable.bg_thumb_activated)
  setActiveMode(DividerSeekBar.ACTIVE_MODE_TARGET)
  setActivateTargetValue(3)
  setOnDividerSeekBarChangeStateListener(
    object : DividerSeekBar.OnDividerSeekBarChangeStateListener {
      override fun onProgressEnabled(dividerSeekBar: DividerSeekBar, progress: Int) {
      // do something
      }
      override fun onProgressDisabled(dividerSeekBar: DividerSeekBar, progress: Int) {
      // do something
      }
    })
}
```

</br>

### Text Location, Interval

We can set text location mode and interval. Default interval value is 1

```kotlin
dividerSeekBar.setTextLocationMode(DividerSeekBar.TEXT_LOCATION_BOTTOM) // set text location
dividerSeekBar.setTextInterval(10) // set text interval of divider 
```



|          Location: BOTTOM<br/> TEXT_LOCATION_BOTTOM          |             Location: TOP<br/>TEXT_LOCATION_TOP              |
| :----------------------------------------------------------: | :----------------------------------------------------------: |
| <img src="https://github.com/hongbeomi/DividerSeekBar/blob/master/demo/text_bottom.png" width="33%"/> | <img src="https://github.com/hongbeomi/DividerSeekBar/blob/master/demo/text_top.png" width="33%"/> |

</br>

### SeaLine Color, Stroke Width

We can change SeaLine color and stroke width

```kotlin
dividerSeekBar.setSeaLineColor(R.color.light_blue_600) // set color light_blue_600 
dividerSeekBar.setSeaLineStrokeWidth(R.dimen.dp_1) // set width 1dp
```

</br>

### Divider Color, Stroke Width, Interval

We can change the color and stroke width, interval of the divider. default divider interval value is 1

```kotlin
dividerSeekBar.setDividerInterval(10) // set divider interval 10
dividerSeekBar.setDividerColor(R.color.light_blue_600) // set color light_blue_600
dividerSeekBar.setDividerStrokeWidth(R.dimen.dp_1) // set width 1dp
```

</br>

### Thumb Drawable

We can change Thumb when Activated and when Default (Not Activated).

```kotlin
dividerSeekBar.setThumbDefaultDrawable(R.drawable.bg_thumb_default) // set default thumb
dividerSeekBar.setThumbActivatedDrawable(R.drawable.bg_thumb_activated) // set activated thumb
```

</br>

### Activated Mode & Target Value

We can set activated mode, target value. default mode is ACTIVE_MODE_MINIMUM, default target value is 0

```kotlin
dividerSeekBar.setActiveMode(DividerSeekBar.ACTIVE_MODE_TARGET) // set target mode
dividerSeekBar.setActivateTargetValue(3) // set target value
```

|         Mode : ACTIVE_MODE_TARGET<br>TargetValue : 3         |        Mode : ACTIVE_MODE_MINIMUM<br/>TargetValue : 3        |        Mode : ACTIVE_MODE_MAXIMUM<br/>TargetValue : 3        |
| :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: |
| <image src="https://github.com/hongbeomi/DividerSeekBar/blob/master/demo/target_mode.gif" width="33%"/> | <image src="https://github.com/hongbeomi/DividerSeekBar/blob/master/demo/minimum_mode.gif" width="33%"/> | <image src="https://github.com/hongbeomi/DividerSeekBar/blob/master/demo/maximum_mode.gif" width="33%"/> |

</br>

### onProgressEnabled, onProgressDisabled

<img src="https://github.com/hongbeomi/DividerSeekBar/blob/master/demo/listener_divider_seek_bar.gif" aligh="right" width="32%"/> We can hear DividerSeekBar when it is active and when it is not active, and we can listen it using the listener. 

```kotlin
dividerSeekBar.setOnDividerSeekBarChangeStateListener(
  object : DividerSeekBar.OnDividerSeekBarChangeStateListener {
    override fun onProgressEnabled(dividerSeekBar: DividerSeekBar, progress: Int) {
    // do something
    }
    override fun onProgressDisabled(dividerSeekBar: DividerSeekBar, progress: Int) {
    // do something
    }
  }
)
```

</br>

### Activate Mode Switch

We can Activate mode turn it on and off. default is on status

```kotlin
dividerSeekBar.setOffActivatedEvent() // set off
dividerSeekBar.setOnActivatedEvent() // set on
```

<br>

### XML Attribute

We can set all value in xml

```xml
<github.hongbeomi.dividerseekbar.DividerSeekBar
        android:id="@+id/dividerSeekBar_test"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:max="100"
        app:dividerActiveMode="target"
        app:dividerTextLocationMode="top"
        app:dividerTextInterval="10"
        app:dividerSeaLineStrokeWidth="@dimen/dp_1"
        app:dividerSeaLineColor="@color/gray_400"
        app:dividerTextColor="@color/gray_600"
        app:dividerStrokeWidth="@dimen/dp_1"
        app:dividerColor="@color/light_blue_600"
        app:dividerTextSize="@dimen/sp_12"
        app:dividerActivatedTargetValue="5"
        app:dividerThumbActivatedDrawable="@color/thumb_default_enabled_color"
        app:dividerThumbDefaultDrawable="@color/thumb_default_disabled_color"></github.hongbeomi.dividerseekbar.DividerSeekBar>
```

