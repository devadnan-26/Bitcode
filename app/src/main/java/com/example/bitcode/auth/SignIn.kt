package com.example.bitcode.auth

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bitcode.R
import com.example.bitcode.activities.ErrorMessage
import com.example.bitcode.activities.checkEmail
import com.example.bitcode.activities.checkPassword
import com.example.bitcode.data.User
import com.example.bitcode.googleAuth.GoogleSignInAuth
import com.example.bitcode.ui.theme.BitcodeTheme
import com.example.bitcode.ui.theme.arvo
import com.example.bitcode.ui.theme.arvo_bold
import com.example.bitcode.viewModels.AuthenticationViewModel
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun LoginPage(navController: NavController?, context: ComponentActivity?) {
    Scaffold { paddingValues ->
        var emailText by remember { mutableStateOf("") }
        var passwordText by remember { mutableStateOf("") }
        var emailError by remember { mutableStateOf(false) }
        var passwordError by remember { mutableStateOf(false) }
        val viewModel = viewModel<AuthenticationViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        LaunchedEffect(key1 = state.isSignInSuccessful) {
            if (state.isSignInSuccessful) {
                val user = Firebase.auth.currentUser
                val db = Firebase.database
                val myRef = db.getReference("users")
                val data = User(user?.displayName?: "", user?.email ?: "", user?.photoUrl.toString())
                myRef.child(data.name.replace(".", "")).child("data").setValue(data).addOnFailureListener { exception ->
                        Log.d("storage", exception.message ?: "")
                    }
                navController?.navigate("start")
                viewModel.resetState()
            }
        }
        ConstraintLayout(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())

        ) {
            val (fabButtons, email, password, icon, loginButton, signInMessage, signUpMessage, continueMessage) = createRefs()
            Row(modifier = Modifier.constrainAs(icon) {
                centerHorizontallyTo(parent)
                top.linkTo(parent.top, margin = 16.dp)
            }) {
                Image(
                    painter = painterResource(id = R.drawable.bitcode_icon),
                    contentDescription = null,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(72.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Image(
                    painter = painterResource(id = R.drawable.bitcode),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(115.09.dp, 23.35.dp)
                )
            }
            Row(modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .constrainAs(signInMessage) {
                    top.linkTo(icon.bottom, margin = 48.dp)
                }) {
                HorizontalDivider(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .weight(2.0f)
                        .align(Alignment.CenterVertically)
                )
                Text(
                    text = stringResource(id = R.string.sign_in),
                    modifier = Modifier.align(Alignment.CenterVertically),
                    fontFamily = arvo_bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                HorizontalDivider(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(2.0f)
                        .align(Alignment.CenterVertically)
                )
            }
            Row(modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .constrainAs(continueMessage) {
                    top.linkTo(
                        loginButton.bottom,
                        margin = 32.dp
                    )
                }) {
                HorizontalDivider(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .weight(2.0f)
                        .align(Alignment.CenterVertically)
                )
                Text(
                    text = stringResource(id = R.string.or_you_can),
                    modifier = Modifier.align(Alignment.CenterVertically),
                    fontFamily = arvo_bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                HorizontalDivider(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(2.0f)
                        .align(Alignment.CenterVertically)
                )
            }
            Column(modifier = Modifier.constrainAs(email) {
                width = Dimension.preferredWrapContent
                top.linkTo(signInMessage.bottom, margin = 32.dp)
            }) {
                OutlinedTextField(
                    value = emailText,
                    isError = emailError,
                    onValueChange = {
                        emailText = it
                        emailError = !checkEmail(emailText)
                    },
                    label = {
                        Text(stringResource(id = R.string.email), fontFamily = arvo_bold)
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.email_bitcode),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    textStyle = TextStyle(color = Color.Black, fontFamily = arvo),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorResource(id = R.color.app_green),
                        unfocusedBorderColor = Color(0xF0666666),
                        errorBorderColor = Color(0xFFF44336),
                        focusedLabelColor = Color(0xFF0C58D0)
                    )
                )
                Spacer(modifier = Modifier.padding(vertical = 4.dp))
                AnimatedVisibility(visible = emailError) {
                    ErrorMessage(
                        error = stringResource(id = R.string.email_error),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
            Column(modifier = Modifier.constrainAs(password) {
                width = Dimension.preferredWrapContent
                top.linkTo(email.bottom, margin = 32.dp)
            }) {
                OutlinedTextField(
                    value = passwordText,
                    isError = passwordError,
                    onValueChange = {
                        passwordText = it
                        passwordError = !checkPassword(passwordText)
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    label = {
                        Text(stringResource(id = R.string.password), fontFamily = arvo_bold)
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.password),
                            contentDescription = null
                        )
                    },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorResource(id = R.color.app_green),
                        unfocusedBorderColor = Color(0xF0666666),
                        errorBorderColor = Color(0xFFF44336),
                        focusedLabelColor = Color(0xFF0C58D0)
                    ),
                    textStyle = TextStyle(color = Color.Black, fontFamily = arvo),
                )
                Spacer(modifier = Modifier.padding(vertical = 4.dp))
                AnimatedVisibility(visible = passwordError) {
                    ErrorMessage(
                        error = stringResource(id = R.string.password_error),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
            Row(modifier = Modifier.constrainAs(signUpMessage) {
                centerHorizontallyTo(parent)
                top.linkTo(password.bottom, margin = 32.dp)
            }) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = Color.Black)) {
                            append(stringResource(id = R.string.dont_have_account))
                            append(" ")
                        }
                    },
                    fontFamily = arvo,
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Text(
                    text = stringResource(id = R.string.sign_up),
                    fontFamily = arvo,
                    fontSize = 14.sp,
                    color = Color(0xFF0678be),
                    modifier = Modifier.clickable {
                        navController?.navigate("signUp")
                    }
                )
            }
            Button(
                onClick = {
                    if (checkEmail(emailText) && checkPassword(passwordText)) {
                        val auth = Firebase.auth
                        auth.signInWithEmailAndPassword(emailText, passwordText)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    val db = Firebase.firestore
                                    db.collection("$emailText - data").get()
                                        .addOnSuccessListener { result ->
                                            Log.d("Verification", result.documents.toString())
                                        }.addOnFailureListener {

                                    }
                                    viewModel.onSignInResultNative(it.result)
                                }
                            }.addOnFailureListener {
                                Toast.makeText(
                                    context,
                                    ContextCompat.getString(
                                        context!!,
                                        R.string.native_sign_in_error_message
                                    ),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                    } else {
                        emailError = !checkEmail(emailText)
                        passwordError = !checkPassword(passwordText)
                    }
                },
                modifier = Modifier.constrainAs(loginButton) {
                    centerHorizontallyTo(parent)
                    top.linkTo(signUpMessage.bottom, margin = 48.dp)
                },
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(colorResource(R.color.app_green))
            ) {
                Text(
                    text = stringResource(id = R.string.sign_in),
                    color = Color.White,
                    fontFamily = arvo_bold,
                    fontSize = 18.sp
                )
            }
            val googleAuthUiClient by lazy {
                GoogleSignInAuth(
                    context = context!!,
                    oneTapClient = Identity.getSignInClient(context)
                )
            }
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        viewModel.viewModelScope.launch {
                            val signInResult = googleAuthUiClient.signInWithIntent(
                                intent = result.data ?: return@launch
                            )
                            viewModel.onSignInResultOneTap(signInResult)
                        }
                    }
                }
            )

            Column(modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .constrainAs(fabButtons) {
                    centerHorizontallyTo(parent)
                    top.linkTo(continueMessage.bottom, margin = 32.dp)
                }, horizontalAlignment = Alignment.CenterHorizontally) {
                val signInLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    try {
                        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                        val account = task.getResult(ApiException::class.java)
                        if (account != null) {
                            val credential =
                                GoogleAuthProvider.getCredential(account.idToken!!, null)
                            Firebase.auth.signInWithCredential(credential)
                                .addOnCompleteListener { tasks ->
                                    if (tasks.isSuccessful) {
                                        // Sign in success, update UI with the signed-in user's information
                                        viewModel.onSignInResult(account)
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(
                                            "SignInActivity",
                                            "signInWithCredential:failure",
                                            tasks.exception
                                        )
                                    }
                                }
                        }
                    }
                    catch (e: Exception) {
                        Toast.makeText(context, "Unable to sign in with Google", Toast.LENGTH_LONG).show()
                    }
                }
                val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(context!!.getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
                val googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions)
                Button(
                    onClick = {
                        if (Firebase.auth.currentUser != null)
                            navController?.navigate("start")
                        viewModel.viewModelScope.launch {
                            val signInIntentSender = googleAuthUiClient.signInOneTap()
                            if (signInIntentSender != null) {
                                launcher.launch(
                                    IntentSenderRequest.Builder(
                                        signInIntentSender
                                    ).build()
                                )
                            }
                            else {
                                val signInIntent = googleSignInClient.signInIntent
                                signInLauncher.launch(signInIntent)
                            }
                        }
                    },
                    modifier = Modifier.width(450.dp),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(Color.White),
                    border = BorderStroke(
                        2.dp,
                        Brush.linearGradient(
                            listOf(
                                Color(0xFFDB4437),
                                Color(0xFFF4B400),
                                Color(0xFF0F9D58),
                                Color(0xFF4285F4)
                            )
                        )
                    )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.google),
                        contentDescription = null,
                        modifier = Modifier.weight(1.0f),
                        alignment = Alignment.CenterStart
                    )
                    Text(
                        text = stringResource(id = R.string.continue_google),
                        color = Color.Black,
                        modifier = Modifier
                            .weight(7.0f)
                            .padding(start = 8.dp),
                        fontFamily = arvo_bold,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Start
                    )
                }
                Spacer(modifier = Modifier.padding(8.dp))

                val scope = rememberCoroutineScope()
                val loginManager = LoginManager.getInstance()
                val callbackManager = remember { CallbackManager.Factory.create() }
                val launcherFacebook = rememberLauncherForActivityResult(
                    loginManager.createLogInActivityResultContract(callbackManager, null)
                ) {
                    // nothing to do. handled in FacebookCallback
                }
                DisposableEffect(Unit) {

                    loginManager.registerCallback(
                        callbackManager,
                        object : FacebookCallback<LoginResult> {
                            override fun onCancel() {

                            }

                            override fun onError(error: FacebookException) {

                            }

                            override fun onSuccess(result: LoginResult) {
                                scope.launch {
                                    val token = result.accessToken.token
                                    val credential = FacebookAuthProvider.getCredential(token)
                                    val authResult =
                                        Firebase.auth.signInWithCredential(credential).await()
                                    if (authResult.user != null) {
                                        viewModel.onSignInResultFacebook(authResult)
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Unable to sign in with Facebook",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }

                        })

                    onDispose {
                        loginManager.unregisterCallback(callbackManager)
                    }
                }

                Button(
                    onClick = { launcherFacebook.launch(listOf("email", "public_profile")) },
                    modifier = Modifier.width(450.dp),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF1877F2)),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.facebook),
                        contentDescription = null,
                        modifier = Modifier.weight(1.0f),
                        alignment = Alignment.CenterStart
                    )
                    Text(
                        text = stringResource(id = R.string.continue_facebook),
                        color = Color.White,
                        modifier = Modifier
                            .weight(7.0f)
                            .padding(start = 8.dp),
                        fontFamily = arvo_bold,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Start
                    )
                }
                val auth = Firebase.auth
                val provider = OAuthProvider.newBuilder("github.com")
                provider.scopes = listOf("user:email")

                Spacer(modifier = Modifier.padding(8.dp))
                Button(
                    onClick = {
                        val pendingResultTask = auth.pendingAuthResult
                        if (pendingResultTask != null) {
                            pendingResultTask
                                .addOnSuccessListener {
                                    viewModel.onSignInResultGithub(it)
                                }
                                .addOnFailureListener {
                                    it.message?.let { it1 -> Log.d("GITHUB", it1) }
                                }
                        } else {
                            auth.startActivityForSignInWithProvider(context!!, provider.build())
                                .addOnSuccessListener {
                                    viewModel.onSignInResultGithub(it)
                                }.addOnFailureListener {
                                    Toast.makeText(
                                        context,
                                        "Unable to sign in with Github",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    Log.d("GITHUB", it.message.toString())
                                }
                        }
                    },
                    modifier = Modifier.width(450.dp),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(Color.Black),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.github),
                        contentDescription = null,
                        modifier = Modifier.weight(1.0f),
                        alignment = Alignment.CenterStart
                    )
                    Text(
                        text = stringResource(id = R.string.continue_github),
                        color = Color.White,
                        modifier = Modifier
                            .weight(7.0f)
                            .padding(start = 8.dp),
                        fontFamily = arvo_bold,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
}

private fun firebaseAuthWithGoogle(idToken: String) {

}

@Composable
@Preview(apiLevel = 34)
fun LoginPagePreview() {
    BitcodeTheme {
        LoginPage(navController = null, context = null)
    }
}