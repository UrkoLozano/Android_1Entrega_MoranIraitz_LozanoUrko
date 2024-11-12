package com.example.walletchecker

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyWalletCheckerTheme {
                val navController = rememberNavController()
                NavigationGraph(navController = navController)
            }
        }
    }
}

// ViewModel para manejar las notas y el historial de direcciones verificadas
class NotesViewModel : androidx.lifecycle.ViewModel() {
    val notesList: MutableList<String> = mutableListOf()
    val verifiedWalletAddresses: MutableList<String> = mutableListOf() // Historial de direcciones verificadas
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun NavigationGraph(navController: NavHostController) {
    val notesViewModel: NotesViewModel = viewModel() // Usamos el ViewModel para las notas y el historial
    NavHost(
        navController = navController,
        startDestination = "entry_screen"
    ) {
        composable("entry_screen") { EntryScreen(navController = navController) }
        composable("wallet_checker") { WalletCheckerScreen(navController = navController, notesViewModel = notesViewModel) }
        composable("show_notes") { ShowNotesScreen(navController = navController, notesViewModel = notesViewModel) }
        composable("add_note") { AddNoteScreen(navController = navController, notesViewModel = notesViewModel) }
        composable("history") { HistoryScreen(navController = navController, notesViewModel = notesViewModel) } // Pantalla de historial
    }
}

@Composable
fun EntryScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to Wallet Checker", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.navigate("wallet_checker") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Entry")
        }
    }
}

@Composable
fun WalletCheckerScreen(navController: NavHostController, notesViewModel: NotesViewModel) {
    var walletAddress by remember { mutableStateOf(TextFieldValue("")) }
    var isValid by remember { mutableStateOf<Boolean?>(null) }
    var verificationCount by remember { mutableIntStateOf(0) }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Wallet Checker", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = walletAddress,
                onValueChange = { walletAddress = it },
                label = { Text("Enter Wallet Address") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                isValid = isValidWallet(walletAddress.text)
                verificationCount += 1
                if (isValid == true) {
                    notesViewModel.verifiedWalletAddresses.add(walletAddress.text) // Añadir al historial
                }
            }) {
                Text("Check Wallet")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar la dirección y su estado de validación
            AnimatedVisibility(visible = isValid != null) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Address: ${walletAddress.text}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (isValid == true) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isValid == true) "Valid Wallet Address" else "Invalid Wallet Address",
                        color = if (isValid == true) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para navegar a la pantalla de notas
            Button(
                onClick = { navController.navigate("show_notes") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Show Notes")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para navegar a la pantalla de agregar nota
            Button(
                onClick = { navController.navigate("add_note") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Add Note")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para navegar al historial de direcciones verificadas
            Button(
                onClick = { navController.navigate("history") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("View Verification History")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                walletAddress = TextFieldValue("")
                isValid = null
            }) {
                Text("Reset")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Addresses verified: $verificationCount")
        }
    }
}

@Composable
fun ShowNotesScreen(navController: NavHostController, notesViewModel: NotesViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text("Notes", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar las notas
        notesViewModel.notesList.forEach { note ->
            Text(
                text = note,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para volver a la pantalla anterior
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}

@Composable
fun AddNoteScreen(navController: NavHostController, notesViewModel: NotesViewModel) {
    var noteText by remember { mutableStateOf(TextFieldValue("")) }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Add Note", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = noteText,
                onValueChange = { noteText = it },
                label = { Text("Enter Note for Wallet") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                notesViewModel.notesList.add(noteText.text) // Añadimos la nota al ViewModel
                navController.popBackStack() // Regresamos a la pantalla anterior
            }) {
                Text("Save Note")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.popBackStack() }) {
                Text("Back")
            }
        }
    }
}

@Composable
fun HistoryScreen(navController: NavHostController, notesViewModel: NotesViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text("Verification History", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar las direcciones verificadas
        if (notesViewModel.verifiedWalletAddresses.isEmpty()) {
            Text("No verified wallet addresses.", style = MaterialTheme.typography.bodyMedium)
        } else {
            notesViewModel.verifiedWalletAddresses.forEach { address ->
                Text(
                    text = address,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para volver a la pantalla anterior
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}

fun isValidWallet(walletAddress: String): Boolean {
    return walletAddress.startsWith("0x") && walletAddress.length == 42
}

@Composable
fun MyWalletCheckerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF6200EE),
            secondary = Color(0xFF03DAC6),
            background = Color(0xFFF1F1F1),
            error = Color(0xFFB00020),
            onPrimary = Color.White,
            onSecondary = Color.Black,
            onBackground = Color.Black,
            onError = Color.White
        ),
        typography = Typography(),
        content = content
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewEntryScreen() {
    MyWalletCheckerTheme {
        EntryScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWalletCheckerScreen() {
    MyWalletCheckerTheme {
        WalletCheckerScreen(
            navController = rememberNavController(),
            notesViewModel = viewModel()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewShowNotesScreen() {
    MyWalletCheckerTheme {
        ShowNotesScreen(navController = rememberNavController(), notesViewModel = viewModel())
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddNoteScreen() {
    MyWalletCheckerTheme {
        AddNoteScreen(navController = rememberNavController(), notesViewModel = viewModel())
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHistoryScreen() {
    MyWalletCheckerTheme {
        HistoryScreen(navController = rememberNavController(), notesViewModel = viewModel())
    }
}






















