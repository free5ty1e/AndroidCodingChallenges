package com.primestationone.chrisprimeishcoding.androidcodingchallenges.utilities

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import timber.log.Timber
import java.math.BigDecimal
import java.math.MathContext
import java.util.*

/**
 * Created by chris on 9/13/17.
 */

object SitkaCodingChallenge {

    private const val SITKA_CHALLENGE_HASH_LETTERS = "acdekilmnoprstuy"
    private const val SITKA_CHALLENGE_HASH_FACTOR = 83
    private const val SITKA_CHALLENGE_HASH_INITIAL_VALUE = 9
    private const val SITKA_CHALLENGE_HASH_EXAMPLE_WORD5 = "cloud"
    private const val SITKA_CHALLENGE_HASH_CHALLENGE: Long = 1693941520599974437
    private const val SITKA_CHALLENGE_HASH_CHALLENGE_WORD_LENGTH = 9

    //Let's use Kotlin Extension methods here because it's fun (har)
    private fun BigDecimal.isWholeNumber(): Boolean = this.remainder(BigDecimal(1.0)) == BigDecimal.ZERO
    private fun BigDecimal.sitkaHashReverseOneStep(indexOfLetter: Int): BigDecimal = this.minus(BigDecimal(indexOfLetter)).divide(BigDecimal(83.0), MathContext.DECIMAL128)
    private fun String.sitkaHash(): Long {
        var hash: Long = SITKA_CHALLENGE_HASH_INITIAL_VALUE.toLong()
        for (index in 0..(this.length - 1)) {
            val currentChar = this[index]
            hash = (hash * SITKA_CHALLENGE_HASH_FACTOR.toLong() + SITKA_CHALLENGE_HASH_LETTERS.indexOf(currentChar))
        }
        return hash
    }


    /**
     * Looked at and started challenge 2017.09.13 @ ~2:10pm in between interviews
     *
     * Original scope estimate: I think I can complete this challenge this evening, in a couple hours.
     *

    Find a 9 letter string of characters that contains only letters from

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
        val sitkaHashOfCloud = SITKA_CHALLENGE_HASH_EXAMPLE_WORD5.sitkaHash()
        val sitkaHashReverseOfCloud = sitkaHashReverse(sitkaHashOfCloud, SITKA_CHALLENGE_HASH_EXAMPLE_WORD5.length)
        val sitkaHashReverseOfChallenge = sitkaHashReverse(SITKA_CHALLENGE_HASH_CHALLENGE, SITKA_CHALLENGE_HASH_CHALLENGE_WORD_LENGTH)

        //Copy the actual answer to the coding challenge to the clipboard for easy paste answering when this code is executed
        copyToClipboard(activity, sitkaHashReverseOfChallenge)


        //TODO: Turn the cloud test into unit tests

        return "sitkaHash of the word \'cloud\' is: $sitkaHashOfCloud\n" +
                "sitkaHashReverse of this \'$SITKA_CHALLENGE_HASH_EXAMPLE_WORD5\' hash " +
                "is hopefully still \'$SITKA_CHALLENGE_HASH_EXAMPLE_WORD5\': $sitkaHashReverseOfCloud\n" +
                "sitkaHashReverse of the hash \'$SITKA_CHALLENGE_HASH_CHALLENGE\' " +
                "for a word of length $SITKA_CHALLENGE_HASH_CHALLENGE_WORD_LENGTH is: $sitkaHashReverseOfChallenge"
    }

    /**
     * The theory for reversing this hash function, since it is known, is the following:
     * Take the hash value and divide it by the known hash factor after subtracting each possible character index value,
     *  based on the known string length.
     * One of the divisions will result in a whole number.  Use the character at this index and continue with the next cycle
     *  Theoretically, we will be left with the original string since we know the character set, the original length, the start hash value, and the hash factor
     */
    private fun sitkaHashReverse(hashToStringify: Long, stringLength: Int): String {
        var operationsToFindAnswer = 0
        val unhashedCharArray = CharArray(stringLength)
        var hashTempValue = BigDecimal(hashToStringify)

        val startTime: Long = Date().time
        for (index in 0..(stringLength - 1)) {
            val currentUnhashedStringCharIndex = stringLength - 1 - index
            var foundLetterThisCycle = false
            SITKA_CHALLENGE_HASH_LETTERS.forEachIndexed { indexOfLetter, letter ->
                if (!foundLetterThisCycle) {
                    val potentialPreviousHashValue = hashTempValue.sitkaHashReverseOneStep(indexOfLetter)
                    if (potentialPreviousHashValue.isWholeNumber() && potentialPreviousHashValue > BigDecimal.ZERO) {
                        unhashedCharArray[currentUnhashedStringCharIndex] = letter
                        hashTempValue = potentialPreviousHashValue
                        foundLetterThisCycle = true //Need to utilize a flag to limit results to one character per cycle, since forEachIndexed does not have break/continue functonality as a Kotlin Unit action object
                    }
                    operationsToFindAnswer++
                }
            }
        }
        val endTime: Long = Date().time
        val secondsToReverseHash: Double = (endTime - startTime).toDouble() / 1000.0
        val unhashedString = String(unhashedCharArray)
        Timber.v(".sitkaHashReverse($hashToStringify, length = $stringLength) " +
                "took $operationsToFindAnswer operations to find the answer ($unhashedString), in $secondsToReverseHash seconds.")
        return unhashedString
    }

    private fun copyToClipboard(activity: Activity, toCopy: String?) {
        val clipLabel = "clipboardText"
        (activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).primaryClip = ClipData.newPlainText(clipLabel, toCopy)
        Toast.makeText(activity, "String \'$toCopy\' has been copied to the clipboard with label $clipLabel!", Toast.LENGTH_LONG).show()
    }
}
