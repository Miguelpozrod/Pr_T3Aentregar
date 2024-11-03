package com.example.pr_t3a

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Patterns
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configurar los componentes principales de la interfaz
        configurarSpinnerPais()
        configurarBarraSatisfaccion()
        configurarBotonGuardar()

        // Poner el foco en el EditText de nombre al iniciar la app
        findViewById<EditText>(R.id.nameEditText).requestFocus()
    }

    private fun configurarSpinnerPais() {
        val paises = arrayOf("Seleccione un país", "España", "Portugal", "EEUU", "Uganda", "Guinea", "Rusia")
        // Crear y configurar el adaptador para el spinner
        val adaptador = ArrayAdapter(this, android.R.layout.simple_spinner_item, paises)
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Asignar el adaptador al spinner
        findViewById<Spinner>(R.id.countrySpinner).adapter = adaptador
    }

    private fun configurarBarraSatisfaccion() {
        val barraSatisfaccion = findViewById<SeekBar>(R.id.satisfactionSeekBar)
        val textoSatisfaccion = findViewById<TextView>(R.id.satisfactionTextView)
        // Listener para actualizar el texto cuando se mueve la barra
        barraSatisfaccion.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progreso: Int, fromUser: Boolean) {
                // Actualizar el texto con el valor actual de la barra
                textoSatisfaccion.text = "Nivel de Satisfacción: $progreso"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun configurarBotonGuardar() {
        // Configurar el listener del botón de guardar
        findViewById<Button>(R.id.saveButton).setOnClickListener {
            // Validar entradas antes de mostrar el resumen
            if (validarEntradas()) {
                mostrarResumenPerfil()
            }
        }
    }

    private fun validarEntradas(): Boolean {
        // Obtener los valores de los campos principales
        val nombre = findViewById<EditText>(R.id.nameEditText).text.toString()
        val apellido = findViewById<EditText>(R.id.lastNameEditText).text.toString()
        val email = findViewById<EditText>(R.id.emailEditText).text.toString()
        // Checkear que los campos obligatorios no estén vacíos
        if (nombre.isBlank() || apellido.isBlank() || email.isBlank()) {
            mostrarError("Por favor, complete todos los campos obligatorios.")
            return false
        }
        // Validar el formato del email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mostrarError("Por favor, ingrese un correo electrónico válido.")
            return false
        }

        return true
    }

    private fun mostrarError(mensaje: String) {
        // Mostrar un Snackbar con el mensaje de error
        Snackbar.make(findViewById(android.R.id.content), mensaje, Snackbar.LENGTH_LONG).show()
    }

    private fun mostrarResumenPerfil() {
        // Mostrar los datos introducidos por el usuario
        val nombre = findViewById<EditText>(R.id.nameEditText).text.toString()
        val apellido = findViewById<EditText>(R.id.lastNameEditText).text.toString()
        val email = findViewById<EditText>(R.id.emailEditText).text.toString()
        // Determinar el género seleccionado
        val genero = when (findViewById<RadioGroup>(R.id.genderRadioGroup).checkedRadioButtonId) {
            R.id.maleRadioButton -> "Masculino"
            R.id.femaleRadioButton -> "Femenino"
            R.id.otherRadioButton -> "Otro"
            else -> "No especificado"
        }
        // Obtener el país seleccionado
        val pais = findViewById<Spinner>(R.id.countrySpinner).selectedItem.toString()
        // Recopilar los hobbies seleccionados
        val hobbies = mutableListOf<String>().apply {
            if (findViewById<CheckBox>(R.id.readingCheckBox).isChecked) add("Lectura")
            if (findViewById<CheckBox>(R.id.sportsCheckBox).isChecked) add("Deporte")
            if (findViewById<CheckBox>(R.id.musicCheckBox).isChecked) add("Música")
            if (findViewById<CheckBox>(R.id.artCheckBox).isChecked) add("Arte")
        }
        // Obtener el nivel de satisfacción
        val satisfaccion = findViewById<SeekBar>(R.id.satisfactionSeekBar).progress
        // Comprobar si está suscrito al boletín
        val boletin = if (findViewById<Switch>(R.id.newsletterSwitch).isChecked) "Sí" else "No"
        // Crear el resumen con todos los datos
        val resumen = """
            Nombre: $nombre $apellido
            Email: $email
            Género: $genero
            País: $pais
            Hobbies: ${hobbies.joinToString(", ")}
            Nivel de Satisfacción: $satisfaccion
            Suscrito al Boletín: $boletin
        """.trimIndent()
        // Mostrar el resumen en el TextView
        findViewById<TextView>(R.id.profileSummaryTextView).text = resumen
    }
}
