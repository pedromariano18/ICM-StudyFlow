package com.example.studyflow.presentation.auth

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.studyflow.presentation.destinations.DashboardScreenRouteDestination
import com.example.studyflow.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import androidx.compose.ui.layout.ContentScale


@RootNavGraph(start = true)
@Destination
@Composable
fun AuthScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val isLoggedIn = viewModel.isLoggedIn
    val errorMessage = viewModel.errorMessage
    var registerError by remember { mutableStateOf<String?>(null) }

    // Redireciona para o dashboard se estiver autenticado
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navController.navigate(DashboardScreenRouteDestination.route) {
                popUpTo("auth_screen") { inclusive = true }
            }
        }
    }

    // Launcher para login com Google
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken
            if (idToken != null) {
                viewModel.firebaseAuthWithGoogle(idToken) { success, error ->
                    if (!success && error != null) {
                        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        } catch (e: ApiException) {
            Toast.makeText(context, "Erro no login com Google", Toast.LENGTH_SHORT).show()
        }
    }

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("249558520810-am6c8gn1mtdteh4aj2n2p9g7m6vtm48p.apps.googleusercontent.com")
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.study_logo),
            contentDescription = "App Logo",
            modifier = Modifier
                .padding(top = 32.dp, bottom = 16.dp)
                .size(220.dp)
        )

        // login
        TextField(
            value = viewModel.email,
            onValueChange = { viewModel.email = it.trim() },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )

        TextField(
            value = viewModel.password,
            onValueChange = { viewModel.password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )

        // Login e Criar Conta lado a lado
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { viewModel.login() },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp)
            ) {
                Text("Login")
            }

            Button(
                onClick = {
                    viewModel.register { success, error ->
                        if (!success && error != null) {
                            registerError = error
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp)
            ) {
                Text("Create Account")
            }
        }

        // Erro de login ou registo
        errorMessage?.let {
            Text(text = it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
        }

        registerError?.let {
            Text(text = it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
        }

        // Botão com imagem do Google
        Image(
            painter = painterResource(id = R.drawable.google1),
            contentDescription = "Login com Google",
            modifier = Modifier
                .padding(top = 24.dp)
                .size(width = 280.dp, height = 56.dp)
                .clickable {
                    val signInIntent = googleSignInClient.signInIntent
                    launcher.launch(signInIntent)
                }
        )
    }
}
