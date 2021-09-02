package com.hubertpawlowski.alertphone.features.alert

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
import com.hubertpawlowski.alertphone.Constants
import com.hubertpawlowski.alertphone.R
import com.hubertpawlowski.alertphone.databinding.ActivityMainBinding
import com.hubertpawlowski.alertphone.features.onboarding.OnboardingActivity


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private var alarmSound: MediaPlayer? = null
    private var vibrator: Vibrator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        initViewModel()
        val userName: String? = getSharedPreferences(Constants.PREF_NAME,
            MODE_PRIVATE).getString(Constants.PREF_USER_NAME, "")
        val userId = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE).getLong(Constants.PREF_ID, 0L)
            viewModel.setUser(userName, userId)
        viewModel.subscribeForAlerts()
        if (intent.hasExtra(EXTRA_STATE)) {
            viewModel.updateState(getInitialViewState())
            intent.extras?.clear()
        }
        initListeners()
        initObservers()
    }

    private fun initViewModel() {
        val groupAlertSubscriber = GroupAlertSubscriber(this)
        val alertSender = AlertSender()
        val viewModelFactory = MainViewModelFactory(groupAlertSubscriber, alertSender)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
    }

    private fun initListeners() {
        binding.ivAlert.setOnClickListener {
            viewModel.updateTitle(getString(R.string.title))
            viewModel.updateMessage(binding.tvMessage.text.toString())
            alertClicked()
        }
        binding.tvMessage.doAfterTextChanged {
            viewModel.updateMessage(it.toString())
        }
        binding.ivEditGroupName.setOnClickListener {
            val intent = OnboardingActivity.newIntent(this)
            startActivity(intent)
            finish()
        }
    }

    private fun alertClicked() {
        viewModel.proceedWithAlert()
    }

    private fun initObservers() {
        viewModel.stateLiveData.observe(this, {
            viewStateChanged(it)
        })
        viewModel.groupNameLiveData.observe(this, Observer { groupName ->
            groupNameChanged(groupName)
        })
    }

    private fun viewStateChanged(viewState: MainViewState) {
        when (viewState) {
            MainViewState.STANDBY -> handleStandbyState()
            MainViewState.ALERT_SENT -> handleAlertSentState()
            MainViewState.ALERT_RECEIVED -> handleAlertReceivedState()
        }
    }

    private fun handleAlertReceivedState() {
        initVibrator()
        initAlarmSound()
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

    private fun initVibrator() {
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
    }

    private fun initAlarmSound() {
        alarmSound = MediaPlayer.create(this, R.raw.alert_sound).also {
            it.isLooping = true
            it.setVolume(100f, 100f)
        }
    }

    private fun getInitialViewState() = intent.getSerializableExtra(EXTRA_STATE) as MainViewState

    private fun groupNameChanged(groupName: String?) {
        binding.groupName = groupName
    }

    private fun setAlertButtonBackground(drawable: Int) {
        binding.ivAlert.setBackgroundResource(drawable)
    }

    private fun showMainContainer() {
        TransitionManager.beginDelayedTransition(binding.clMain)
        binding.messageContainer.visibility = View.VISIBLE
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
        alarmSound?.start()
    }

    private fun stopAlertSound() {
        alarmSound?.stop()
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
            vibrator?.vibrate(VibrationEffect.createWaveform(timings, 10))
        } else {
            vibrator?.vibrate(150)
        }
    }

    private fun stopVibratory() {
        vibrator?.cancel()
    }

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"

        @JvmStatic
        fun newIntent(
            context: Context?,
            state: MainViewState = MainViewState.STANDBY,
            userId: String = "",
        ): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(EXTRA_STATE, state)
            return intent
        }
    }
}
