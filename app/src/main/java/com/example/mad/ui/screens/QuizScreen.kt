package com.example.mad.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mad.R
import com.example.mad.viewmodel.QuestViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun QuizScreen(navController: NavController, viewModel: QuestViewModel) {
    // Get the current context.
    val context = LocalContext.current

    viewModel.getQuestions()
    // Observe the current question from the view model.
    val quiz by viewModel.currentQuestion.observeAsState()

    var selectedAnswerIndex by remember { mutableStateOf(-1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                // "Arrow back" implementation.
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() } ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back to homescreen"
                        )
                    }
                }
            )
        },
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            quiz?.let { question ->
                Text(
                    text = question.questionNumber,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                question.choices.forEachIndexed { index, choice ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .clickable {
                                selectedAnswerIndex = index
                            }
                    ) {
                        RadioButton(
                            selected = selectedAnswerIndex == index,
                            onClick = { selectedAnswerIndex = index },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = choice,
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .fillMaxWidth()
                        )
                    }
                }

                Button(
                    onClick = {
                        // Check if the selected answer is correct.
                        if (selectedAnswerIndex >= 0 && question.choices[selectedAnswerIndex] == question.correctAnswer) {
                            // If the answer is correct, go to the next question.
                            viewModel.getNextQuestion(context = context, navController = navController)
                        } else {
                            Toast.makeText(
                                context,
                                "Incorrect answer! Please try again.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    enabled = selectedAnswerIndex >= 0,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(text = context.getString(R.string.confirm))
                }
            }
        }
    }
}
