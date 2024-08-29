package com.example.bitcode.common

import android.content.Context
import androidx.core.content.ContextCompat.getString
import com.example.bitcode.R
import com.example.bitcode.activities.Choices

class QuestionsList(context: Context) {
    private val questionOneOne = listOf(
        getString(context, R.string.answer_1_1_1),
        getString(context, R.string.answer_1_1_2),
        getString(context, R.string.answer_1_1_3)
    )
    private val questionOneTwo = listOf(
        getString(context, R.string.answer_1_2_1),
        getString(context, R.string.answer_1_2_2),
        getString(context, R.string.answer_1_2_3)
    )
    private val questionOneThree = listOf(
        getString(context, R.string.answer_1_3_1),
        getString(context, R.string.answer_1_3_2),
        getString(context, R.string.answer_1_3_3)
    )
    private val questionTwoOne = listOf(
        getString(context, R.string.answer_2_1_1),
        getString(context, R.string.answer_2_1_2),
        getString(context, R.string.answer_2_1_3)
    )
    private val questionTwoTwo = listOf(
        getString(context, R.string.answer_2_2_1),
        getString(context, R.string.answer_2_2_2),
        getString(context, R.string.answer_2_2_3)
    )
    private val questionThreeOne = listOf(
        getString(context, R.string.answer_3_1_1),
        getString(context, R.string.answer_3_1_2),
        getString(context, R.string.answer_3_1_3)
    )
    private val questionThreeTwo = listOf(
        getString(context, R.string.answer_3_2_1),
        getString(context, R.string.answer_3_2_2),
        getString(context, R.string.answer_3_2_3)
    )
    private val questionFourOne = listOf(
        getString(context, R.string.answer_4_1_1),
        getString(context, R.string.answer_4_1_2),
        getString(context, R.string.answer_4_1_3)
    )
    private val questionFourTwo = listOf(
        getString(context, R.string.answer_4_2_1),
        getString(context, R.string.answer_4_2_2),
        getString(context, R.string.answer_4_2_3)
    )
    private val questionFiveOne = listOf(
        getString(context, R.string.answer_5_1_1),
        getString(context, R.string.answer_5_1_2),
        getString(context, R.string.answer_5_1_3)
    )
    private val questionFiveTwo = listOf(
        getString(context, R.string.answer_5_2_1),
        getString(context, R.string.answer_5_2_2),
        getString(context, R.string.answer_5_2_3)
    )
    private val questionSixOne = listOf(
        getString(context, R.string.answer_6_1_1),
        getString(context, R.string.answer_6_1_2),
        getString(context, R.string.answer_6_1_3)
    )
    private val questionSixTwo = listOf(
        getString(context, R.string.answer_6_2_1),
        getString(context, R.string.answer_6_2_2),
        getString(context, R.string.answer_6_2_3)
    )
    private val questionsList = listOf(
        null,
        Choices(getString(context, R.string.question_1_1), questionOneOne),
        Choices(getString(context, R.string.question_1_2), questionOneTwo),
        Choices(getString(context, R.string.question_1_3), questionOneThree),
        null,
        Choices(getString(context, R.string.question_2_1), questionTwoOne),
        Choices(getString(context, R.string.question_2_2), questionTwoTwo),
        null,
        Choices(getString(context, R.string.question_3_1), questionThreeOne),
        Choices(getString(context, R.string.question_3_2), questionThreeTwo),
        null,
        Choices(getString(context, R.string.question_4_1), questionFourOne),
        Choices(getString(context, R.string.question_4_2), questionFourTwo),
        null,
        Choices(getString(context, R.string.question_5_1), questionFiveOne),
        Choices(getString(context, R.string.question_5_2), questionFiveTwo),
        null,
        Choices(getString(context, R.string.question_6_1), questionSixOne),
        Choices(getString(context, R.string.question_6_2), questionSixTwo),
        null
    )
    fun getQuestionList(): List<Choices?> = questionsList
}