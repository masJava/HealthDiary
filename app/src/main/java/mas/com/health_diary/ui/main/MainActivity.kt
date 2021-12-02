package mas.com.health_diary.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_main.*
import mas.com.health_diary.R
import mas.com.health_diary.data.entity.Health
import mas.com.health_diary.ui.base.BaseActivity
import mas.com.health_diary.ui.health.HealthActivity
import mas.com.health_diary.ui.splash.SplashActivity
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity<List<Health>?>() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    lateinit var adapter: MainAdapter

    override val viewModel: MainViewModel by viewModel()

    override val layoutRes = R.layout.activity_main


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        adapter = MainAdapter { health ->
            HealthActivity.start(this, health.id)
        }
        mainRecycler.adapter = adapter
        fab.setOnClickListener {
            HealthActivity.start(this)
        }
    }

    override fun renderData(data: List<Health>?) {
        data?.let {
            adapter.healthList = it.groupBy {
                it.date
            }.toList().sortedBy {
                it.first
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean =
        MenuInflater(this).inflate(R.menu.main, menu).let { true }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.logout -> showLogoutDialog().let { true }
        else -> false
    }

    fun showLogoutDialog() {
        supportFragmentManager.findFragmentByTag(LogoutDialog.TAG) ?: LogoutDialog.createInstance {
            onLogout()
        }.show(supportFragmentManager, LogoutDialog.TAG)
    }

    fun onLogout() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                startActivity(Intent(this, SplashActivity::class.java))
                finish()
            }
    }

}