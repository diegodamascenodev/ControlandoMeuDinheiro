package com.damascenoapps.controlandomeudinheiro.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.damascenoapps.controlandomeudinheiro.R
import com.damascenoapps.controlandomeudinheiro.fragments.DatePickerFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var spinner_recorrencia: Spinner
    lateinit var editText_dataVencimento: EditText
    lateinit var imageButton_addImage: ImageButton
    lateinit var imageView_miniatura: ImageView
    lateinit var stringArray_categoria: Spinner
    lateinit var stringArray_subcategoria: Spinner
    lateinit var stringArray_conta: Spinner
    lateinit var editText_dataLancamento: EditText
    lateinit var editText_horario: EditText
    lateinit var editText_dataPagamento: EditText
    lateinit var imageButton_takePicture : ImageButton
    lateinit var imageButton_pdf: ImageButton
    private val pickImage = 100
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar)) //Definindo a Toolbar que eu adicionei ao layout como sendo a nova ActionBar (Aquela Barra que fica na parte superior da tela)

        spinner_recorrencia = findViewById(R.id.spinner_recorrencia)
        editText_dataVencimento = findViewById(R.id.editText_dataVencimento)
        stringArray_categoria = findViewById(R.id.spinner_categoria)
        stringArray_subcategoria = findViewById(R.id.spinner_subcategoria)
        stringArray_conta = findViewById(R.id.spinner_conta)
        editText_dataLancamento = findViewById(R.id.editText_dataLancamento)
        editText_horario = findViewById(R.id.editText_horario)
        editText_dataPagamento = findViewById(R.id.editText_dataPagamento)
        imageButton_addImage = findViewById(R.id.imageButton_addImage)
        imageButton_takePicture = findViewById(R.id.imageButton_takePicture)
        imageButton_pdf = findViewById(R.id.imageButton_pdf)
        imageView_miniatura = findViewById(R.id.imageView_miniatura)


        //spinner_recorrencia - Código que faz o spinner funcionar
        ArrayAdapter.createFromResource(
            this,
            R.array.stringArray_recorrencia,
            android.R.layout.simple_spinner_item
        ).also { adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner_recorrencia.adapter = adapter
        }

        //editText_dataVencimento - Código que faz o editText funcionar com calendário
        editText_dataVencimento.setOnClickListener {
            showDatePickerDialog(R.id.editText_dataVencimento);
        }

        //spinner_categoria - Código que faz o spinner funcionar
        ArrayAdapter.createFromResource(
            this,
            R.array.stringArray_categoria,
            android.R.layout.simple_spinner_item
        ).also { adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner_categoria.adapter = adapter
        }

        //spinner_subcategoria - Código que faz o spinner funcionar
        ArrayAdapter.createFromResource(
            this,
            R.array.stringArray_subcategoria,
            android.R.layout.simple_spinner_item
        ).also { adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner_subcategoria.adapter = adapter
        }

        //spinner_conta - Código que faz o spinner conta funcionar
        ArrayAdapter.createFromResource(
            this,
            R.array.stringArray_conta,
            android.R.layout.simple_spinner_item
        ).also { adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner_conta.adapter = adapter
        }

        //editText_dataLancamento - Código que faz o editText funcionar com calendário
        editText_dataLancamento.setOnClickListener {
            showDatePickerDialog(R.id.editText_dataLancamento)
        }

        //editText_horario - Código que faz o editText funcionar selecionando um horário num relógio
        editText_horario.setOnClickListener{
             val cal = Calendar.getInstance()
             val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                 cal.set(Calendar.HOUR_OF_DAY, hour)
                 cal.set(Calendar.MINUTE, minute)
                 editText_horario.setText(SimpleDateFormat("HH:mm").format(cal.time))
             }
            TimePickerDialog(
                this,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }

        //editText_dataPagamento - Código que faz o editText funcionar com calendário
        editText_dataPagamento.setOnClickListener {
            showDatePickerDialog(R.id.editText_dataPagamento)
        }

        imageButton_addImage.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        imageButton_takePicture.setOnClickListener{
            dispatchTakePictureIntent()
        }

        imageButton_pdf.setOnClickListener {
            val intent = Intent() .setType("*/*") .setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
        }

    }

    //Função que cria e mostra o calendario, que nada mais que um fragmento (Fragment).
    //Essa função também pega a data, formata e seta ela como valor do editText, aparecendo na tela.
    private fun showDatePickerDialog(id: Int) {
        val newFragment =
            DatePickerFragment.newInstance(DatePickerDialog.OnDateSetListener { _, year, month, day ->
                // +1 because January is zero
                val selectedDate = day.toString() + " / " + (month + 1) + " / " + year

                when (id) {
                    R.id.editText_dataVencimento -> editText_dataVencimento.setText(selectedDate)
                    R.id.editText_dataLancamento -> editText_dataLancamento.setText(selectedDate)
                    R.id.editText_dataPagamento -> editText_dataPagamento.setText(selectedDate)
                }
            })
        newFragment.show(supportFragmentManager, "datePicker")
    }

    //Função que mnostra a miniatura da imagem
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Miniatura da imagem após ela ser selecionada na galeria do celular
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            imageView_miniatura.setImageURI(imageUri)
        }
        //Miniatura da foto que acabou de ser tirada pela câmera
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageView_miniatura.setImageBitmap(imageBitmap)
        }
        if (requestCode == 111 && resultCode == RESULT_OK) {

            val selectedFile = data?.data //The uri with the location of the file }

        }
    }

    val REQUEST_IMAGE_CAPTURE = 1

    //Função que chama o app nativo da câmera
    //pagina que explica o código:   https://developer.android.com/training/camera/photobasics?hl=pt-br
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    //Função que executa o que for clicado nos items do menu flutuante da Action Bar
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.itemMenu_DespesaCartao -> {
            // User chose the "Settings" item, show the app settings UI...
            true
        }

        R.id.itemMenu_GerenciarCategorias -> {
            // User chose the "Favorite" action, mark the current item
            // as a favorite...
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_flutuante_actionbar, menu)
        return true
    }

}