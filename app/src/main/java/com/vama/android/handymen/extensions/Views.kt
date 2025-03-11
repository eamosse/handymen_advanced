package com.vama.android.handymen.extensions

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.vama.android.data.utils.DataResult

fun <T : Any> Fragment.handle(result: DataResult<T>, transform: (T) -> Unit) {
    when (result) {
        is DataResult.Success -> transform(result.data)
        is DataResult.Error -> Toast.makeText( // TODO: Handle errors properly
            requireContext(),
            "Oops: ${result.message}",
            Toast.LENGTH_SHORT
        ).show()

        is DataResult.Working -> Toast.makeText( // TODO: Handle loading properly, display a loading for instance ?
            requireContext(),
            "Working...",
            Toast.LENGTH_SHORT
        ).show()
    }
}