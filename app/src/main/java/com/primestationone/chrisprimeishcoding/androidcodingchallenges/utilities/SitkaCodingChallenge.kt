package com.primestationone.chrisprimeishcoding.androidcodingchallenges.utilities

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import timber.log.Timber
import java.util.*

/**
 * Created by chris on 9/13/17.
 */

object SitkaCodingChallenge {

    const val SITKA_CHALLENGE_HASH_LETTERS = "acdekilmnoprstuy"
    const val SITKA_CHALLENGE_HASH_FACTOR: Long = 83
    const val SITKA_CHALLENGE_HASH_INITIAL_VALUE: Long = 9
    const val SITKA_CHALLENGE_HASH_EXAMPLE_WORD5 = "cloud"
    const val SITKA_CHALLENGE_HASH_CHALLENGE: Long = 1693941520599974437
    const val SITKA_CHALLENGE_HASH_CHALLENGE_WORD_LENGTH = 9

    @JvmStatic  //The JvmStatic annotation is only required to call statically from Java
            /**
             * Find a 9 letter string of characters that contains only letters from

            acdekilmnoprstuy

            such that the hash(the_string) is

            1693941520599974437

            if hash is defined by the following pseudo-code:

            Int64 hash (String s) {
            Int64 h = 9
            String letters = "acdekilmnoprstuy"
            for(Int32 i = 0; i < s.length; i++) {
            h = (h * 83 + letters.indexOf(s[i]))
            }
            return h
            }

            For example, if we were trying to find the 5 letter string where hash(the_string) was 35502317995, the answer would be "cloud".)

            Please enter the one word solution below and attach your solution as a txt file to avoid filters. *
             */
    fun sitkaChallenge(activity: Activity): String {
        val sitkaHashOfCloud = sitkaHash(SITKA_CHALLENGE_HASH_EXAMPLE_WORD5)
        val sitkaHashReverseOfCloud = sitkaHashReverse(sitkaHashOfCloud, SITKA_CHALLENGE_HASH_EXAMPLE_WORD5.length)
        val sitkaHashReverseOfChallenge = sitkaHashReverse(SITKA_CHALLENGE_HASH_CHALLENGE, SITKA_CHALLENGE_HASH_CHALLENGE_WORD_LENGTH)

        //Copy the actual answer to the coding challenge to the clipboard for easy paste answering when this code is executed
        copyToClipboard(activity, sitkaHashReverseOfChallenge)

        return "sitkaHash of the word \'cloud\' is: $sitkaHashOfCloud\n" +
                "sitkaHashReverse of this \'$SITKA_CHALLENGE_HASH_EXAMPLE_WORD5\' hash " +
                "is hopefully still \'$SITKA_CHALLENGE_HASH_EXAMPLE_WORD5\': $sitkaHashReverseOfCloud\n" +
                "sitkaHashReverse of the hash \'$SITKA_CHALLENGE_HASH_CHALLENGE\' " +
                "for a word of length $SITKA_CHALLENGE_HASH_CHALLENGE_WORD_LENGTH is: $sitkaHashReverseOfChallenge"
    }

    @JvmStatic
    fun sitkaHash(stringToHash: String): Long {
        var hash: Long = SITKA_CHALLENGE_HASH_INITIAL_VALUE

        for (index in 0..(stringToHash.length - 1)) {
            hash = (hash * SITKA_CHALLENGE_HASH_FACTOR + SITKA_CHALLENGE_HASH_LETTERS.indexOf(stringToHash[index]))
        }
        return hash
    }

    @JvmStatic
    fun sitkaHashReverse(hashToStringify: Long, stringLength: Int): String {
        var cyclesToFindAnswer = 0
        var reversedHashString = ""
        val startTime: Long = Date().time
        for (index in 0..(stringLength - 1)) {

        }
        val endTime: Long = Date().time
        val secondsToReverseHash: Double = (endTime - startTime).toDouble() / 1000.0
        Timber.i(".sitkaHashReverse($hashToStringify, length = $stringLength) " +
                "took $cyclesToFindAnswer cycles to find the answer, in $secondsToReverseHash seconds.")
        return reversedHashString
    }


    @JvmStatic
    fun copyToClipboard(activity: Activity, toCopy: String?) {
        val clipLabel = "clipboardText"
        (activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).primaryClip = ClipData.newPlainText(clipLabel, toCopy)
        Toast.makeText(activity, "String \'$toCopy\' has been copied to the clipboard with label $clipLabel!", Toast.LENGTH_LONG).show()
    }
}
