package com.example.handsfaster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class AboutActivity2 : AppCompatActivity() {

    private val descripcion = "En este caso los avances que se realizaron fueron pensando en la implementación futura del juego las cuales son :\n\n" +
            "-mostrar una indicación por pantalla la cual es de manera aleatoria (haciendo referencia al gesto pedido) en este caso serian 4" +
            " arriba (scrollup), abajo (scrolldown), derecha (swipe right) y izquierda (swipe left) estas inicialmente van a estar rotando cada"+
            "5 segundos y cada 3 rotaciones el tiempo disminuye 0.5 segundos hasta llegar a 1 segundo como tiempo mínimo\n\n" +

            "-como segundo punto es sobre el media player controlando manejo de los ajustes de audio como el volumen en preferencias, la música de fondo que solo se ejecuta dentro de la activity,"+
            "y la implementación de dos efectos de sonido en los toques que se ejecutan tocando la mitad superior de la pantalla (punto bueno) y la parte inferior (punto malo o error), y por ultimo relacionado al audio"+
            " es un aumento de velocidad de 5% cada 15 segundos en la canción de fondo con un máximo de aumento de 8 veces ósea la canción como máximo sonara al X1.8 de la velocidad original\n\n"+

            "- la detección y reconocimiento de gestos a través del GestureDetecter\n\n"+

            "Descripción de la versión 3:\n\n" +

            "Se implementaron los gestos dentro de la pantalla principal de juego -MainActivity- , dentro de esta además se implemento un"+
            " contador de errores interno que al fallar 5 veces con los gestos se tenga que reiniciar el nivel, faltaría hacer una actividad o pantalla de perdiste quieres "+
            "reiniciar para que se vea mas estético\n" +
            "\n" +
            "\n" +
            "adicional se implemento dentro de la clase de -borrador- mencionada en la versión anterior, un contador de -agitadas del celular- usando "+
            "el acelerómetro, este cuenta el movimiento en el eje X si varia en positivo o negativo en 8 unidades y luego lo vuelve a hacer en el valor contrario"+
            " ósea +8 con -8 y -8 con +8 para recién contarlo como una sola sacudida\n\n"+

            "Descripción de la versión 4:\n\n" +

            "Se implementaron los detectores de agitacion o sacudidas dentro de la pantalla principal de juego -MainActivity- , dentro de esta además se implemento un"+
            " contador del puntaje mas alto, indicador de dificultad y comprobacion de las variables de volumen, por otro lado en el sentido estetico"+
            "se agregaron imagenes representativas al ganar, perder puntos (hacerlo bien o mal) y al superar el TopScore (record local de puntaje.\n" +
            "\n" +
            "\n" +
            "En otras cosas se elimino la clase de -borrador- mencionada en la versiones anteriores, pero se uso para la implementacion de distintas formas de persistencia de datos "+
            "para logara hacer una nueva ventana de ajustes que si pudiera tener no solo persistencia de datos si no ademas el poder acceder a estos desde otra activity"+
            " y ya con eso logar terminar la implementacion completa de puntos de la versiones anteriores"





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about2)

        val longTextView = findViewById<TextView>(R.id.longTextView)
        longTextView.text = descripcion
    }
}
