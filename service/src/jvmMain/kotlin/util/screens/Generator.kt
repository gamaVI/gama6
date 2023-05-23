package util.screens

import Sparkasse
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import util.ApiRequests.savePosli
import util.generatePosel
import util.ui.PoselItem

fun generatePosli(
    generate: MutableState<Boolean>,
    steviloPoslov: MutableState<String>,
    posli: MutableState<MutableList<Sparkasse>>,
    showInputSection: MutableState<Boolean>
) {
    generate.value = !generate.value
    if (generate.value) {
        for (i in 1..steviloPoslov.value.toInt()) {
            posli.value.add(generatePosel())
        }
        println(posli)
        generate.value = !generate.value
        showInputSection.value = false
    } else
        println("Stopped generating...")
}

fun handleSave(
    sparkasse: MutableList<Sparkasse>
) {
    savePosli(sparkasse)
    println("Saving...")
}

@Composable
fun GeneratorScreen(
    sparkasse: MutableState<MutableList<Sparkasse>>,
    generate: MutableState<Boolean>,
    steviloPoslov: MutableState<String>,
    pattern: Regex,
    showInputSection: MutableState<Boolean>,
    state: LazyListState
) {


    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            "Generator poslov",
            modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.h3
        )
        if (showInputSection.value || sparkasse.value.isEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 24.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                OutlinedTextField(
                    value = steviloPoslov.value,
                    onValueChange = {
                        if (it.matches(pattern)) {
                            steviloPoslov.value = it
                        }
                    },
                    label = { Text("Stevilo poslov") },
                    modifier = Modifier.weight(0.5f),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF030711),
                        focusedLabelColor = Color(0xFF030711)
                    )
                )
                Spacer(modifier = Modifier.width(16.dp))
                ExtendedFloatingActionButton(
                    text = { Text("Generiraj") },
                    onClick = {
                        generatePosli(generate, steviloPoslov, sparkasse, showInputSection)
                    },
                    modifier = Modifier.align(Alignment.CenterVertically),
                    backgroundColor = Color(0xFF030711),
                    contentColor = Color.White
                )
            }
        } else {
            Box(modifier = Modifier.weight(1f)) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = state
                ) {
                    item {
                        Column(modifier = Modifier.padding(16.dp)) {
                            sparkasse.value.forEachIndexed { index, posel ->
                                PoselItem(
                                    posel = posel,
                                    onDelete = {
                                        sparkasse.value = sparkasse.value.toMutableList().apply {
                                            removeAt(index)
                                        }
                                    },
                                    onEdit = { editedPosel: Sparkasse ->
                                        sparkasse.value = sparkasse.value.toMutableList().apply {
                                            this[index] = editedPosel
                                        }
                                    }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(start = 10.dp)

                ) {

                    ExtendedFloatingActionButton(
                        text = { Text("Osveži") },
                        onClick = {
                            sparkasse.value = mutableListOf()
                            generatePosli(generate, steviloPoslov, sparkasse, showInputSection)
                        },
                        modifier = Modifier.padding(5.dp)
                    )

                    ExtendedFloatingActionButton(
                        text = { Text("Počisti") },
                        onClick = {
                            sparkasse.value = mutableListOf()
                            showInputSection.value = true
                        },
                        modifier = Modifier.padding(5.dp),
                        backgroundColor = MaterialTheme.colors.error,
                    )
                    ExtendedFloatingActionButton(
                        text = { Text("Shrani") },
                        onClick = {
                            handleSave(sparkasse.value)
                        },
                        modifier = Modifier.padding(5.dp),
                        backgroundColor = Color(0xFF00D100),
                    )
                }

                VerticalScrollbar(
                    modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight().padding(start = 5.dp),
                    adapter = rememberScrollbarAdapter(scrollState = state)
                )
            }
        }

    }
}
