package com.example.alertphone.features.alert

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionManager
import com.example.alertphone.R
import com.example.alertphone.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var alarmSound: MediaPlayer
    private lateinit var vibrator: Vibrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        initViewModel()
        initVibrator()
        initAlarmSound()
        viewModel.subscribeForAlerts()
        viewModel.updateState(getInitialViewState())
        binding.tvMessage.doAfterTextChanged {
            viewModel.updateMessage(it.toString())
        }
        viewModel.stateLiveData.observe(this, {
            viewStateChanged(it)
        })
        viewModel.groupNameLiveData.observe(this, Observer { groupName ->
            groupNameChanged(groupName)
        })
    }

    private fun initViewModel() {
        val groupAlertSubscriber = GroupAlertSubscriber(this)
        val alertSender = AlertSender()
        val viewModelFactory = MainViewModelFactory(groupAlertSubscriber, alertSender)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
    }

    private fun initVibrator() {
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
    }

    private fun initAlarmSound() {
        alarmSound = MediaPlayer.create(this, R.raw.censor_beep_sound).also {
            it.isLooping = true
            it.setVolume(100f, 100f)
        }
    }

    private fun getInitialViewState() = intent.getSerializableExtra(EXTRA_STATE) as MainViewState

    private fun viewStateChanged(viewState: MainViewState) {
        when (viewState) {
            MainViewState.STANDBY -> handleStandbyState()
            MainViewState.ALERT_SENT -> handleAlertSentState()
            MainViewState.ALERT_RECEIVED -> handleAlertReceivedState()
        }
    }

    private fun handleAlertReceivedState() {
        setAlertButtonBackground(R.drawable.bg_iv_call_icon_received)
        hideMainContainer()
        startAlert()
    }

    private fun handleAlertSentState() {
        showMainContainer()
        setAlertButtonBackground(R.drawable.bg_iv_call_icon_sent)
    }

    private fun handleStandbyState() {
        setAlertButtonBackground(R.drawable.bg_iv_call_icon_standby)
        showMainContainer()
        stopAlert()
    }

    private fun groupNameChanged(groupName: String?) {
        binding.groupName = groupName
        binding.ivAlert.setOnClickListener { alertClicked() }
    }

    private fun alertClicked() {
        viewModel.proceedWithAlert()
    }

    private fun setAlertButtonBackground(drawable: Int) {
        binding.ivAlert.setBackgroundResource(drawable)
    }

    private fun hideMainContainer() {
        binding.messageContainer.visibility = View.GONE
    }

    private fun stopAlert() {
        stopAlertButtonAnimation()
        stopAlertSound()
        stopVibratory()
    }

    private fun startAlert() {
        startAlertButtonAnimation()
        startAlertSound()
        startVibrator()
    }

    private fun startAlertSound() {
        alarmSound.start()
    }

    private fun stopAlertSound() {
        alarmSound.stop()
    }

    private fun startAlertButtonAnimation() {
        val animation = SpinAnimation(50.0f, 600, -1)
        binding.ivAlert.startAnimation(animation)
    }

    private fun stopAlertButtonAnimation() {
        binding.ivAlert.clearAnimation()
    }

    private fun startVibrator() {
        if (Build.VERSION.SDK_INT >= 26) {
            val timings = LongArray(100) { i -> 200 }
            vibrator.vibrate(VibrationEffect.createWaveform(timings, 10))
        } else {
            vibrator.vibrate(150)
        }
    }

    private fun stopVibratory() {
        vibrator.cancel()
    }

    private fun showMainContainer() {
        TransitionManager.beginDelayedTransition(binding.clMain)
        binding.messageContainer.visibility = View.VISIBLE
    }

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
        const val TITLE_MEDICAl = "Emergency"

        @JvmStatic
        fun newIntent(context: Context?, state: MainViewState = MainViewState.STANDBY): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(EXTRA_STATE, state)
            return intent
        }
    }
}
