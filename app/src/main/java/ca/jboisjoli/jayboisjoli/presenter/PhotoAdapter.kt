package ca.jboisjoli.jayboisjoli.presenter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import ca.jboisjoli.jayboisjoli.R
import ca.jboisjoli.jayboisjoli.ext.inflate
import ca.jboisjoli.jayboisjoli.ext.setImageUrl
import ca.jboisjoli.jayboisjoli.repository.model.entity.Photo
import ca.jboisjoli.jayboisjoli.ui.detail.DetailActivity
import kotlinx.android.synthetic.main.item_recycler_view.view.*
import org.jetbrains.anko.intentFor

internal class PhotoAdapter(private val context: Context,
                            private val items: MutableList<Photo>,
                            private val listener: (Photo) -> Unit) : RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
            holder.bind(items[position], listener)

    fun add(photo: Photo) {
        items.add(photo)
    }

    fun clear() {
        items.clear()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Inner class ViewHolder
    ///////////////////////////////////////////////////////////////////////////

    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_recycler_view)) {

        private lateinit var photoUrl: String

        fun bind(item: Photo, listener: (Photo) -> Unit) {
            with(itemView) {

                photoUrl = context.getString(R.string.templateUrl, item.farm, item.server, item.id, item.secret)

                imgItem.setImageUrl(photoUrl)
                imgTitle.text = item.title
            }

            //Send data to Intent.
            itemView.setOnClickListener {
                context.startActivity(context.intentFor<DetailActivity>(
                        "id" to item.id,
                        "photoUrl" to photoUrl))
            }

        }
    }
}