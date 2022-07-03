package kr.co.so.softcapus.chptor6


import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private val remainMinutesTextView: TextView by lazy {
        findViewById(R.id.remainMinutesTextView)
    }

    private val remainSecondsTextView: TextView by lazy {
        findViewById(R.id.remainSecondeTextView)
    }

    private val seekBar: SeekBar by lazy {
        findViewById(R.id.seekBar)
    }

    private val soundPool = SoundPool.Builder().build()


    private  var tickingSound: Int? = null
    private  var BellSound: Int? = null
    private var currentCountDownTimer: CountDownTimer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViews()
        initSound()
    }

    override fun onResume() {
        super.onResume()
        soundPool.autoResume()
    }

    override fun onPause() {
        super.onPause()
        soundPool.autoPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }

    private fun bindViews() {
        seekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    //TODO("Not yet implemented")
                    if (p2) {
                        updateRemainTime(p1 * 60 * 1000L)
                    }
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                    // TODO("Not yet implemented")

                    stopCountDown()
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                    // TODO("Not yet implemented")
                    seekBar ?: return
                    if(seekBar.progress==0){
                        stopCountDown()
                    }else{
                        startCountDown()
                    }
                }
            }
        )

    }

    private fun createCountDownTimer(initMillis: Long): CountDownTimer {
        return object : CountDownTimer(initMillis, 1000L) {
            override fun onTick(p0: Long) {
                updateRemainTime(p0)
                updateSeekBar(p0)

            }

            override fun onFinish() {
                complateCountDown()

            }

        }

    }

    private fun complateCountDown(){
        updateRemainTime(0)
        updateSeekBar(0)

        soundPool.autoPause()
        BellSound?.let{
                soundid-> soundPool.play(soundid,1f,1f,0,0,1f)
        }
    }

    private fun startCountDown(){
        currentCountDownTimer = createCountDownTimer(seekBar.progress * 60 * 1000L)
        currentCountDownTimer?.start()

        tickingSound?.let {
                soundId->soundPool.play(soundId, 1F,1F,0,-1,1F)
        }
    }

    private fun stopCountDown(){
        currentCountDownTimer?.cancel()
        currentCountDownTimer = null
        soundPool.autoPause()
    }

    private fun updateRemainTime(reaminMillis: Long) {

        val remainSeconds = reaminMillis / 1000

        remainMinutesTextView.text = "%02d'".format(remainSeconds / 60)
        remainSecondsTextView.text = "%02d".format(remainSeconds % 60)
    }

    private fun updateSeekBar(reaminMillis: Long) {
        seekBar.progress = (reaminMillis / 1000 / 60).toInt()
    }


    private fun initSound() {
        tickingSound = soundPool.load(this, R.raw.timer_ticking, 1)
        BellSound = soundPool.load(this, R.raw.timer_bell, 1)
    }


}
