package com.example.mad.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mad.data.Quiz
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout

class QuestionRepository {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val questionCollection: CollectionReference = firestore.collection("questions")

    private val _questions: MutableLiveData<List<Quiz>> = MutableLiveData()
    val questions: LiveData<List<Quiz>> = _questions

    // To get all questions from the database
    suspend fun getAllQuestions(): List<Quiz> {
        return try {
            // Timeout after 5 seconds
            withTimeout(5_000) {
                val querySnapshot = questionCollection.get().await()
                // Map the documents to a list of questions
                val questions = querySnapshot.documents.mapNotNull { document ->
                    val id = document.getLong("id")?.toInt()
                    val questionNumber = document.getString("question")
                    val choices = document.get("choices") as? List<String>
                    val correctAnswer = document.get("correctAnswer").toString()
                    if (id != null && questionNumber != null && choices != null) {
                        Quiz(id, questionNumber, choices, correctAnswer)
                    } else {
                        null
                    }
                }
                questions
            }
        } catch (e: Exception) {
            throw QuestionRetrievalError("Retrieval-firebase-task was unsuccessful")
        }
    }

    class QuestionRetrievalError(message: String) : Exception(message)
}
