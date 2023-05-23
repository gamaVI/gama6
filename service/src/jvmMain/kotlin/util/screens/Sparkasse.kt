package util.screens

import Sparkasse
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import util.ApiRequests
import util.ui.SparkasseItem

fun handleGetSparkasse(
    sparkassePosli: MutableState<MutableList<Sparkasse>>,
    dateIntervalMonths: MutableState<Int>,
    selectedUnitTypeIndex: Int,
    selectedSubTypeIndex: Int
) {
    println(dateIntervalMonths.value)
    println(selectedUnitTypeIndex)
    println(selectedSubTypeIndex)
    try {
        val response = ApiRequests.getPosli(dateIntervalMonths.value, selectedUnitTypeIndex, selectedSubTypeIndex)
        sparkassePosli.value = response.toMutableList()
    } catch (e: Exception) {
        println(e)
    }
    println("Test connection")
}

fun handleSaveSparkasse(sparkasse: MutableList<Sparkasse>) {
    try {
        ApiRequests.savePosli(sparkasse)
    } catch (e: Exception) {
        println(e)
    }
    println("Saving")
}

@Preview
@Composable
fun SparkasseScreen(
    sparkassePosli: MutableState<MutableList<Sparkasse>>,
    pattern: Regex,
    showInputSectionSparkasse: MutableState<Boolean>
) {

    var loading = remember { mutableStateOf(false) }
    val state = rememberLazyListState()

    // ------------------ Sparkasse states ------------------
    val dateIntervalMonths = remember { mutableStateOf(6) }
    val unitType = remember { mutableStateOf(listOf("Hiša", "Stanovanje")) }
    val subTypes1 = remember { mutableStateOf(listOf("Krajna", "Samostoječa", "Vmesna")) }
    val subTypes2 = remember { mutableStateOf(listOf("Garsonjera", "1-sobno", "2-sobno", "3-sobno", "4 in več sobno")) }
    val selectedUnitType = remember { mutableStateOf("Hiša") }
    val selectedSubType = remember {
        mutableStateOf(
            if (selectedUnitType.value == "Hiša") {
                subTypes1.value[0]
            } else {
                subTypes2.value[0]
            }
        )
    }

    val selectedUnitTypeIndex = unitType.value.indexOf(selectedUnitType.value)
    val selectedSubTypeIndex = if (selectedUnitType.value == "Hiša") {
        subTypes1.value.indexOf(selectedSubType.value)
    } else {
        subTypes2.value.indexOf(selectedSubType.value)
    }

    val expandedUnitType = remember { mutableStateOf(false) }
    val expandedSubType = remember { mutableStateOf(false) }

    val mTextFieldSize = remember { mutableStateOf(Size.Zero) }

    // ------------------ Handle async requests ------------------
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            "Sparkasse",
            modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.h3
        )
        if (loading.value) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        /*
        Ce so sparkassePosli prazni, potem uporabnik izbere podatke, ki jih zeli pridobiti.
        Izpise se mu input section, kjer lahko izbere stevilo mesecev in tip nepremicnine.
         */

        if (sparkassePosli.value.isEmpty() || showInputSectionSparkasse.value) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedTextField(
                    value = dateIntervalMonths.value.toString(),
                    onValueChange = {
                        if (it.matches(pattern)) dateIntervalMonths.value = it.toInt()
                    },
                    label = { Text("Število mesecev") },
                )
                CustomDropDown(
                    selectedText = selectedUnitType,
                    expanded = expandedUnitType,
                    unitType = unitType,
                    label = "Unit Type",
                    mTextFieldSize = mTextFieldSize
                )
                if (selectedUnitType.value == "Hiša") {
                    CustomDropDown(
                        selectedText = selectedSubType,
                        expanded = expandedSubType,
                        unitType = subTypes1,
                        label = "Sub Type",
                        mTextFieldSize = mTextFieldSize
                    )
                } else {
                    CustomDropDown(
                        selectedText = selectedSubType,
                        expanded = expandedSubType,
                        unitType = subTypes2,
                        label = "Sub Type",
                        mTextFieldSize = mTextFieldSize
                    )
                }
            }

            /*
            Gumb za pridoibvanje poslov.
             */
            ExtendedFloatingActionButton(
                text = { Text("Pridobi posle") },
                onClick = {
                    loading.value = true
                    coroutineScope.launch {
                        try {
                            withContext(Dispatchers.IO) {
                                handleGetSparkasse(
                                    sparkassePosli,
                                    dateIntervalMonths,
                                    selectedUnitTypeIndex,
                                    selectedSubTypeIndex
                                )
                                sparkassePosli.value = sparkassePosli.value
                                println(sparkassePosli.value)
                            }
                        } catch (e: Exception) {
                            println(e)
                        } finally {
                            loading.value = false
                            showInputSectionSparkasse.value = false
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(16.dp)
                    .width(150.dp),
            )

        } else {
            /*
            Izpise se seznam poslov, ki jih je uporabnik pridobil.
            */
            Box(modifier = Modifier.weight(1f)) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = state
                ) {
                    item {
                        Column(modifier = Modifier.padding(16.dp)) {
                            sparkassePosli.value.forEachIndexed { index, posel ->
                                SparkasseItem(sparkasse = posel)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(start = 10.dp)
                ) {
                    /*
                    Gumb, ki izbrise vse posle.
                     */
                    ExtendedFloatingActionButton(
                        text = { Text("Clear") },
                        onClick = {
                            sparkassePosli.value.clear()
                            showInputSectionSparkasse.value = true
                        },
                        modifier = Modifier
                            .padding(16.dp)
                            .width(150.dp),
                        backgroundColor = MaterialTheme.colors.error,
                    )

                    /*
                    Gumb, ki shrani vse posle v bazo.
                     */
                    ExtendedFloatingActionButton(
                        text = { Text("Save") },
                        onClick = {
                            loading.value = true
                            coroutineScope.launch {
                                try {
                                    withContext(Dispatchers.IO) {
                                        handleSaveSparkasse(sparkassePosli.value)
                                    }
                                } catch (e: Exception) {
                                    println(e)
                                } finally {
                                    loading.value = false
                                }
                            }
                        },
                        modifier = Modifier
                            .padding(16.dp)
                            .width(150.dp),
                    )
                }
            }
        }
    }
}


@Composable
fun CustomDropDown(
    selectedText: MutableState<String>,
    expanded: MutableState<Boolean>,
    unitType: MutableState<List<String>>,
    label: String,
    mTextFieldSize: MutableState<Size>
) {
    val icon = if (expanded.value) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown

    Column {
        OutlinedTextField(
            value = selectedText.value,
            onValueChange = { selectedText.value = it },
            modifier = Modifier
                .clickable { expanded.value = !expanded.value }
                .onGloballyPositioned { coordinates ->
                    mTextFieldSize.value = coordinates.size.toSize()
                },
            label = { Text(label) },
            trailingIcon = {
                Icon(
                    icon,
                    "contentDescription",
                    Modifier.clickable { expanded.value = !expanded.value }
                )
            }
        )

        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier
                .offset(y = 8.dp)
                .width(with(LocalDensity.current) { mTextFieldSize.value.width.toDp() }) // Match the width of TextField
        ) {
            unitType.value.forEach { unit ->
                DropdownMenuItem(onClick = {
                    selectedText.value = unit
                    expanded.value = false
                }) {
                    Text(text = unit)
                }
            }
        }
    }
}
