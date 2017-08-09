package me.rocka.mokeedelta.activity

import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import me.rocka.mokeedelta.R
import me.rocka.mokeedelta.adapter.RomPackageAdapter
import me.rocka.mokeedelta.databinding.ActivityMainBinding
import me.rocka.mokeedelta.model.DeltaPackage
import me.rocka.mokeedelta.model.IRomPackage
import me.rocka.mokeedelta.util.BuildProp
import me.rocka.mokeedelta.util.Parser
import me.rocka.mokeedelta.util.Request
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var pkgListView: RecyclerView

    private val handleDownload = { e: IRomPackage ->
        doAsync {
            val url = Request.postKey(e.key)
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(toolbar)
        pkgListView = find<RecyclerView>(R.id.main_package_list)
        binding.currentPkg = Parser.parseCurrentVersion(BuildProp.get("ro.mk.version")!!)

        pkgListView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            adapter = RomPackageAdapter(ArrayList<IRomPackage>(), handleDownload)
        }

        fab.setOnClickListener {
            doAsync {
                val html = Request.get(Request.deviceLink(Build.DEVICE))
                val pkgList = Parser.parseFullPkg(html!!)
                val deltaList = ArrayList<DeltaPackage>()
                pkgList.filter { it.version.toLong() > binding.currentPkg.version.toLong() }
                        .forEach {
                            doAsync {
                                val tmpHtml = Request.get(it.deltaUrl)
                                val tmpList = Parser.parseDeltaPkg(tmpHtml!!)
                                tmpList.findLast { it.base == binding.currentPkg.version }
                                        ?.let { deltaList.add(it) }
                                uiThread { pkgListView.adapter = RomPackageAdapter(deltaList, handleDownload) }
                            }
                        }
            }
        }
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
