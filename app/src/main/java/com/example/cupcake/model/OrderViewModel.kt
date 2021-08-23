package com.example.cupcake.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private const val PRICE_PER_CUP = 2.00
private const val PRICE_FOR_SAME_DAY_DELIVERY = 3.00
class OrderViewModel : ViewModel() {
    private val _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int> get() = _quantity

    private val _flavour = MutableLiveData<String>()
    val flavour: LiveData<String> get() = _flavour

    private val _price = MutableLiveData<Double>()
    val price: LiveData<String> = Transformations.map(_price){
        NumberFormat.getCurrencyInstance().format(it)
    }

    private val _date = MutableLiveData<String>()
    val date: LiveData<String> get() = _date

    val dateOptions = getPickUpOptions()

init {
    resetOrder()
}
    fun setQuantity(numberOfCupcakes: Int) {
        _quantity.value = numberOfCupcakes
        updatePrice()
    }

    fun setFlavor(desiredFlavor: String){
        _flavour.value = desiredFlavor
    }

    fun setDate(pickUpdate: String){
        _date.value = pickUpdate
        updatePrice()
    }

    fun hasNoFlavorSet(): Boolean {
        return _flavour.value.isNullOrEmpty()
    }
    
    private fun getPickUpOptions(): List<String>{
        val options = mutableListOf<String>()
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        val calender = Calendar.getInstance()
        repeat(4 ){
            options.add(formatter.format(calender.time))
            calender.add(Calendar.DATE, 1)
        }
        return options
    }

    fun resetOrder(){
        _quantity.value = 0
        _date.value = dateOptions[0]
        _flavour.value = ""
        _price.value = 0.0
    }
    fun updatePrice(){
        var calculatedPrice = (quantity.value?: 0) * PRICE_PER_CUP
        if(_date.value== dateOptions[0] ){
            calculatedPrice += PRICE_FOR_SAME_DAY_DELIVERY
        }
        _price.value = calculatedPrice
    }
}