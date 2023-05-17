package util.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import util.objects.Posel

@Composable
fun PoselItem(posel: Posel, onEdit: (Posel) -> Unit, onDelete: () -> Unit) {
    var isEditing by remember { mutableStateOf(false) }
    val editedPosel = remember { mutableStateOf(posel.copy()) }
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = 4.dp
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                if (isEditing) {
                    OutlinedTextField(
                        value = editedPosel.value.skupnaPovrsina.toString(),
                        onValueChange = {
                            editedPosel.value = editedPosel.value.copy(skupnaPovrsina = it.toDoubleOrNull() ?: 50.0)
                        },
                        label = { Text("Skupna površina") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editedPosel.value.cena.toString(),
                        onValueChange = {
                            editedPosel.value = editedPosel.value.copy(cena = it.toIntOrNull() ?: 60_000)
                        },
                        label = { Text("Cena") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editedPosel.value.cenaNaM2.toString(),
                        onValueChange = {
                            editedPosel.value = editedPosel.value.copy(cenaNaM2 = it.toIntOrNull() ?: 500)
                        },
                        label = { Text("Cena na m2") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editedPosel.value.lokacija,
                        onValueChange = { editedPosel.value = editedPosel.value.copy(lokacija = it) },
                        label = { Text("Lokacija") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editedPosel.value.datumPosla,
                        onValueChange = { editedPosel.value = editedPosel.value.copy(datumPosla = it) },
                        label = { Text("Datum posla") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editedPosel.value.letoGradnje.toString(),
                        onValueChange = {
                            editedPosel.value = editedPosel.value.copy(letoGradnje = it.toIntOrNull() ?: 1950)
                        },
                        label = { Text("Leto gradnje") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editedPosel.value.tipPosla,
                        onValueChange = { editedPosel.value = editedPosel.value.copy(tipPosla = it) },
                        label = { Text("Tip posla") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editedPosel.value.tipNepremicnine,
                        onValueChange = { editedPosel.value = editedPosel.value.copy(tipNepremicnine = it) },
                        label = { Text("Tip nepremicnine") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editedPosel.value.koordinateX.toString(),
                        onValueChange = {
                            editedPosel.value = editedPosel.value.copy(koordinateX = it.toDoubleOrNull() ?: 46.0)
                        },
                        label = { Text("Koordinata X") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editedPosel.value.koordinateY.toString(),
                        onValueChange = {
                            editedPosel.value = editedPosel.value.copy(koordinateY = it.toDoubleOrNull() ?: 14.0)
                        },
                        label = { Text("Koordinata Y") },
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Text(
                        text = "Skupna površina: ${posel.skupnaPovrsina}",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Cena: ${posel.cena} €",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Cena na m2: ${posel.cenaNaM2} €",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Lokacija: ${posel.lokacija}",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Datum posla: ${posel.datumPosla}",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Leto gradnje: ${posel.letoGradnje}",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Tip posla: ${posel.tipPosla}",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Tip nepremičnine: ${posel.tipNepremicnine}",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Koordinate X: ${posel.koordinateX}",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Koordinate Y: ${posel.koordinateY}",
                        style = MaterialTheme.typography.body1
                    )
                }
            }

            Column(modifier = Modifier.wrapContentWidth(Alignment.End)) {
                if (isEditing) {
                    IconButton(onClick = {
                        isEditing = false
                        onEdit(editedPosel.value)
                    }
                    ) {
                        Icon(Icons.Default.Done, contentDescription = "Done")
                    }
                    IconButton(onClick = { isEditing = false }) {
                        Icon(Icons.Default.Clear, contentDescription = "Discard")
                    }
                } else {
                    IconButton(onClick = { isEditing = true }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }
        }
    }
}

