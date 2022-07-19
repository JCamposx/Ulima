package pe.edu.ulima.pm.petclub

import android.app.Activity
import android.content.Intent
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat

fun Activity.toast(text: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, length).show()
}

fun Activity.setHintEditTextColor(
    editText: EditText,
    setFocus: Boolean = false,
    clearText: Boolean = false
) {
    editText.setHintTextColor(ContextCompat.getColor(this.applicationContext, R.color.red))

    if (setFocus) editText.requestFocus()

    if (clearText) editText.setText("")
}

fun Activity.goToActivity(activity: Activity, finish: Boolean = false) {
    startActivity(Intent(this, activity::class.java))

    if (finish) finish()
}