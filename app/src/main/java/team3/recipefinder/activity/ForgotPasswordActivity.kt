package team3.recipefinder.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import team3.recipefinder.R

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var emailEt: EditText
    private lateinit var emailMat: TextInputLayout

    private lateinit var resetPasswordBtn: Button
    private lateinit var back: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgot_password_activity)

        auth = FirebaseAuth.getInstance()

        emailEt = findViewById(R.id.edit_email_reset)
        emailMat = findViewById(R.id.edit_email_material_reset)

        resetPasswordBtn = findViewById(R.id.button_reset_pass)
        back = findViewById(R.id.button_cancel)

        back.setOnClickListener {
            switchToLogin()
        }

        resetPasswordBtn.setOnClickListener {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailEt.text).matches()) {
                emailMat.isErrorEnabled = true
                emailMat.error = "Please enter a valid email"
            } else {
                var email: String = emailEt.text.toString()
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(this, "Please enter email id", Toast.LENGTH_LONG).show()
                } else {
                    auth.sendPasswordResetEmail(email)
                        .addOnSuccessListener(
                            this,
                            OnSuccessListener() {
                                Toast.makeText(
                                    this,
                                    "Reset link sent to your email",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }
                        ).addOnFailureListener(
                            this,
                            OnFailureListener() {
                                emailMat.isErrorEnabled = true
                                Toast.makeText(this, "Unable to send reset mail", Toast.LENGTH_LONG)
                                    .show()
                            }
                        )
                }
            }
        }
    }

    override fun onBackPressed() {
        switchToLogin()
    }

    private fun switchToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
