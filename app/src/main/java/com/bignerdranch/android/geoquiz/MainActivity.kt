package com.bignerdranch.android.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var questionTextView: TextView

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    private var currentIndex = 0
    private var currentResult = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "OnCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        val provider: ViewModelProvider = ViewModelProviders.of(this)
        val quizViewModel = provider.get(QuizViewModel::class.java)
        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        questionTextView = findViewById(R.id.question_text_view)

        trueButton.setOnClickListener { view: View ->
            updateResult(checkAnswer(true))
            listenAnswer(false)
        }

        falseButton.setOnClickListener { view: View ->
            updateResult(checkAnswer(false))
            listenAnswer(false)

        }
        nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
            listenAnswer(true)
            if (currentIndex == 0) {
                showResult(currentResult)
                resetResult()
            }

        }

        updateQuestion()

    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)
    }

    private fun listenAnswer(set: Boolean) {
        if (set) {
            trueButton.isClickable = true
            falseButton.isClickable = true
        } else {
            trueButton.isClickable = false
            falseButton.isClickable = false
        }
    }

    private fun checkAnswer(userAnswer: Boolean): Boolean {
        val correctAnswer = questionBank[currentIndex].answer

        val result = userAnswer == correctAnswer

        val messageResId = if (result) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }

        Toast.makeText(
            this,
            messageResId,
            Toast.LENGTH_SHORT
        )
            .show()

        return result
    }

    private fun showResult(result: Int) {

        val messageResId = result.toString() + "out of " + questionBank.size.toString()

        val myResultToast = Toast.makeText(
            this,
            messageResId,
            Toast.LENGTH_SHORT
        )
            .show()

    }

    private fun updateResult(answer: Boolean) {
        if (answer) {
            currentResult += 1
        }

    }

    private fun resetResult() {
        currentResult = 0
    }
}