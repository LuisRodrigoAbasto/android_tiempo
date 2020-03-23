package com.luis.rodrigo.abasto.android.mianuncio.mianuncio.workers

import androidx.work.Worker
import com.luis.rodrigo.abasto.android.mianuncio.mianuncio.utils.L


class SubirFicheroWorker : Worker() {
    override fun doWork() : WorkerResult {
return try {
            L.d("Estamos Subiendo un Fichero :-P")
            WorkerResult.SUCCESS
        }
        catch (ex: Exception)
            {
            WorkerResult.FAILURE
            }
    }
}