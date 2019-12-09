package com.techtest.smartconverter.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.techtest.smartconverter.R
import com.techtest.smartconverter.models.Rate
import com.techtest.smartconverter.util.getCurrencyFlagResId
import com.techtest.smartconverter.util.getCurrencyNameResId
import kotlinx.android.synthetic.main.item_currency_convert_list.view.*

class CurrencyListAdapter:RecyclerView.Adapter<CurrencyListAdapter.CurrencyListViewHolder>(){

    private val items = mutableListOf<Rate>()

    fun refreshData(rateList:List<Rate>){
        this.items.clear()
        this.items.addAll(rateList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyListViewHolder {
        return CurrencyListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_currency_convert_list,parent,false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: CurrencyListViewHolder, position: Int) {
        holder.bindData(items[position])
    }


    /**
     * CurrencyListViewHolder for the currencies
     * */
    inner class CurrencyListViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        private val amountEditText = itemView.currencyAmountEditText
        private val currecyDetailTextView = itemView.currDetailTV
        private val currencyTextView  = itemView.currencyTV
        private val currencyFlag = itemView.currencyFlagIV

        fun bindData(item:Rate){
            amountEditText.setText(item.rate.toString())
            currencyTextView.text = item.symbol
            val curFlagId = getCurrencyFlagResId(this.itemView.context,item.symbol.toLowerCase())
            val curNameResId = getCurrencyNameResId(this.itemView.context,item.symbol.toLowerCase())
            currencyFlag.setImageResource(curFlagId)
            currecyDetailTextView.text = this.itemView.context.getString(curNameResId)
        }
    }
}