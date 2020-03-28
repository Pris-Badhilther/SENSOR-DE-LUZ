package org.developers.missensores.models

enum class SensorTipo(i: Int) {
    LIGHT(1),

}

class MiSensor {
    var type = SensorTipo.LIGHT
    var value = ""
}