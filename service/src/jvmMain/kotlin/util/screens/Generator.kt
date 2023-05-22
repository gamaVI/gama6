package util.screens

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import util.generatePosel
import util.objects.Posel
import util.ui.PoselItem

fun generatePosli(
    generate: MutableState<Boolean>,
    steviloPoslov: MutableState<String>,
    posli: MutableState<MutableList<Posel>>,
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

@Composable
fun GeneratorScreen(
    posli: MutableState<MutableList<Posel>>,
    generate: MutableState<Boolean>,
    steviloPoslov: MutableState<String>,
    pattern: Regex,
    showInputSection: MutableState<Boolean>,
    state: LazyListState
) {


    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            "Generator poslov",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.h2
        )
        if (showInputSection.value || posli.value.isEmpty()) {
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
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        generatePosli(generate, steviloPoslov, posli, showInputSection)
                    },
                    modifier = Modifier.align(Alignment.CenterVertically),
                    
                ) {
                    if (generate.value)
                        Text("Generating...")
                    else
                        Text("Generate")
                }
            }
        } else {
            Box(modifier = Modifier.weight(1f)) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = state
                ) {
                    item {
                        Column(modifier = Modifier.padding(16.dp)) {
                            posli.value.forEachIndexed { index, posel ->
                                PoselItem(
                                    posel = posel,
                                    onDelete = {
                                        posli.value = posli.value.toMutableList().apply {
                                            removeAt(index)
                                        }
                                    },
                                    onEdit = { editedPosel: Posel ->
                                        posli.value = posli.value.toMutableList().apply {
                                            this[index] = editedPosel
                                        }
                                    }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }

                VerticalScrollbar(
                    modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight().padding(start = 5.dp),
                    adapter = rememberScrollbarAdapter(scrollState = state)
                )
            }
        }

    }
}
