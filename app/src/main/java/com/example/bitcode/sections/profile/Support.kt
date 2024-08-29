package com.example.bitcode.sections.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.bitcode.R
import com.example.bitcode.ui.theme.BitcodeTheme
import com.example.bitcode.ui.theme.arvo_bold

@Composable
fun Support(navController: NavController) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (illustrations, info, troubleshoot) = createRefs()
        Column(modifier = Modifier.constrainAs(illustrations) {
            centerVerticallyTo(parent)
            top.linkTo(parent.top)
        }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.your_opinion_matters),
                    contentDescription = "one-on-one support",
                    modifier = Modifier.weight(0.45f)
                )
                Column(modifier = Modifier.weight(0.55f)) {
                    Text(
                        text = stringResource(id = R.string.your_opinion_matters),
                        textAlign = TextAlign.Start,
                        fontSize = 38.sp,
                        fontFamily = arvo_bold,
                        lineHeight = 46.sp,
                        modifier = Modifier.padding(start = 16.dp)
                    )
//                    Text(
//                        text = stringResource(id = R.string.your_opinion_matters_explanation),
//                        textAlign = TextAlign.Center,
//                        fontSize = 48.sp,
//                    )
                }
            }
            Spacer(modifier = Modifier.padding(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Column(modifier = Modifier.weight(0.55f)) {
                    Text(
                        text = stringResource(id = R.string.tailored_support_for_you),
                        textAlign = TextAlign.Start,
                        fontSize = 38.sp,
                        fontFamily = arvo_bold,
                        lineHeight = 46.sp,
                    )
//                    Text(
//                        text = stringResource(id = R.string.tailored_support_for_you_explanation),
//                        textAlign = TextAlign.Center,
//                        fontSize = 48.sp,
//                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.one_on_one_support),
                    contentDescription = "one-on-one support",
                    modifier = Modifier.weight(0.45f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SupportPreview() {
    BitcodeTheme {
        Support(rememberNavController())
    }
}