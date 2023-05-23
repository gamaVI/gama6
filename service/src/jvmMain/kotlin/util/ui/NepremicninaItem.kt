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
import util.objects.Listing

@Composable
fun NepremicninaItem(nepremicnina: Listing, onEdit: (Listing) -> Unit, onDelete: () -> Unit) {
    var isEditing by remember { mutableStateOf(false) }
    val editedNepremicnina = remember { mutableStateOf(nepremicnina.copy()) }
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                if (isEditing) {
                    OutlinedTextField(
                        value = editedNepremicnina.value.title,
                        onValueChange = {
                            editedNepremicnina.value = editedNepremicnina.value.copy(title = it)
                        },
                        label = { Text("Naslov") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editedNepremicnina.value.link,
                        onValueChange = {
                            editedNepremicnina.value = editedNepremicnina.value.copy(link = it)
                        },
                        label = { Text("Link") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editedNepremicnina.value.photoUrl,
                        onValueChange = {
                            editedNepremicnina.value = editedNepremicnina.value.copy(photoUrl = it)
                        },
                        label = { Text("URL slike") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editedNepremicnina.value.price.toString(),
                        onValueChange = {
                            editedNepremicnina.value = editedNepremicnina.value.copy(price = it.toIntOrNull() ?: 50_000)
                        },
                        label = { Text("Cena") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editedNepremicnina.value.size.toString(),
                        onValueChange = {
                            editedNepremicnina.value = editedNepremicnina.value.copy(size = it.toDoubleOrNull() ?: 50.0)
                        },
                        label = { Text("Velikost") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editedNepremicnina.value.year.toString(),
                        onValueChange = {
                            editedNepremicnina.value = editedNepremicnina.value.copy(year = it.toIntOrNull() ?: 2000)
                        },
                        label = { Text("Leto") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editedNepremicnina.value.seller,
                        onValueChange = {
                            editedNepremicnina.value = editedNepremicnina.value.copy(seller = it)
                        },
                        label = { Text("Prodajalec") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editedNepremicnina.value.location,
                        onValueChange = {
                            editedNepremicnina.value = editedNepremicnina.value.copy(location = it)
                        },
                        label = { Text("Lokacija") },
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Text(
                        text = nepremicnina.title,
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Link: ${nepremicnina.link}",
                        style = MaterialTheme.typography.body1,
                    )
                    Text(
                        text = "Slika: ${nepremicnina.photoUrl}",
                        style = MaterialTheme.typography.body1,
                    )
                    Text(
                        text = "Cena: ${nepremicnina.price} €",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Velikost: ${nepremicnina.size} m2",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Leto: ${nepremicnina.year}",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Prodajalec: ${nepremicnina.seller}",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Lokacija: ${nepremicnina.location}",
                        style = MaterialTheme.typography.body1
                    )
                }
            }
            Column(modifier = Modifier.wrapContentWidth(Alignment.End)) {
                if (isEditing) {
                    IconButton(onClick = {
                        isEditing = false
                        onEdit(editedNepremicnina.value)
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