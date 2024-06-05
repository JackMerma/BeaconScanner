package com.idnp2024a.beaconscanner

class MovingAverage (private val predior: Int){
    private val window = mutableListOf<Double>()
    private var sum = 0.0

    fun next(value: Double): Double{
        if (window.size >= predior){
            sum -= window.removeAt(0)
        }
        window.add(value)
        sum += value
        return sum / window.size
    }
}