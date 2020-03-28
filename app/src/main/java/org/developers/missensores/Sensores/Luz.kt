package dev.cardoso.sensores.sensores

import android.app.Service
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.annotation.RequiresApi
import org.developers.missensores.MiAplicacion
import org.developers.missensores.models.MiSensor
import org.developers.missensores.models.SensorTipo


object Luz :
    HandlerThread("Luz"), SensorEventListener {
    private val TAG : String = "Luz"
    private var handler: Handler? = null
    private var sensorManager : SensorManager?= null
    private var sensor : Sensor?= null
    private var sensorExists = false
    private var sensorThread: HandlerThread? = null
    private var sensorHandler: Handler? = null


    init{
        sensorManager = (MiAplicacion.getApplicationContext().getSystemService(Service.SENSOR_SERVICE)) as SensorManager
        sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT)

        // Check sensor exists
        if (sensor != null) {
            sensorExists = true
        } else {
            sensorExists = false
        }
    }

    fun startSensor(){
        sensorThread = HandlerThread(TAG, Thread.NORM_PRIORITY)
        sensorThread!!.start()
        sensorHandler = Handler(sensorThread!!.getLooper()) //Blocks until looper is prepared, which is fairly quick
        sensorManager!!.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL, sensorHandler)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun stopSensor(){
        sensorManager!!.unregisterListener(this)
        sensorThread!!.quitSafely()
    }

    fun sensorExists() : Boolean{
        return sensorExists
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d(TAG, ""+accuracy)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null && event.values.isNotEmpty()) {
            // ground!!.updateMe(event.values[1] , event.values[0])
            //Log.d(TAG, ""+event.values[1]+", "+event.values[0])
            //Log.d(TAG, "onSensorChanged: "+event.values[0])

            var msgEvent = MiSensor()
            msgEvent.type = SensorTipo.LIGHT
            msgEvent.value = event.values[0].toString()

            // Send message to MainActivity
            sendMessage(msgEvent)
        }
    }

    fun setHandler(handler: Handler){
        this.handler = handler
    }

    fun sendMessage(sensorEvent: MiSensor) {
        if (handler == null) return

        handler?.obtainMessage(sensorEvent.type.ordinal, sensorEvent)?.apply {
            sendToTarget()
        }
    }
}