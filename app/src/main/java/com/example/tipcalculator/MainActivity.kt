package com.example.tipcalculator

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.tipcalculator.databinding.ActivityMainBinding
import java.text.NumberFormat
import kotlin.math.ceil


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Default selection
        binding.tip15.isChecked = true

        // Listeners to auto-update output as user types/selects
        binding.billInput.addTextChangedListener { computeAndRender() }
        binding.peopleInput.addTextChangedListener { computeAndRender() }
        binding.tipGroup.setOnCheckedChangeListener { _, _ -> computeAndRender() }
        binding.roundUpSwitch.setOnCheckedChangeListener { _, _ -> computeAndRender() }

        // Initial render
        computeAndRender()
    }

    private fun computeAndRender() {
        val currency = NumberFormat.getCurrencyInstance()

        val bill = binding.billInput.text.toString().toDoubleOrNull() ?: 0.0
        val peopleRaw = binding.peopleInput.text.toString().toIntOrNull()
        val people = if (peopleRaw == null || peopleRaw < 1) 1 else peopleRaw

        val tipPercent = when (binding.tipGroup.checkedRadioButtonId) {
            binding.tip10.id -> 0.10
            binding.tip20.id -> 0.20
            else -> 0.15
        }

        val tipAmount = bill * tipPercent
        var total = bill + tipAmount

        if (binding.roundUpSwitch.isChecked) {
            total = ceil(total)
        }

        val perPerson = if (people > 0) total / people else total

        binding.tipAmount.text = "Tip: ${currency.format(tipAmount)}"
        binding.totalPerPerson.text = "Per person: ${currency.format(perPerson)}"
    }
}