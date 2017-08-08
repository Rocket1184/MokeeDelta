package me.rocka.mokeedelta.activity

import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import me.rocka.mokeedelta.R
import me.rocka.mokeedelta.util.BuildProp
import org.jetbrains.anko.alert

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_settings -> true
        R.id.action_device_info -> {
            alert("""|DEVICE: ${Build.DEVICE}
                     |BOARD: ${Build.BOARD}
                     |MODEL: ${Build.MODEL}
                     |RELEASE: ${Build.VERSION.RELEASE}
                     |modVersion: ${BuildProp.get("ro.mk.version")}
                     |modType: ${BuildProp.get("ro.mk.releasetype")}""".trimMargin(),
                    getString(R.string.action_device_info)).show()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
