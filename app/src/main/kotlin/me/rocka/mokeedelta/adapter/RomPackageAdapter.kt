// 2017-08-05_MokeeDelta/me.rocka.mokeedelta.adapter
// rocka, 17-8-9

package me.rocka.mokeedelta.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.rocka.mokeedelta.BR
import me.rocka.mokeedelta.R
import me.rocka.mokeedelta.databinding.ItemRomPackageBinding
import me.rocka.mokeedelta.model.IRomPackage
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class RomPackageAdapter(
        private val pkgList: List<IRomPackage>,
        private val downloadPkg: (pkg: IRomPackage) -> Any
) : RecyclerView.Adapter<RomPackageAdapter.Holder>() {

    class Holder(
            v: View,
            val binding: ItemRomPackageBinding
    ) : RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
        val binding = DataBindingUtil.inflate<ItemRomPackageBinding>(
                LayoutInflater.from(parent!!.context),
                R.layout.item_rom_package,
                parent,
                false
        )
        return Holder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: Holder?, position: Int) {
        val pkg = pkgList[position]
        holder?.binding?.apply {
            buttonDownload?.setOnClickListener {
                doAsync {
                    uiThread {
                        buttonDownload.animate().alpha(0f)
                        progressCircleDownload.animate().alpha(1f)
                    }
                    downloadPkg(pkg)
                    uiThread {
                        buttonDownload.animate().alpha(1f)
                        progressCircleDownload.animate().alpha(0f)
                    }
                }
            }
            setVariable(BR.pkg, pkg)
            executePendingBindings()
        }
    }

    override fun getItemCount() = pkgList.size
}
