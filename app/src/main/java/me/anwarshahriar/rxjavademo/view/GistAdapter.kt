package me.anwarshahriar.rxjavademo.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.util.ArrayList
import me.anwarshahriar.rxjavademo.model.Gist

internal class GistAdapter : RecyclerView.Adapter<GistAdapter.GistViewHolder>() {
  private var onGistItemClickListener: OnGistItemClickListener? = null
  private var gists: List<Gist>

  init {
    gists = ArrayList<Gist>()
  }

  fun setOnGistItemClickListener(onGistItemClickListener: OnGistItemClickListener) {
    this.onGistItemClickListener = onGistItemClickListener
  }

  fun setData(data: List<Gist>) {
    this.gists = data
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GistViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(android.R.layout.simple_list_item_1, parent, false)
    return GistViewHolder(view)
  }

  override fun onBindViewHolder(holder: GistViewHolder, position: Int) {
    val gist = gists[position]
    holder.bind(gist)
  }

  override fun getItemCount(): Int {
    return gists.size
  }

  inner class GistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val textGistName: TextView = itemView.findViewById(android.R.id.text1) as TextView

    fun bind(data: Gist) {
      val gistName = data.files?.entries?.iterator()?.next()?.value?.fileName
      textGistName.text = gistName
      itemView.setOnClickListener {
        onGistItemClickListener?.onGistItemClick(data)
      }
    }
  }

  interface OnGistItemClickListener {
    fun onGistItemClick(gist: Gist)
  }
}