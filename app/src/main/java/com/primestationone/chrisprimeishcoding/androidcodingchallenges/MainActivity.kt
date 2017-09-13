package com.primestationone.chrisprimeishcoding.androidcodingchallenges

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.primestationone.chrisprimeishcoding.androidcodingchallenges.utilities.SitkaCodingChallenge
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cText.text = stringFromJNI()    // Call to a native method in C
        cText.visibility = View.INVISIBLE   //Hide the C output for now

        // Run the coding challenge and display results
        codingChallengeAnswerText.text = SitkaCodingChallenge.sitkaChallenge(this)
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    private external fun stringFromJNI(): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
