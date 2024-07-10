package com.egasmith.aviarails.ui.fragments.home.flightoffermenu

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.egasmith.aviarails.databinding.ItemFlyingOptionTicketsInfoBinding
import com.egasmith.domain.models.ticketoffers.TicketOffers

class FlightOfferAdapter(private val flyingOffers: List<TicketOffers>): RecyclerView.Adapter<FlightOfferAdapter.FlightOfferViewHolder>() {

    class FlightOfferViewHolder(binding: ItemFlyingOptionTicketsInfoBinding): RecyclerView.ViewHolder(binding.root) {
        private val flyingStatus = binding.statusImg
        private val title = binding.titleTv
        private val flyingTime = binding.flyingTime
        private val price = binding.ticketPriceTv
        private val statusDrawable: GradientDrawable = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
        }

        init {
            flyingStatus.background = statusDrawable
        }

        fun bind(item: TicketOffers) {
            with(itemView.context) {
                statusDrawable.setColor(ContextCompat.getColor(this, item.color))
            }
            title.text = item.title
            flyingTime.text = item.timeRange
            price.text = item.price
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightOfferViewHolder {
        val binding = ItemFlyingOptionTicketsInfoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FlightOfferViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FlightOfferViewHolder, position: Int) {
        holder.bind(flyingOffers[position])
    }

    override fun getItemCount() = flyingOffers.size
}