
package com.example.sharedofficebooking.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

// Stage 2 - Room availability interface

private val BrandBlue = Color(0xFF234F7D)
private val PageBackground = Color(0xFFF4F6F9)

private data class Room(
    val name: String,
    val type: String
)

private data class BusySlot(
    val roomName: String,
    val date: LocalDate,
    val start: LocalTime,
    val end: LocalTime
)

// Five meeting rooms and two seminar rooms
private val rooms = listOf(
    Room("Meeting Room 01", "Meeting room"),
    Room("Meeting Room 02", "Meeting room"),
    Room("Meeting Room 03", "Meeting room"),
    Room("Meeting Room 04", "Meeting room"),
    Room("Meeting Room 05", "Meeting room"),
    Room("Seminar Room 01", "Seminar room"),
    Room("Seminar Room 02", "Seminar room")
)

@Composable
fun RoomsScreen(
    onBack: () -> Unit,
    onRoomSelected: (String) -> Unit
) {
    val context = LocalContext.current

    var selectedDate by remember {
        mutableStateOf(LocalDate.now().plusDays(1))
    }

    var startTime by remember {
        mutableStateOf(LocalTime.of(10, 0))
    }

    var endTime by remember {
        mutableStateOf(LocalTime.of(11, 0))
    }

    var hasSearched by remember {
        mutableStateOf(false)
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    // Sample existing bookings for tomorrow
    val busySlots = remember {
        val tomorrow = LocalDate.now().plusDays(1)

        listOf(
            BusySlot(
                "Meeting Room 01",
                tomorrow,
                LocalTime.of(9, 30),
                LocalTime.of(11, 30)
            ),
            BusySlot(
                "Meeting Room 04",
                tomorrow,
                LocalTime.of(10, 0),
                LocalTime.of(12, 0)
            ),
            BusySlot(
                "Seminar Room 02",
                tomorrow,
                LocalTime.of(10, 0),
                LocalTime.of(11, 0)
            )
        )
    }

    // Check whether the requested time overlaps a booking
    val availableRooms = rooms.filter { room ->
        busySlots.none { booking ->
            booking.roomName == room.name &&
                    booking.date == selectedDate &&
                    startTime.isBefore(booking.end) &&
                    endTime.isAfter(booking.start)
        }
    }

    val dateFormat = DateTimeFormatter.ofPattern("dd MMM yyyy")

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = PageBackground
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 600.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                TextButton(onClick = onBack) {
                    Text("‹ Back to dashboard")
                }

                Text(
                    text = "Find a room",
                    style = MaterialTheme.typography.headlineMedium,
                    color = BrandBlue,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Choose when you need a room to view available spaces."
                )

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        Text(
                            text = "Booking details",
                            style = MaterialTheme.typography.titleMedium
                        )

                        OutlinedButton(
                            onClick = {
                                DatePickerDialog(
                                    context,
                                    { _, year, month, day ->
                                        selectedDate = LocalDate.of(
                                            year, month + 1, day
                                        )
                                        hasSearched = false
                                        errorMessage = ""
                                    },
                                    selectedDate.year,
                                    selectedDate.monthValue - 1,
                                    selectedDate.dayOfMonth
                                ).apply {
                                    datePicker.minDate =
                                        LocalDate.now()
                                            .atStartOfDay(
                                                ZoneId.systemDefault()
                                            )
                                            .toInstant()
                                            .toEpochMilli()
                                }.show()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Date: ${selectedDate.format(dateFormat)}")
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    TimePickerDialog(
                                        context,
                                        { _, hour, minute ->
                                            startTime = LocalTime.of(hour, minute)
                                            hasSearched = false
                                            errorMessage = ""
                                        },
                                        startTime.hour,
                                        startTime.minute,
                                        true
                                    ).show()
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("From: $startTime")
                            }

                            OutlinedButton(
                                onClick = {
                                    TimePickerDialog(
                                        context,
                                        { _, hour, minute ->
                                            endTime = LocalTime.of(hour, minute)
                                            hasSearched = false
                                            errorMessage = ""
                                        },
                                        endTime.hour,
                                        endTime.minute,
                                        true
                                    ).show()
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("To: $endTime")
                            }
                        }
                    }
                }

                Button(
                    onClick = {
                        errorMessage = ""

                        if (!startTime.isBefore(endTime)) {
                            errorMessage =
                                "End time must be later than start time."
                            hasSearched = false
                        } else if (
                            selectedDate.isBefore(LocalDate.now()) ||
                            (
                                    selectedDate == LocalDate.now() &&
                                            startTime.isBefore(LocalTime.now())
                                    )
                        ) {
                            errorMessage =
                                "Please select a future date and time."
                            hasSearched = false
                        } else {
                            hasSearched = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandBlue
                    )
                ) {
                    Text("Search available rooms")
                }

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                if (hasSearched) {

                    Text(
                        text = "${availableRooms.size} rooms available",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Results are based on demonstration booking data.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )

                    if (availableRooms.isEmpty()) {
                        Text(
                            "No rooms are available. Try a different time."
                        )
                    }

                    availableRooms.forEach { room ->

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {

                                Text(
                                    text = room.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(room.type)

                                Text(
                                    text = "Available",
                                    color = Color(0xFF267A45),
                                    fontWeight = FontWeight.SemiBold
                                )

                                Button(
                                    onClick = {
                                        onRoomSelected(room.name)
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Select room")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
