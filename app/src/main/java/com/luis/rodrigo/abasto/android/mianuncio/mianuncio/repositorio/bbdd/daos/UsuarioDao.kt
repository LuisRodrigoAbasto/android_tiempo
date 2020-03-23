package com.luis.rodrigo.abasto.android.mianuncio.mianuncio.repositorio.bbdd.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.luis.rodrigo.abasto.android.mianuncio.mianuncio.repositorio.bbdd.entidades.Usuario

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(usuario: Usuario)
    @Delete
    fun deleteUser(usuario: Usuario)

    @Query("DELETE FROM usuarios")
    fun deleteAll()

    @Query("Select email from usuarios where id_user==:id")
    fun getEmail(id:String):LiveData<String>

    @Query("Select *from usuarios")
    fun selectAll(): LiveData<List<Usuario>>

}