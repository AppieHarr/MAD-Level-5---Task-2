package com.example.mad.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import androidx.navigation.NavController
import com.example.mad.data.Quiz
import com.example.mad.repository.QuestionRepository
import com.example.mad.ui.screens.Screen
import kotlinx.coroutines.launch

class QuestViewModel(application: Application) : AndroidViewModel(application) {
    private val questionRepository: QuestionRepository = QuestionRepository()

    private val _currentQuestion: MutableLiveData<Quiz> = MutableLiveData()
    val currentQuestion: LiveData<Quiz>
        get() = _currentQuestion


    private var currentQuestionIndex = 0
    private lateinit var questionList: List<Quiz>

    // Initialize the view model
    init {
        questionList = emptyList()
    }

    // To get all questions from the database
    fun getQuestions() {
        // Launch a coroutine in viewModelScope
        viewModelScope.launch {
            // Try to get the questions
            try {
                questionList = questionRepository.getAllQuestions()
                // Check if the list is not empty
                if (questionList.isNotEmpty()) {
                    _currentQuestion.value = questionList[currentQuestionIndex]
                }
            } catch (e: Exception) {
                Log.e("Question Retrieval Error", e.message.toString())
            }
        }
    }

    // To get the next question from the list
    fun getNextQuestion(context: Context, navController: NavController) {
        // Check if there are more questions
        if (currentQuestionIndex < questionList.lastIndex) {
            // increment index and set the current question
            currentQuestionIndex++
            _currentQuestion.value = questionList[currentQuestionIndex]
        } else {
            // end of questions
            Toast.makeText(
                context,
                "Quiz completed!",
                Toast.LENGTH_SHORT
            ).show()
            navController.navigate(Screen.HomeScreen.route)
        }
    }

}
