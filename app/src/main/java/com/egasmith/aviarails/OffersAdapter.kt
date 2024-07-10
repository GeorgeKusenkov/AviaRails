package com.egasmith.aviarails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.egasmith.domain.models.offer.Offer

class OffersAdapter(private val offers: List<Offer>) : RecyclerView.Adapter<OffersAdapter.OfferViewHolder>() {

    class OfferViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.image)
        private val title: TextView = itemView.findViewById(R.id.title_tv)
        private val town: TextView = itemView.findViewById(R.id.town)
        private val price: TextView = itemView.findViewById(R.id.price)

        fun bind(item: Offer) {
            image.setImageResource(item.image ?: R.drawable.img_offer_default)
            title.text = item.title
            town.text = item.town
            price.text = item.price
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_offer, parent, false)
        return OfferViewHolder(view)
    }

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        holder.bind(offers[position])
    }

    override fun getItemCount() = offers.size
}