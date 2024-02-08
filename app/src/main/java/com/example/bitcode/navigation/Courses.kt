package com.example.bitcode.navigation

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.example.bitcode.R
import com.example.bitcode.ui.theme.arvo
import com.example.bitcode.ui.theme.arvo_bold
import com.example.bitcode.viewModels.PlatformViewModel

@Composable
fun Courses(context: ComponentActivity?, navController: NavController?) {
    val viewModel = context?.let { ViewModelProvider(it) }?.get(PlatformViewModel::class.java)
    BackHandler {
        viewModel?.changeItems("Home")
        navController?.navigate("Home")
    }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        val listOfColors = listOf(Color(0xFF0053d5), Color(0xFFFFED50), Color(0xFFFF4D4D))
        var searchValue by remember { mutableStateOf("") }
        val (coursesList, header, search, featured, featuredCourses) = createRefs()
        val viewModel = context?.let { ViewModelProvider(it) }?.get(PlatformViewModel::class.java)
        TextField(
            value = searchValue,
            onValueChange = { newText ->
                searchValue = newText
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .heightIn(50.dp, 75.dp)
                .clip(RoundedCornerShape(50.dp))
                .constrainAs(search) {
                    width = Dimension.preferredWrapContent
                    height = Dimension.preferredWrapContent
                    top.linkTo(parent.top, margin = 24.dp)
                },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFC0C0C0),
                unfocusedContainerColor = Color(0xFFC0C0C0),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
            },
            label = {
                Text(text = stringResource(id = R.string.search_course), fontFamily = arvo)
            }
        )

        Text(
            text = stringResource(id = R.string.my_courses),
            fontSize = 28.sp,
            color = Color.Black,
            fontFamily = arvo_bold,
            modifier = Modifier.constrainAs(header) {
                start.linkTo(parent.start, margin = 16.dp)
                top.linkTo(search.bottom, margin = 16.dp)
            }
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .constrainAs(coursesList) {
                    width = Dimension.preferredWrapContent
                    width = Dimension.preferredWrapContent
                    top.linkTo(header.bottom, margin = 24.dp)
                }) {
            listOfColors.forEach { color ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .defaultMinSize(minHeight = 150.dp)
                        .alpha(0.90f)
                        .background(color)
                ) {

                }
                Spacer(modifier = Modifier.padding(8.dp))

            }
        }
        Text(
            text = stringResource(id = R.string.featured_courses),
            fontSize = 28.sp,
            color = Color.Black,
            fontFamily = arvo_bold,
            modifier = Modifier.constrainAs(featured) {
                start.linkTo(parent.start, margin = 16.dp)
                top.linkTo(coursesList.bottom, margin = 16.dp)
            }
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .constrainAs(featuredCourses) {
                    width = Dimension.preferredWrapContent
                    width = Dimension.preferredWrapContent
                    top.linkTo(featured.bottom, margin = 24.dp)
                }
                .padding(bottom = 8.dp)) {
            listOfColors.forEach { color ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .defaultMinSize(minHeight = 150.dp)
                        .alpha(0.90f)
                        .background(color)
                ) {

                }
                Spacer(modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun CoursesPreview() {
    Courses(context = null, null)
}