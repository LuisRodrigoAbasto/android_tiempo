package com.luis.rodrigo.abasto.android.mianuncio.mianuncio.modelos

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.luis.rodrigo.abasto.android.mianuncio.mianuncio.repositorio.bbdd.MiAnuncioRoomDatabase
import com.luis.rodrigo.abasto.android.mianuncio.mianuncio.repositorio.bbdd.entidades.Usuario
import com.luis.rodrigo.abasto.android.mianuncio.mianuncio.workers.ComprimirFicheroWorker
import com.luis.rodrigo.abasto.android.mianuncio.mianuncio.workers.SubirFicheroWorker

class UserViewModel:ViewModel() {
    private lateinit var mBBDD: MiAnuncioRoomDatabase
    private var mCurrentName: MutableLiveData<String> = MutableLiveData()
    var contador: Int=0
    lateinit var mTodosUsuarios: LiveData<List<Usuario>>
    fun setUp(aplicacion:Application){
        mBBDD = MiAnuncioRoomDatabase.getInstance(aplicacion.applicationContext)!!
        mTodosUsuarios = mBBDD.usuarioDao().selectAll()

    }
    fun getCurrentName():MutableLiveData<String>{
        return mCurrentName
    }
    fun insertarDatosUsuario(usuario:Unit){

    }

    fun actualizarFicheroUsuario(){
        val workManager = WorkManager.getInstance()
        val constraints = Contrains.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val comprimirFicheroWorker = OneTimeWorkRequest.Builder(ComprimirFicheroWorker::class.java)
            .setConstraints(constraints)
            .build()
        val subirFicheroWorker = OneTimeWorkRequest.Builder(SubirFicheroWorker::class.java)
            .setConstraints(constraints)
            .build()
        workManager.beginWith(comprimirFicheroWorker).then(subirFicheroWorker).enqueue()
    }
}