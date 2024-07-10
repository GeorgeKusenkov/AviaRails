package com.egasmith.aviarails.ui.fragments.home.alltickets

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.egasmith.aviarails.databinding.ItemTicketCardWithBadgeBinding
import com.egasmith.aviarails.databinding.ItemTicketCardWithoutBadgeBinding
import com.egasmith.domain.models.uiticket.UiTicket

private const val VIEW_TYPE_WITH_BADGE = 1
private const val VIEW_TYPE_WITHOUT_BADGE = 2

class AllTicketsAdapter (private val allTickets: List<UiTicket>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class AllTicketsViewHolderWithBadge(binding: ItemTicketCardWithBadgeBinding) : RecyclerView.ViewHolder(binding.root) {
        private val price = binding.price
        private val departureTime = binding.departureTime
        private val departureAirport = binding.departureAirport
        private val arrivalTime = binding.arrivalTime
        private val arrivalAirport = binding.arrivalAirport
        private val transfer = binding.transfer
        private val badge = binding.badge
        private val travelTime = binding.travelTime

        fun bind(item: UiTicket) {
            badge.text = item.badge
            price.text = item.price
            departureTime.text = item.departureDate
            departureAirport.text = item.departureAirport
            arrivalTime.text = item.arrivalDate
            arrivalAirport.text = item.arrivalAirport
            travelTime.text = item.flyingTime
            transfer.text = if(item.hasTransfer) {
                " / Без пересадок"
            } else {
                transfer.visibility = View.GONE
                ""
            }
        }
    }

    class AllTicketsViewHolderWithoutBadge(binding: ItemTicketCardWithoutBadgeBinding) : RecyclerView.ViewHolder(binding.root) {
        private val price = binding.price
        private val departureTime = binding.departureTime
        private val departureAirport = binding.departureAirport
        private val arrivalTime = binding.arrivalTime
        private val arrivalAirport = binding.arrivalAirport
        private val transfer = binding.transfer
        private val travelTime = binding.travelTime

        fun bind(item: UiTicket) {
            price.text = item.price
            departureTime.text = item.departureDate
            departureAirport.text = item.departureAirport
            arrivalTime.text = item.arrivalDate
            arrivalAirport.text = item.arrivalAirport
            travelTime.text = item.flyingTime
            transfer.text = if(item.hasTransfer) {
                " / Без пересадок"
            } else {
                transfer.visibility = View.GONE
                ""
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_WITH_BADGE -> {
                val binding = ItemTicketCardWithBadgeBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                AllTicketsViewHolderWithBadge(binding)
            }
            VIEW_TYPE_WITHOUT_BADGE -> {
                val binding = ItemTicketCardWithoutBadgeBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                AllTicketsViewHolderWithoutBadge(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val ticket = allTickets[position]
        when (holder.itemViewType) {
            VIEW_TYPE_WITH_BADGE -> (holder as AllTicketsViewHolderWithBadge).bind(ticket)
            VIEW_TYPE_WITHOUT_BADGE -> (holder as AllTicketsViewHolderWithoutBadge).bind(ticket)
        }
    }

    override fun getItemCount() = allTickets.size

    override fun getItemViewType(position: Int): Int {
        return if (allTickets[position].badge == null) {
            VIEW_TYPE_WITH_BADGE
        } else {
            VIEW_TYPE_WITHOUT_BADGE
        }
    }
}