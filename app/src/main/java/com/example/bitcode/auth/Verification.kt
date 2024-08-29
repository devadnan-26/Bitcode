package com.example.bitcode.auth

import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.bitcode.R
import com.example.bitcode.ui.theme.arvo
import com.example.bitcode.ui.theme.arvo_bold
import com.example.bitcode.viewModels.AuthenticationViewModel
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay

@Composable
fun Verification(context: ComponentActivity?, navController: NavController?) {
    val auth = Firebase.auth
    val viewModel = ViewModelProvider(context!!)[AuthenticationViewModel::class.java]
    val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val user = firebaseAuth.currentUser
        if (user != null) {
            // User is signed in
            if (user.isEmailVerified) {
                // User's email is verified
                viewModel.updateVerificationState(true)
            } else {
                // User's email is not verified
                viewModel.updateVerificationState(false)
            }
        } else {
            // User is signed out
        }
    }
    auth.addAuthStateListener(authListener)

    Scaffold { paddingValues ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val (icon, resendButton, verificationMessage, timerText) = createRefs()
            val verification = viewModel.isVerified.collectAsStateWithLifecycle()
            var timer by remember { mutableIntStateOf(0) }
            var isSend by remember { mutableStateOf(false) }
            var timerOn by remember { mutableStateOf(false) }
            var verificationText by remember {
                mutableStateOf(
                    context.getString(R.string.verification_link_sent)
                )
            }
            if (timer == 0 && !isSend) {
                val actionCodeSettingsBuilder = ActionCodeSettings.newBuilder()
                actionCodeSettingsBuilder.setUrl("https://bitcode-3024.firebaseapp.com")
                actionCodeSettingsBuilder.setAndroidPackageName(
                    "com.example.bitcode",
                    false,
                    null
                )
                auth.currentUser?.sendEmailVerification(actionCodeSettingsBuilder.build())
                    ?.addOnCompleteListener {
                        auth.signOut()
                    }?.addOnFailureListener {
                        Toast.makeText(
                            context,
                            "Failed to send link. Please try again later.",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.d("Verification", it.message.toString())
                    }
                timerOn = true
                isSend = true
            }
            LaunchedEffect(timerOn) {
                for (i in 1..60) {
                    timer = 60 - i
                    delay(1000)
                }
            }
            LaunchedEffect(verification.value) {
                if (verification.value) {
                    verificationText = context.getString(R.string.account_verified)
                    delay(1500)
                    navController?.navigate("start")
                }
            }
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
            Button(
                onClick = {
                    if (timer == 0) {
                        val actionCodeSettingsBuilder = ActionCodeSettings.newBuilder()
                        actionCodeSettingsBuilder.setUrl("https://bitcode-3024.firebaseapp.com")
                        actionCodeSettingsBuilder.setAndroidPackageName(
                            "com.example.bitcode",
                            false,
                            null
                        )
                        auth.currentUser?.sendEmailVerification(actionCodeSettingsBuilder.build())
                            ?.addOnCompleteListener {
                                auth.signOut()
                            }?.addOnFailureListener {
                            Toast.makeText(
                                context,
                                "Failed to send link. Please try again later.",
                                Toast.LENGTH_LONG
                            ).show()
                            Log.d("Verification", it.message.toString())
                        }
                        timerOn = true
                    }
                },
                enabled = timer == 0,
                modifier = Modifier.constrainAs(resendButton) {
                    centerHorizontallyTo(parent)
                    bottom.linkTo(parent.bottom, margin = 48.dp)
                },
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(colorResource(R.color.app_green))
            ) {
                Text(
                    text = stringResource(id = R.string.resend),
                    fontFamily = arvo_bold,
                    fontSize = 18.sp
                )
            }
            Text(
                text = verificationText,
                fontSize = 22.sp,
                fontFamily = arvo_bold,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .constrainAs(verificationMessage) {
                        centerHorizontallyTo(parent)
                        linkTo(icon.bottom, resendButton.top)
                    },
                textAlign = TextAlign.Center,
                lineHeight = 36.sp
            )
            Text(
                text = if (timer != 0) "${stringResource(id = R.string.resend)} $timer ${
                    stringResource(
                        id = R.string.seconds
                    )
                }" else "",
                fontFamily = arvo,
                modifier = Modifier.constrainAs(timerText) {
                    linkTo(resendButton.start, resendButton.end)
                    top.linkTo(resendButton.bottom, margin = 8.dp)
                }
            )
        }
    }
}

@Composable
@Preview(apiLevel = 33)
fun VerificationPreview() {
    Verification(context = null, null)
}