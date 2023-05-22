package util.ui

import Sparkasse
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SparkasseItem(sparkasse: Sparkasse){
    var isEditing by remember { mutableStateOf(false) }
    val editedPosel = remember { mutableStateOf(sparkasse.copy()) }
    Card(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Tip: ${sparkasse.componentTypeDisplay}",
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = "Naslov: ${sparkasse.address}",
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = "Cena na m2: ${sparkasse.transactionAmountM2?.toString() ?: "N/A"}" ,
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = "Ocenjena cena na m2: ${sparkasse.estimatedAmountM2.toString()}",
                )
                Text(
                    text = "X koordinata: ${sparkasse.gps.lng}",
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = "Y koordinata: ${sparkasse.gps.lat}",
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = sparkasse.transactionItemsList.toString(),
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text= "Item lists: ${sparkasse.transactionSumParcelSizes}",
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = "Datum transakcije: ${sparkasse.transactionDate}",
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = "Cena transakcije: ${sparkasse.transactionAmountGross}",
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = "Davek: ${sparkasse.transactionTax?.toString() ?: "N/A"} " ,
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = "Leto gradnje: ${sparkasse.buildingYearBuilt}",
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = "St. sob: ${sparkasse.unitRoomCount?.toString() ?: "N/A"} " ,
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = "Stevilo kvadratov: ${sparkasse.unitRoomsSumSize}",
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = "Sobe: ${sparkasse.unitRooms}",
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }

}