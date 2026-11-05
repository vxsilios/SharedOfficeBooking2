
package com.example.sharedofficebooking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sharedofficebooking.ui.theme.SharedOfficeBookingTheme
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

// Exercise 5 - Stage 1: Employee Login and Dashboard
// UI prototype only - no backend authentication.

private val BrandBlue = Color(0xFF234F7D)
private val PageBackground = Color(0xFFF4F6F9)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SharedOfficeBookingTheme {
                SpaceBookApp()
            }
        }
    }
}

// Main App Navigation

@Composable
fun SpaceBookApp() {

    var signedIn by rememberSaveable {
        mutableStateOf(false)
    }

    var currentPage by rememberSaveable {
        mutableStateOf("home")
    }

    if (!signedIn) {

        EmployeeLoginScreen(
            onLogin = {
                signedIn = true
                currentPage = "home"
            }
        )

    } else {

        when (currentPage) {

            "rooms" -> ComingNextScreen(
                title = "Find a room",
                description = "Room availability search will be implemented in Stage 2.",
                onBack = {
                    currentPage = "home"
                }
            )

            "bookings" -> ComingNextScreen(
                title = "My bookings",
                description = "Reservation history and cancellation will be implemented in Stage 4.",
                onBack = {
                    currentPage = "home"
                }
            )

            else -> EmployeeDashboardScreen(
                onFindRooms = {
                    currentPage = "rooms"
                },

                onMyBookings = {
                    currentPage = "bookings"
                },

                onLogout = {
                    signedIn = false
                }
            )
        }
    }
}

// Employee Login Screen

@Composable
fun EmployeeLoginScreen(onLogin: () -> Unit) {

    var email by rememberSaveable {
        mutableStateOf("")
    }

    var password by rememberSaveable {
        mutableStateOf("")
    }

    var showError by rememberSaveable {
        mutableStateOf(false)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = PageBackground
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding(),

            contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 480.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),

                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.plymouth_crest_background),
                    contentDescription = "University of Plymouth crest",
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Fit
                )

                Text(
                    text = "SpaceBook",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandBlue
                )

                Text(
                    text = "Shared Office Space Booking",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Employee sign in",
                    color = Color.DarkGray
                )

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),

                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        OutlinedTextField(
                            value = email,

                            onValueChange = {
                                email = it
                                showError = false
                            },

                            label = {
                                Text("Work email")
                            },

                            singleLine = true,

                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = password,

                            onValueChange = {
                                password = it
                                showError = false
                            },

                            label = {
                                Text("Password")
                            },

                            singleLine = true,

                            visualTransformation = PasswordVisualTransformation(),

                            modifier = Modifier.fillMaxWidth()
                        )

                        if (showError) {

                            Text(
                                text = "Enter an email and password to continue.",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Button(
                            onClick = {

                                if (email.isBlank() || password.isBlank()) {
                                    showError = true
                                } else {
                                    onLogin()
                                }
                            },

                            modifier = Modifier.fillMaxWidth(),

                            colors = ButtonDefaults.buttonColors(
                                containerColor = BrandBlue
                            )
                        ) {

                            Text("Continue as Employee")
                        }
                    }
                }

                Text(
                    text = "Prototype only: this screen does not authenticate real accounts.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

// Employee Dashboard

@Composable
fun EmployeeDashboardScreen(
    onFindRooms: () -> Unit,
    onMyBookings: () -> Unit,
    onLogout: () -> Unit
) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = PageBackground
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),

            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.mipmap.plymouth_crest_foreground),
                        contentDescription = "University of Plymouth crest",
                        modifier = Modifier.size(45.dp)
                    )

                    Text(
                        text = "SpaceBook",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandBlue
                    )
                }

                TextButton(onClick = onLogout) {
                    Text("Sign out")
                }
            }


            Text(
                text = "Employee dashboard",
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Book a shared office space and manage your requests."
            )

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),

                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    Text(
                        text = "OUR WORKPLACE",
                        style = MaterialTheme.typography.labelLarge,
                        color = BrandBlue
                    )

                    Text(
                        text = "7 rooms in total",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text("5 meeting rooms  •  2 seminar rooms")

                    Text(
                        text = "Search by date and time to see which rooms can be booked."
                    )
                }
            }

            Button(
                onClick = onFindRooms,

                modifier = Modifier.fillMaxWidth(),

                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandBlue
                )
            ) {

                Text("Find an available room")
            }

            OutlinedButton(
                onClick = onMyBookings,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text("My bookings")
            }

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFEAF2FC)
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {

                    Text(
                        text = "How requests work",
                        fontWeight = FontWeight.SemiBold,
                        color = BrandBlue
                    )

                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    Text(
                        text = "Submitting a request does not mean the room is approved. Your booking status will be shown in My bookings."
                    )
                }
            }
        }
    }
}

// Placeholder Screens For Future Stages

@Composable
fun ComingNextScreen(
    title: String,
    description: String,
    onBack: () -> Unit
) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = PageBackground
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(24.dp),

            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            TextButton(
                onClick = onBack
            ) {

                Text("‹ Back to dashboard")
            }

            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = BrandBlue
            )

            Text(description)
        }
    }
}
