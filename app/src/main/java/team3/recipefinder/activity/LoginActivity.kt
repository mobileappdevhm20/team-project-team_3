package team3.recipefinder.activity

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import team3.recipefinder.MainActivity
import team3.recipefinder.R

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var emailEt: EditText
    private lateinit var emailMat: TextInputLayout
    private lateinit var passwordEt: EditText
    private lateinit var passwordMat: TextInputLayout

    private lateinit var signupBtn: Button
    private lateinit var loginBtn: Button

    private lateinit var resetPasswordTv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val loginTextView: View = findViewById(R.id.login_title)
            loginTextView.visibility = View.GONE
        }

        emailEt = findViewById(R.id.edit_email_login)
        emailMat = findViewById(R.id.edit_email_material_login)
        passwordEt = findViewById(R.id.edit_pass_login)
        passwordMat = findViewById(R.id.edit_pass_material_login)

        signupBtn = findViewById(R.id.button_signup)
        loginBtn = findViewById(R.id.button_login)

        resetPasswordTv = findViewById(R.id.devider_reset_pass)

        auth = FirebaseAuth.getInstance()

        loginBtn.setOnClickListener {
            val email: String = emailEt.text.toString()
            val password: String = passwordEt.text.toString()

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                emailMat.isErrorEnabled = true
                emailMat.error = " "
                passwordMat.isErrorEnabled = true
                passwordMat.error = " "
                Toast.makeText(this@LoginActivity, "Please fill all the fields", Toast.LENGTH_LONG)
                    .show()
            } else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(
                        this,
                        OnSuccessListener() {
                            Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_LONG).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    ).addOnFailureListener(
                        this,
                        OnFailureListener() {
                            emailMat.isErrorEnabled = true
                            emailMat.error = " "
                            passwordMat.isErrorEnabled = true
                            passwordMat.error = " "
                            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show()
                        }
                    )
            }
        }

        signupBtn.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }

        resetPasswordTv.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }
}
