package util.screens

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import util.ApiRequests
import util.ListingsResponse
import util.objects.Listing
import util.ui.NepremicninaItem

fun handleGetNepremicnine(apiResponse: MutableState<ListingsResponse>, pageNumber: Int = 1) {
    try {
        apiResponse.value = ApiRequests.getNepremicnine(pageNumber)
    } catch (e: Exception) {
        println(e)
    }
    println("Test connection")
}


@Composable
fun NepremicnineScreen(apiResponse: MutableState<ListingsResponse>, listings: MutableState<MutableList<Listing>>, pageNumber: MutableState<Int>) {
    var loading by remember { mutableStateOf(false) }
    val state = rememberLazyListState()

    //Handle async requests
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            "Nepremičnine",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.h2
        )
        if (loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        if (!loading && listings.value.isNotEmpty()) {
            Box(modifier = Modifier.weight(1f)) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = state
                ) {
                    item {
                        Column(modifier = Modifier.padding(16.dp)) {
                            listings.value.forEachIndexed { index, nepremicnina ->
                                NepremicninaItem(
                                    nepremicnina = nepremicnina,
                                    onDelete = {
                                        listings.value = listings.value.toMutableList().apply {
                                            removeAt(index)
                                        }
                                    },
                                    onEdit = { editedNepremicnina: Listing ->
                                        listings.value = listings.value.toMutableList().apply {
                                            this[index] = editedNepremicnina
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
                if (loading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else {
                    ExtendedFloatingActionButton(
                        text = { Text("Prikaži več") },
                        onClick = {
                            loading = true
                            coroutineScope.launch {
                                try {
                                    withContext(Dispatchers.IO) {
                                        handleGetNepremicnine(apiResponse, pageNumber.value)
                                        listings.value.addAll(apiResponse.value.listings)
                                        pageNumber.value += 1
                                    }
                                } catch (e: Exception) {
                                    println(e)
                                } finally {
                                    loading = false
                                }
                            }
                        },
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

        }
        if (!loading && listings.value.isEmpty()) {
            ExtendedFloatingActionButton(
                text = { Text("Pridobi nepremičnine") },
                onClick = {
                    loading = true
                    coroutineScope.launch {
                        try {
                            withContext(Dispatchers.IO) {
                                handleGetNepremicnine(apiResponse, pageNumber.value)
                                listings.value = apiResponse.value.listings.toMutableList()
                                pageNumber.value += 1
                            }
                        } catch (e: Exception) {
                            println(e)
                        } finally {
                            loading = false
                        }
                    }
                },
                modifier = Modifier.padding(16.dp)
            )
        }

    }
}
