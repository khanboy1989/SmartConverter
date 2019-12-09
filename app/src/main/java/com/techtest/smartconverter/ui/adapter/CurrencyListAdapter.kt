package com.techtest.smartconverter.ui.adapter

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.techtest.smartconverter.R
import com.techtest.smartconverter.models.Rate
import com.techtest.smartconverter.util.format
import com.techtest.smartconverter.util.getCurrencyFlagResId
import com.techtest.smartconverter.util.getCurrencyNameResId
import kotlinx.android.synthetic.main.item_currency_convert_list.view.*

class CurrencyListAdapter(private val onAmountChangedListener: OnAmountChangedListener):RecyclerView.Adapter<CurrencyListAdapter.CurrencyListViewHolder>(){

    private val items = mutableListOf<Rate>()
    private var amount:Float = 1.0F
    private val TAG = CurrencyListAdapter::class.java.simpleName
    fun refreshData(rateList:List<Rate>){
        this.items.clear()
        this.items.addAll(rateList)
        notifyItemRangeChanged(0, items.size - 1, amount)
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
    inner class CurrencyListViewHolder(itemView: View):RecyclerView.ViewHolder(itemView),
        TextWatcher {

        private val amountEditText = itemView.currencyAmountEditText
        private val currecyDetailTextView = itemView.currDetailTV
        private val currencyTextView  = itemView.currencyTV
        private val currencyFlag = itemView.currencyFlagIV

        fun bindData(item:Rate){
            currencyTextView.text = item.symbol
            val curFlagId = getCurrencyFlagResId(this.itemView.context,item.symbol.toLowerCase())
            val curNameResId = getCurrencyNameResId(this.itemView.context,item.symbol.toLowerCase())
            currencyFlag.setImageResource(curFlagId)
            currecyDetailTextView.text = this.itemView.context.getString(curNameResId)
            amountEditText.addTextChangedListener(this)

            if (!amountEditText.isFocused){
                amountEditText.setText((item.rate * amount).format())
            }
        }


        override fun afterTextChanged(p0: Editable?) {
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(string: CharSequence?, start: Int, before: Int, count: Int) {
            if(string.toString().isNullOrEmpty()){
                if(amountEditText.isFocused){
                    Log.d(TAG,string.toString())
                }
            }
        }

    }
}