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

class CurrencyListAdapter(private val onAmountChangedListener: OnAmountChangedListener) :
    RecyclerView.Adapter<CurrencyListAdapter.CurrencyListViewHolder>() {


    private val symbolPosition = ArrayList<String>()
    private val symbolRate = HashMap<String , Rate>()

    //keep default amount as reference so that we partially update the list and we do not update the first row
    private var amount: Float = 1.0F


    fun refreshData(rates:ArrayList<Rate>){
        if(symbolPosition.isEmpty()){
            symbolPosition.addAll(rates.map { it.symbol })
        }

        for (rate in rates){
            symbolRate[rate.symbol] = rate
        }

        notifyItemRangeChanged(0,symbolPosition.size -1, amount)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyListViewHolder {
        return CurrencyListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_currency_convert_list,
                parent,
                false
            )
        )
    }
    // returns the rate at position from symbol rate list
    private fun rateAtPosition(pos:Int):Rate{
        return symbolRate[symbolPosition[pos]]!!
    }
    override fun getItemCount() = symbolPosition.size

    override fun onBindViewHolder(holder: CurrencyListViewHolder, position: Int) {
        holder.bindData(rateAtPosition(position))
    }

    override fun onBindViewHolder(holder: CurrencyListViewHolder, position: Int, payloads: List<Any>) {
        if (!payloads.isEmpty()) {
            holder.bindData(rateAtPosition(position))
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    //Update the amount it is called from the activity when amount has changed
    //The list simply will update the currencies for each row
    fun updateAmount(amount: Float) {
        this.amount = amount
        //notifyItemRangeChanged is called and amount is passed as a payload
        //since we want to update recyclerview partly (we do not want to update the first row)
        notifyItemRangeChanged(0, symbolPosition.size - 1, amount)
    }

    /**
     * CurrencyListViewHolder for the currencies
     * */
    inner class CurrencyListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val amountEditText = itemView.currencyAmountEditText
        private val currencyDetailTextView = itemView.currDetailTV
        private val currencyTextView = itemView.currencyTV
        private val currencyFlag = itemView.currencyFlagIV
        var symbol:String = ""

        fun bindData(item: Rate) {
            if(this.symbol != item.symbol){
                initViewData(item)
                this.symbol = item.symbol
            }
            //if the edit text doesn't have the focus we do calculate the value
            if (!amountEditText.isFocused) {
                amountEditText.setText((item.rate * amount).format())
            }
        }
        //initialize the bind data into textview, edittext and imageview on layout
        private fun initViewData(rate:Rate){
            val symbol = rate.symbol.toLowerCase()
            val nameId = getCurrencyNameResId(itemView.context,symbol)
            val resId = getCurrencyFlagResId(itemView.context,symbol)
            currencyTextView.text = rate.symbol
            currencyFlag.setImageResource(resId)
            currencyDetailTextView.text = this.itemView.context.getString(nameId)

            amountEditText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                //If there is not focus on edittext we do not do anything
                if (!hasFocus) {
                    return@OnFocusChangeListener
                }

                //If the layoutPosition greater than zero (simply if it is not first row of recycler)
                //We simply bring the focused row to the top of the list and
                //we update the symbolPosition index so that our reference list is updated
                //After updating the list user enters the amount
                layoutPosition.takeIf { it > 0 }?.also { currentPosition ->
                    symbolPosition.removeAt(currentPosition).also {
                        symbolPosition.add(0, it)
                    }
                    //notify the recycler that view moved to position one since new currency has the focus
                    notifyItemMoved(currentPosition, 0)
                    //notify the presenter base changed and we get the latest rate at depending on latest amount entered by user
                    amountEditText.setText(amount.toString())
                    onAmountChangedListener.onAmountChanged(symbol,amount)
                }


            }


            amountEditText.addTextChangedListener(object :TextWatcher{
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(string: CharSequence?, start: Int, before: Int, count: Int) {
                    if (string.toString().isNotEmpty()) {
                        if (amountEditText.isFocused) {
                            onAmountChangedListener.onAmountChanged(symbol, string.toString().toFloat())
                        }
                    }

                }

            })
        }



    }
}