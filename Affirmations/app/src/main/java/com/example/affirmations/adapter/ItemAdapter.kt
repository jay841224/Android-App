package com.example.affirmations.adapter

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.affirmations.R
import com.example.affirmations.model.Affirmation

class ItemAdapter(private val context: Context, private val dataset: List<Affirmation>) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    /* 儲存 View reference 的地方 */
    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.item_title)
        val imgView: ImageView = view.findViewById(R.id.item_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // 建立 ViewHolder 的地方，如果有同時支援多種 layout 的需求，
        // 可以複寫 getItemViewType function，
        // 這個 function 就可以拿到不同的 viewType 以供我們識別。
        var adapterLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        // 因為 ViewHolder 會重複使用，
        // 我們要在這個 function 依據 position
        // 把正確的資料跟 ViewHolder 綁定在一起。
        // 可以viewHolder看作view
        var item: Affirmation = dataset[position]
        holder.textView.text = context.resources.getString(item.stringResourceId)
        holder.imgView.setImageResource(item.imgResourceId)
    }

    override fun getItemCount(): Int {
        // 回傳整個 Adapter 包含幾筆資料。
        return dataset.size
    }
}