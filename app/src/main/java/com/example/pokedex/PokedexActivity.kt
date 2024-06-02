package com.example.pokedex

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class PokedexActivity : AppCompatActivity() {

    private lateinit var tvDexNumber: TextView
    private lateinit var tvTypes: TextView
    private lateinit var tvPokemonName: TextView
    private lateinit var etPokemonName: EditText
    private lateinit var url: String
    private lateinit var queue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokedex)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvDexNumber = findViewById(R.id.tvDexNumber)
        tvTypes = findViewById(R.id.tvTypes)
        tvPokemonName = findViewById(R.id.tvPokemonName)
        etPokemonName = findViewById(R.id.etPokemonName)
        url = "https://pokeapi.co/api/v2/pokemon/"
        queue = Volley.newRequestQueue(this)

        etPokemonName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if(etPokemonName.text.isNotEmpty()){
                    getPokemon(url + etPokemonName.text.toString() + "/")
                }
            }
        })
    }

    private fun getPokemon(url: String){
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                updatePokemon(response)
            },
            { error ->
                Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
            })

        this.queue.add(jsonObjectRequest)
    }

    private fun updatePokemon(pokemon: JSONObject){
        tvPokemonName.text = pokemon.getString("name")
        tvDexNumber.text = "#".plus(pokemon.getInt("id").toString())
        val typesArray = pokemon.getJSONArray("types")
        val typesStringBuilder = StringBuilder()
        var i = 0
        while (i < typesArray.length()) {
            val typeObject = typesArray.getJSONObject(i)
            val typeName = typeObject.getJSONObject("type").getString("name")
            typesStringBuilder.append(", $typeName")
            i++
        }
        val types = if (typesStringBuilder.isNotEmpty()) {
            typesStringBuilder.delete(0, 2).toString()
        } else {
            ""
        }
        tvTypes.text = types
    }
}