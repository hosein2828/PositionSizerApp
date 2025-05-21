package com.example.positionsizerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.text.KeyboardOptions

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PositionSizerUI()
                }
            }
        }
    }
}

@Composable
fun PositionSizerUI() {
    var entryPrice by remember { mutableStateOf("") }
    var stopPrice by remember { mutableStateOf("") }
    var riskAmount by remember { mutableStateOf("") }
    var leverage by remember { mutableStateOf("") }
    var positionSize by remember { mutableStateOf("") }

    fun calculatePositionSize() {
        val entry = entryPrice.toDoubleOrNull()
        val stop = stopPrice.toDoubleOrNull()
        val risk = riskAmount.toDoubleOrNull()
        val lev = leverage.toDoubleOrNull()

        if (entry != null && stop != null && risk != null && lev != null && entry != stop) {
            val stopLossPerUnit = kotlin.math.abs(entry - stop)
            val size = (risk / stopLossPerUnit) * lev
            positionSize = "${"%.2f".format(size)} USD"
        } else {
            positionSize = "Invalid input"
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Position Size Calculator", fontSize = 24.sp)

        InputField(label = "Entry Price", value = entryPrice, onValueChange = { entryPrice = it })
        InputField(label = "Stop Price", value = stopPrice, onValueChange = { stopPrice = it })
        InputField(label = "Risk Amount ($)", value = riskAmount, onValueChange = { riskAmount = it })
        InputField(label = "Leverage", value = leverage, onValueChange = { leverage = it })

        Button(onClick = { calculatePositionSize() }) {
            Text("Calculate")
        }

        Text("Position Size: $positionSize", fontSize = 18.sp)
    }
}

@Composable
fun InputField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
        visualTransformation = VisualTransformation.None
    )
}
