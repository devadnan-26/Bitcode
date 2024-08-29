package com.example.bitcode.auth

import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalTextToolbar
import androidx.compose.ui.platform.TextToolbar
import androidx.compose.ui.platform.TextToolbarStatus
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.example.bitcode.R
import com.example.bitcode.activities.ErrorMessage
import com.example.bitcode.activities.ErrorMessages
import com.example.bitcode.activities.checkEmail
import com.example.bitcode.activities.checkPassword
import com.example.bitcode.activities.isPasswordError
import com.example.bitcode.sections.bottomNavigation.MyDatePickerDialog
import com.example.bitcode.ui.theme.arvo
import com.example.bitcode.ui.theme.arvo_bold
import com.example.bitcode.viewModels.AuthenticationViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@ExperimentalMaterial3Api
@Composable
fun SignUp(context: ComponentActivity?, navController: NavController?) {
    Scaffold { paddingValues ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val (icon, column, button, signUpMessage) = createRefs()
            var emailText by remember { mutableStateOf("") }
            var emailError by remember { mutableStateOf(false) }
            var passwordText by remember { mutableStateOf("") }
            var passwordError by remember { mutableStateOf(false) }
            var passwordConfirmText by remember { mutableStateOf("") }
            var passwordConfirmError by remember { mutableStateOf(false) }
            var firstNameText by remember { mutableStateOf("") }
            var firstNameError by remember { mutableStateOf(false) }
            var lastNameText by remember { mutableStateOf("") }
            var lastNameError by remember { mutableStateOf(false) }
            var birthdayText by remember { mutableStateOf("") }
            var birthdayError by remember { mutableStateOf(false) }
            var showDatePicker by remember { mutableStateOf(false) }
            if (showDatePicker) {
                MyDatePickerDialog(
                    onDateSelected = {
                        birthdayText = it
                        birthdayError = false
                    }
                ) { showDatePicker = false }
            }
            Row(modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .constrainAs(signUpMessage) {
                    top.linkTo(icon.bottom, margin = 48.dp)
                }) {
                HorizontalDivider(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .weight(2.0f)
                        .align(Alignment.CenterVertically)
                )
                Text(
                    text = stringResource(id = R.string.user_information),
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
            Column(modifier = Modifier.constrainAs(column) {
                top.linkTo(signUpMessage.bottom, margin = 32.dp)
            }) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .weight(3.0f)
                            .padding(end = 8.dp)
                    ) {
                        OutlinedTextField(
                            value = firstNameText,
                            isError = firstNameError,
                            onValueChange = {
                                firstNameText = it
                                firstNameError = firstNameText.isEmpty()
                            },
                            label = {
                                Text(
                                    stringResource(id = R.string.first_name),
                                    fontFamily = arvo_bold
                                )
                            },
                            textStyle = TextStyle(color = Color.Black, fontFamily = arvo),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = colorResource(id = R.color.app_green),
                                unfocusedBorderColor = Color(0xF0666666),
                                errorBorderColor = Color(0xFFF44336),
                                focusedLabelColor = Color(0xFF0C58D0)
                            ),
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.padding(vertical = 4.dp))
                        AnimatedVisibility(visible = firstNameError) {
                            ErrorMessage(
                                error = stringResource(id = R.string.name_error),
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .weight(3.0f)
                            .padding(start = 8.dp)
                    ) {
                        OutlinedTextField(
                            value = lastNameText,
                            isError = lastNameError,
                            onValueChange = {
                                lastNameText = it
                                lastNameError = lastNameText.isEmpty()
                            },
                            label = {
                                Text(
                                    stringResource(id = R.string.last_name),
                                    fontFamily = arvo_bold
                                )
                            },
                            textStyle = TextStyle(color = Color.Black, fontFamily = arvo),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = colorResource(id = R.color.app_green),
                                unfocusedBorderColor = Color(0xF0666666),
                                errorBorderColor = Color(0xFFF44336),
                                focusedLabelColor = Color(0xFF0C58D0)
                            ),
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.padding(vertical = 4.dp))
                        AnimatedVisibility(visible = lastNameError) {
                            ErrorMessage(
                                error = stringResource(id = R.string.name_error),
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                }
                Column {
                    OutlinedTextField(
                        value = birthdayText,
                        isError = birthdayError,
                        enabled = false,
                        readOnly = true,
                        onValueChange = {
                            birthdayError = birthdayText.isEmpty()
                        },
                        label = {
                            Text(stringResource(id = R.string.birthday), fontFamily = arvo_bold)
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.calendar),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                            .clickable {
                                showDatePicker = true
                            },
                        textStyle = TextStyle(color = Color.Black, fontFamily = arvo),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorResource(id = R.color.app_green),
                            unfocusedBorderColor = Color(0xF0666666),
                            errorBorderColor = Color(0xFFF44336),
                            focusedLabelColor = Color(0xFF0C58D0),
                            disabledBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    Spacer(modifier = Modifier.padding(vertical = 4.dp))
                    AnimatedVisibility(visible = birthdayError) {
                        ErrorMessage(
                            error = stringResource(id = R.string.birthday_error),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
                Column {
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
                Column {
                    CompositionLocalProvider(LocalTextToolbar provides EmptyTextToolbar) {
                        OutlinedTextField(
                            value = passwordText,
                            isError = passwordError,
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            maxLines = 1,
                            onValueChange = {
                                passwordText = it
                                passwordError = isPasswordError(passwordText)
                            },
                            label = {
                                Text(stringResource(id = R.string.password), fontFamily = arvo_bold)
                            },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.password),
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
                    }
                    Spacer(modifier = Modifier.padding(vertical = 4.dp))
                    AnimatedVisibility(visible = passwordError) {
                        ErrorMessages(
                            context = context!!, password = passwordText, modifier = Modifier
                                .padding(horizontal = 16.dp)
                        )
                    }
                }
                Column {
                    CompositionLocalProvider(LocalTextToolbar provides EmptyTextToolbar) {
                        OutlinedTextField(
                            value = passwordConfirmText,
                            isError = passwordConfirmError,
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            maxLines = 1,
                            onValueChange = {
                                passwordConfirmText = it
                                passwordConfirmError = passwordText != passwordConfirmText
                            },
                            label = {
                                Text(
                                    stringResource(id = R.string.password_confirm),
                                    fontFamily = arvo_bold
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.password),
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
                    }
                    Spacer(modifier = Modifier.padding(vertical = 4.dp))
                    AnimatedVisibility(visible = passwordConfirmError) {
                        ErrorMessage(
                            error = stringResource(id = R.string.password_confirm_error),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
            Button(
                onClick = {
                    if (checkAccount(
                            emailText,
                            passwordText,
                            passwordConfirmText,
                            birthdayText,
                            firstNameText,
                            lastNameText
                        )
                    ) {
                        val auth = Firebase.auth
                        auth.createUserWithEmailAndPassword(emailText, passwordText)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    val viewModel =
                                        ViewModelProvider(context!!)[AuthenticationViewModel::class.java]
                                    viewModel.onSignInResultNative(it.result)
                                    val db = Firebase.firestore
                                    val userData = hashMapOf(
                                        "firstName" to firstNameText,
                                        "lastName" to lastNameText,
                                        "birthday" to birthdayText
                                    )
                                    db.collection("$emailText - data").document("User Data")
                                        .set(userData).addOnFailureListener { exception ->
                                        Log.d("storage", exception.message ?: "")
                                    }
                                    navController?.navigate("verificationLink")
                                }
                            }.addOnFailureListener {
                                Toast.makeText(
                                    context,
                                    ContextCompat.getString(
                                        context!!,
                                        R.string.native_sign_up_error_message
                                    ),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                    } else {
                        emailError = !checkEmail(emailText)
                        passwordError = !checkPassword(passwordText)
                        passwordConfirmError =
                            !checkPasswordConfirmation(passwordText, passwordConfirmText)
                        birthdayError = !checkBirthday(birthdayText)
                        firstNameError = firstNameText.isEmpty()
                        lastNameError = lastNameText.isEmpty()
                    }
                },
                modifier = Modifier.constrainAs(button) {
                    centerHorizontallyTo(parent)
                    top.linkTo(column.bottom, margin = 48.dp)
                },
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(colorResource(R.color.app_green))
            ) {
                Text(
                    text = stringResource(id = R.string.sign_up_message),
                    color = Color.White,
                    fontFamily = arvo_bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}

fun checkBirthday(birthday: String): Boolean {
    return birthday.isNotEmpty()
}

fun checkPasswordConfirmation(password: String, passwordConfirm: String): Boolean {
    return passwordConfirm == password
}

fun checkName(firstName: String, lastName: String): Boolean {
    return firstName.isNotEmpty() && lastName.isNotEmpty()
}

fun checkAccount(
    email: String,
    password: String,
    passwordConfirm: String,
    birthday: String,
    firstName: String,
    lastName: String
): Boolean {
    return if (checkEmail(email) && checkPassword(password) && checkBirthday(birthday) && checkPasswordConfirmation(
            password,
            passwordConfirm
        ) && checkName(firstName, lastName)
    ) return true else false
}

object EmptyTextToolbar : TextToolbar {
    override val status: TextToolbarStatus = TextToolbarStatus.Hidden

    override fun hide() {}

    override fun showMenu(
        rect: Rect,
        onCopyRequested: (() -> Unit)?,
        onPasteRequested: (() -> Unit)?,
        onCutRequested: (() -> Unit)?,
        onSelectAllRequested: (() -> Unit)?,
    ) {
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun Preview() {
    SignUp(null, null)
}