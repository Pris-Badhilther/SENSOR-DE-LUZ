package org.developers.missensores

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import dev.cardoso.sensores.sensores.Luz
import kotlinx.android.synthetic.main.activity_main.*
import org.developers.missensores.models.MiSensor
import org.developers.missensores.models.SensorTipo


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBarLuz.progressTintList=ColorStateList.valueOf(Color.RED)
        seekBarLuz.progressTintList=ColorStateList.valueOf(Color.RED)

        Luz.setHandler(handler)


        btnIniciar.setOnClickListener (object : View.OnClickListener{
            override fun onClick(v: View?) {

                if (!Luz.sensorExists()){
                    txtvSensorLuz.text = "No se encontro sensor de luminocidad "
                }
                else {
                    Luz.startSensor()
                }



        }})
        btnDetener.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                Luz.stopSensor()
                txtvSensorLuz.text = ""
                progressBarLuz.progress=0
                seekBarLuz.progress=0


            }})
    }
    //---Control de mensajes
    val handler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(inputMessage: Message) {

            val sensorEvent = inputMessage.obj as MiSensor

            //--- Luz
            if (sensorEvent.type == SensorTipo.LIGHT){
               txtvSensorLuz.text = "ProgressBar"
                textView2.text=sensorEvent.value.toString()

                var l= sensorEvent.value.toString().toFloat()
                if(l<=200F){
                    progressBarLuz.progress=l.toInt()
                    seekBarLuz.progress=l.toInt()
                }

            }

        }
    }



}
