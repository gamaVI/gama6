package util.ui

import Sparkasse
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

@Composable
fun SparkasseItem(sparkasse: Sparkasse, onEdit: (Sparkasse)-> Unit, onDelete: () -> Unit){
    var isEditing by remember { mutableStateOf(false) }
    val editedPosel = remember { mutableStateOf(sparkasse.copy()) }
    Card(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        elevation = 4.dp
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                if (isEditing) {
                    OutlinedTextField(
                        value = editedPosel.value.componentTypeDisplay,
                        onValueChange = {
                            editedPosel.value = editedPosel.value.copy(componentTypeDisplay = it)
                        },
                        label = { Text("Unit type") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editedPosel.value.address,
                        onValueChange = {
                            editedPosel.value = editedPosel.value.copy(address = it)
                        },
                        label = { Text("Naslov") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editedPosel.value.transactionAmountM2.toString(),
                        onValueChange = {
                            editedPosel.value = editedPosel.value.copy(transactionAmountM2 = it.toDoubleOrNull() ?: 1000.0)
                        },
                        label = { Text("Cena na m2") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editedPosel.value.estimatedAmountM2.toString(),
                        onValueChange = { editedPosel.value = editedPosel.value.copy(estimatedAmountM2 = it.toDoubleOrNull() ?: 1000.0 )  },
                        label = { Text("Okvirna cena na m2") },
                        modifier = Modifier.fillMaxWidth()
                    )
//                    OutlinedTextField(
//                        value = editedPosel.value.gps.lat.toString(),
//                        onValueChange = { editedPosel.value = editedPosel.value.copy(gps = it.toDoubleOrNull() ?: 46.1236754) },
//                        label = { Text("Latitude") },
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                    OutlinedTextField(
//                        value = editedPosel.value.gps.lng.toString(),
//                        onValueChange = {
//                            editedPosel.value = editedPosel.value.copy(gps = it.toDoubleOrNull() ?: 15.12456468)
//                        },
//                        label = { Text("Longitude") },
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                    OutlinedTextField(
//                        value = editedPosel.value.transactionItemsList.toString(),
//                        onValueChange = { editedPosel.value = editedPosel.value.copy(transactionItemsList = it.toString().toList()) },
//                        label = { Text("Transaction item list") },
//                        modifier = Modifier.fillMaxWidth()
//                    )
                    OutlinedTextField(
                        value = editedPosel.value.transactionSumParcelSizes.toString(),
                        onValueChange = { editedPosel.value = editedPosel.value.copy(transactionSumParcelSizes = it.toIntOrNull() ?: 50 ) },
                        label = { Text("Transaction Sum Parcel Sizes") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editedPosel.value.transactionDate,
                        onValueChange = {
                            editedPosel.value = editedPosel.value.copy(transactionDate = it)
                        },
                        label = { Text("Transaction date") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editedPosel.value.transactionAmountGross.toString(),
                        onValueChange = {
                            editedPosel.value = editedPosel.value.copy(transactionAmountGross = it.toIntOrNull() ?: 100_000)
                        },
                        label = { Text("Transaction Amount Gross") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editedPosel.value.transaction_tax.toString(),
                        onValueChange = {
                            editedPosel.value = editedPosel.value.copy(transaction_tax = it.toDoubleOrNull() ?: 22.0)
                        },
                        label = { Text("Transaction TAX") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editedPosel.value.buildingYearBuilt.toString(),
                        onValueChange = {
                            editedPosel.value = editedPosel.value.copy(buildingYearBuilt = it.toIntOrNull() ?: 1965)
                        },
                        label = { Text("Year built") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editedPosel.value.unitRoomCount.toString(),
                        onValueChange = {
                            editedPosel.value = editedPosel.value.copy(unitRoomCount = it.toIntOrNull() ?: 3)
                        },
                        label = { Text("Unit room count") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editedPosel.value.unitRoomsSumSize.toString(),
                        onValueChange = {
                            editedPosel.value = editedPosel.value.copy(unitRoomsSumSize = it.toDoubleOrNull() ?: 85.9)
                        },
                        label = { Text("Unit room sum size") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editedPosel.value.unitRooms,
                        onValueChange = {
                            editedPosel.value = editedPosel.value.copy(unitRooms = it)
                        },
                        label = { Text("Unit rooms") },
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Text(
                        text = "Component type display: ${sparkasse.componentTypeDisplay}",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Address: ${sparkasse.address}",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Transaction amount M2: ${sparkasse.transactionAmountM2} €",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Estimated amount: ${sparkasse.estimatedAmountM2 ?: "N/A"} €",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Latitude: ${sparkasse.gps.lat}",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Longitude: ${sparkasse.gps.lng}",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Transaction item list: ${sparkasse.transactionItemsList}",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Transaction sum parcel sizes: ${sparkasse.transactionSumParcelSizes}",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Transaction date: ${sparkasse.transactionDate}",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Transaction amount gross: ${sparkasse.transactionAmountGross}",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Transaction tax: ${sparkasse.transaction_tax ?: "N/A"}",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Year built: ${sparkasse.buildingYearBuilt}",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Unit room count: ${sparkasse.unitRoomCount ?: "N/A"}",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Unit rooms sum size: ${sparkasse.unitRoomsSumSize}",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Unit rooms: ${sparkasse.unitRooms}",
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