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

    const val SITKA_CHALLENGE_HASH_LETTERS = "acdekilmnoprstuy"
    const val SITKA_CHALLENGE_HASH_FACTOR = 83
    const val SITKA_CHALLENGE_HASH_INITIAL_VALUE = 9
    const val SITKA_CHALLENGE_HASH_EXAMPLE_WORD5 = "cloud"
    const val SITKA_CHALLENGE_HASH_CHALLENGE: Long = 1693941520599974437
    const val SITKA_CHALLENGE_HASH_CHALLENGE_WORD_LENGTH = 9

    //Let's use Kotlin Extension methods here because it's fun (har)
    private fun BigDecimal.isWholeNumber(): Boolean = this.remainder(BigDecimal(1.0)) == BigDecimal.ZERO
    private fun BigDecimal.sitkaUnhash(indexOfLetter: Int): BigDecimal = this.minus(BigDecimal(indexOfLetter)).divide(BigDecimal(83.0), MathContext.DECIMAL128)

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
        val sitkaHashOfCloud = sitkaHash(SITKA_CHALLENGE_HASH_EXAMPLE_WORD5)
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

    fun sitkaHash(stringToHash: String): Long {
        var hash: Long = SITKA_CHALLENGE_HASH_INITIAL_VALUE.toLong()
        for (index in 0..(stringToHash.length - 1)) {
            val currentChar = stringToHash[index]
            hash = (hash * SITKA_CHALLENGE_HASH_FACTOR.toLong() + SITKA_CHALLENGE_HASH_LETTERS.indexOf(currentChar))
            Timber.d(".sitkaHash($stringToHash): hash value after currentChar $currentChar with index $index is currently: $hash")
        }
        return hash
    }

    /**
     * The theory for reversing this hash function, since it is known, is the following:
     * Take the hash value and divide it by the known hash factor after subtracting each possible character index value,
     *  based on the known string length.
     * Take note of the indices that result in a whole number from this division;
     *  these are the only ones we need continue on with for performance's sake
     * Iterate downward through the hash function, noting possible solutions that result in whole number division all the way down
     *  until the last division results in the known hash start value.
     *  Theoretically, we will be left with the original string since we know the character set, the original length and the hash factor
     */
    fun sitkaHashReverse(hashToStringify: Long, stringLength: Int): String {
        var operationsToFindAnswer = 0
        val unhashedString = CharArray(stringLength)
        var hashTempValue = BigDecimal(hashToStringify)
        val startTime: Long = Date().time


//        for (index in 0..(stringLength - 1)) {
        val index = 0   //TODO: DELETEME ONCE USING LOOP AGAIN

            val lettersToTryWithPreviousHash: MutableMap<Char, Long> = mutableMapOf()
            SITKA_CHALLENGE_HASH_LETTERS.forEachIndexed { indexOfLetter, letter ->
                val potentialPreviousHashValue = hashTempValue.sitkaUnhash(indexOfLetter)
                if (potentialPreviousHashValue.isWholeNumber()) {
                    Timber.v(".sitkaHashReverse(): found a whole number for $letter: $potentialPreviousHashValue")
                    lettersToTryWithPreviousHash[letter] = potentialPreviousHashValue.toLong()
                } else {
                    Timber.v(".sitkaHashReverse(): not a whole number for $letter")
                }
                operationsToFindAnswer++
            }
            val currentUnhashedStringCharIndex = stringLength - 1 - index
            Timber.d(".sitkaHashReverse($hashToStringify, length = $stringLength): " +
                    "Character index $currentUnhashedStringCharIndex - # letters to try = ${lettersToTryWithPreviousHash.size}, letters to try are $lettersToTryWithPreviousHash")

//        }
        val endTime: Long = Date().time
        val secondsToReverseHash: Double = (endTime - startTime).toDouble() / 1000.0
        Timber.i(".sitkaHashReverse($hashToStringify, length = $stringLength) " +
                "took $operationsToFindAnswer operations to find the answer, in $secondsToReverseHash seconds.")
        return unhashedString.toString()
    }

    fun isWholeNumber(potentialPreviousHashValue: Double) =
            potentialPreviousHashValue % 1.0 == 0.0

    fun isWholeNumber(potentialPreviousHashValue: Float) =
            potentialPreviousHashValue % 1.0 == 0.0

    fun copyToClipboard(activity: Activity, toCopy: String?) {
        val clipLabel = "clipboardText"
        (activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).primaryClip = ClipData.newPlainText(clipLabel, toCopy)
        Toast.makeText(activity, "String \'$toCopy\' has been copied to the clipboard with label $clipLabel!", Toast.LENGTH_LONG).show()
    }
}
