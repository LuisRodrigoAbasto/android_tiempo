package com.luis.rodrigo.abasto.android.mianuncio.mianuncio.repositorio.bbdd.entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName= "usuario")
data class Usuario(@PrimaryKey(autoGenerate = false) @NotNull @ColumnInfo(name="id_user") val id: String,
@NotNull val email: String
)