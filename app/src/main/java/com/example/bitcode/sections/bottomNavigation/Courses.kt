package com.example.bitcode.sections.bottomNavigation

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.bitcode.Items.Items
import com.example.bitcode.R
import com.example.bitcode.ui.theme.arvo
import com.example.bitcode.ui.theme.arvo_bold
import com.example.bitcode.viewModels.PlatformViewModel

@Composable
fun Courses(context: ComponentActivity?, navController: NavController) {

    val viewModel = ViewModelProvider(context!!)[PlatformViewModel::class.java]
    BackHandler {
        viewModel.changeItem(Items.Home)
        navController.navigate("Home")
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        val listOfColors = listOf(Color(0xFF0053d5), Color(0xFFFFED50), Color(0xFFFF4D4D))
        val (coursesList, header, search, featured, featuredCourses) = createRefs()
        var searchValue by remember { mutableStateOf(context?.getString(R.string.search_course)) }
        TextField(
            value = searchValue ?: "",
            onValueChange = { newText ->
                searchValue = newText
            },
            textStyle = TextStyle(
                color = Color(0xFF707070),
                fontSize = 18.sp,
                fontFamily = arvo
            ),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .heightIn(50.dp, 75.dp)
                .clip(RoundedCornerShape(50.dp))
                .constrainAs(search) {
                    width = Dimension.preferredWrapContent
                    height = Dimension.preferredWrapContent
                    top.linkTo(parent.top, margin = 24.dp)
                }
                .clickable(
                    onClick = { navController?.navigate("Search") }
                )
                .onFocusChanged { focusState ->
                    if (focusState.isFocused && searchValue == context?.getString(R.string.search_course)) {
                        searchValue = ""
                        navController?.navigate("Search")
                    } else if (focusState.isFocused && searchValue?.isEmpty() == true) {
                        searchValue = context?.getString(R.string.search_course)
                    }
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
fun SearchView(context: ComponentActivity?, navController: NavController) {
    Scaffold {
        ConstraintLayout(modifier = Modifier.padding(it)) {

            val (searchBar, results) = createRefs()
            var searchValue by remember { mutableStateOf(context?.getString(R.string.search_course)) }

            val colorsList = listOf(
                Color.LightGray.copy(0.6f),
                Color.LightGray.copy(0.2f),
                Color.LightGray.copy(0.6f)
            )

            val transition = rememberInfiniteTransition(label = "")
            val translateAnim = transition.animateFloat(
                initialValue = 0f,
                targetValue = 1000f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 1000,
                        easing = LinearOutSlowInEasing
                    )
                ),
                label = ""
            )

            val brush = Brush.linearGradient(
                colors = colorsList,
                start = Offset.Zero,
                end = Offset(x = translateAnim.value, y = translateAnim.value)
            )

            TextField(
                value = searchValue ?: "",
                onValueChange = { newText ->
                    searchValue = newText
                },
                textStyle = TextStyle(
                    color = Color(0xFF707070),
                    fontSize = 18.sp,
                    fontFamily = arvo
                ),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .heightIn(50.dp, 75.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .constrainAs(searchBar) {
                        width = Dimension.preferredWrapContent
                        height = Dimension.preferredWrapContent
                        top.linkTo(parent.top, margin = 24.dp)
                    }
                    .clickable(
                        onClick = { searchValue = "" }
                    )
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused && searchValue == context?.getString(R.string.search_course)) {
                            searchValue = ""
                        } else if (focusState.isFocused && searchValue?.isEmpty() == true) {
                            searchValue = context?.getString(R.string.search_course)
                        }
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
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .constrainAs(results) {
                        width = Dimension.preferredWrapContent
                        width = Dimension.preferredWrapContent
                        top.linkTo(searchBar.bottom, margin = 24.dp)
                    }) {
                for (i in 0..1)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .defaultMinSize(minHeight = 150.dp)
                            .alpha(0.90f)
                            .background(brush)
                    ) {

                    }

            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun CoursesPreview() {
    Courses(context = null, rememberNavController())
}

@Composable
@Preview(showBackground = true)
fun SearchPreview() {
    SearchView(context = null, navController = rememberNavController())
}