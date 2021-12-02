package mas.com.health_diary.ui.health

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import kotlinx.android.synthetic.main.activity_health.*
import mas.com.health_diary.R
import mas.com.health_diary.data.entity.Health
import mas.com.health_diary.ui.base.BaseActivity
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


class HealthActivity : BaseActivity<HealthData>() {

    private var currentTime = Date()

    companion object {
        private val EXTRA_HEALTH = HealthActivity::class.java.name + "extra.HEALTH"
        private const val DATE_FORMAT = "yyyy.MM.dd HH:mm"
        private const val DATE_SMALL_FORMAT = "yyyy.MM.dd"
        private const val TIME_FORMAT = "HH:mm"

        fun start(context: Context, healthId: String? = null) = Intent(
            context,
            HealthActivity::class.java
        ).run {
            healthId?.let {
                putExtra(EXTRA_HEALTH, healthId)
            }
            context.startActivity(this)
        }
    }

    private var health: Health? = null

    override val viewModel: HealthViewModel by viewModel()
    override val layoutRes = R.layout.activity_health

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val healthId = intent.getStringExtra(EXTRA_HEALTH)

        healthId?.let { id ->
            viewModel.loadHealth(id)
        } ?: let {
            supportActionBar?.title = getString(R.string.new_health_title)
        }
        initView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.health_menu, menu)
        return true
    }

    override fun renderData(data: HealthData) {
        if (data.isDeleted) finish()
        this.health = data.health
        currentTime = Calendar.getInstance().time
        initView()
    }

    private fun initView() {
        health?.let { healthData ->
            et_max.setText(healthData.max.toString())
            et_min.setText(healthData.min.toString())
            et_pulse.setText(healthData.pulse.toString())
            drawDateTime(healthData.dateFull)
        } ?: let {
            supportActionBar?.title = getString(R.string.new_health_title)
            drawDateTime(currentTime)
        }

        bt_save?.setOnClickListener {
            saveHealth()
            onBackPressed()
        }

        bt_date.setOnClickListener {
            val cal = Calendar.getInstance()
            health?.let {
                cal.time = it.dateFull
            }
            val dialog = DatePickerDialog(
                it.context, android.R.style.Theme_Holo_Light_Dialog,
                { datePicker, year, month, day ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, month)
                    cal.set(Calendar.DAY_OF_MONTH, day)
                    if (health == null) {
                        currentTime = cal.time
                    } else {
                        health?.dateFull = cal.time
                    }
                    drawDateTime(cal.time)

                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }

        bt_time.setOnClickListener {
            val cal = Calendar.getInstance()
            health?.let {
                cal.time = it.dateFull
            }
            val dialog = TimePickerDialog(
                it.context, android.R.style.Theme_Holo_Light_Dialog,
                { timePicker, hourOfDay, minute ->
                    cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    cal.set(Calendar.MINUTE, minute)
                    if (health == null) {
                        currentTime = cal.time
                    } else {
                        health?.dateFull = cal.time
                    }
                    drawDateTime(cal.time)
                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }

    }

    private fun drawDateTime(date: Date) {
        SimpleDateFormat(DATE_SMALL_FORMAT, Locale.getDefault()).format(date)
            .let {
                et_date.setText(it)
            }
        SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(date).let {
            et_time.setText(it)
        }
    }


    private fun saveHealth() {

        health = health?.copy(
            dateFull = health?.dateFull ?: currentTime,
            date = et_date.text.toString(),
            time = et_time.text.toString(),
            max = et_max.text.toString().toInt(),
            min = et_min.text.toString().toInt(),
            pulse = et_pulse.text.toString().toInt(),
        ) ?: Health(
            UUID.randomUUID().toString(),
            currentTime,
            et_date.text.toString(),
            et_time.text.toString(),
            et_max.text.toString().toInt(),
            et_min.text.toString().toInt(),
            et_pulse.text.toString().toInt()
        )

        health?.let {
            viewModel.save(it)
        }
    }

    private fun deleteData() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.helth_del_message))
            .setPositiveButton(R.string.health_del_ok) { _, _ ->
                viewModel.deleteHealth()
                onBackPressed()
            }
            .setNegativeButton(R.string.health_del_cancel) { dialog, _ -> dialog.dismiss() }
            .show()
    }
}