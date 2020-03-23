package com.luis.rodrigo.abasto.android.mianuncio.mianuncio.repositorio.bbdd

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.luis.rodrigo.abasto.android.mianuncio.mianuncio.repositorio.bbdd.daos.UsuarioDao
import com.luis.rodrigo.abasto.android.mianuncio.mianuncio.repositorio.bbdd.entidades.Usuario


@Database(entities = [(Usuario::class)],version = 1,exportSchema = true)
abstract class MiAnuncioRoomDatabase : RoomDatabase(){

    abstract fun usuarioDao(): UsuarioDao

    companion object{
        private var INSTANCE:MiAnuncioRoomDatabase?=null
    }
    @Synchronized
    fun getInstance(context: Context): MiAnuncioRoomDatabase?{
        if(INSTANCE==null){
         INSTANCE= Room.databaseBuilder(context.applicationContext,
         MiAnuncioRoomDatabase::class.java,"mi_anuncio.db")
             .allowMainThreadQueries()
             .build()
        }
        return INSTANCE
    }
    fun destroyInstance(){
        INSTANCE=null
    }
}