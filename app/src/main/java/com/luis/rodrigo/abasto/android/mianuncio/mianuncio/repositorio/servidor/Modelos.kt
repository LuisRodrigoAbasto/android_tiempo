package com.luis.rodrigo.abasto.android.mianuncio.mianuncio.repositorio.servidor

object Modelos {
    data class ResultadoTiempoActual(val main:Main, val weather:List<Weather>)
    data class Main(val temp:Float,
    val humidity:Int,
    val pressure:Int,
    val temp_min:Float,
    val temp_max:Float)

    data class Weather(val id:Int,
    val main:String,
    val description:String,
    val icon:String)

}