package org.developers.missensores


import android.app.Application
import android.content.Context

class MiAplicacion : Application() {

    init {
        instancia = this
    }
    companion object {
        private var instancia: MiAplicacion?=null

        fun getApplicationContext() : Context {
            return instancia!!.applicationContext
        }
    }
}