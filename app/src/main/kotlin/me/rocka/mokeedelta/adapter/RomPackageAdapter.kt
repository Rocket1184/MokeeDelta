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

class RomPackageAdapter(
        val pkgList: List<IRomPackage>
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
            setVariable(BR.pkg, pkg)
            executePendingBindings()
        }
    }

    override fun getItemCount() = pkgList.size
}
