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
import com.google.firebase.auth.FirebaseAuth
import team3.recipefinder.MainActivity
import team3.recipefinder.R

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var emailEt: EditText
    private lateinit var passwordEt: EditText

    private lateinit var signUpBtn: Button
    private lateinit var loginBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_activity)

        auth = FirebaseAuth.getInstance()

        emailEt = findViewById(R.id.edit_email)
        passwordEt = findViewById(R.id.edit_pass)

        loginBtn = findViewById(R.id.button_login)
        signUpBtn = findViewById(R.id.button_signup)

        signUpBtn.setOnClickListener {
            val email: String = emailEt.text.toString()
            val password: String = passwordEt.text.toString()

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_LONG).show()
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(
                        this,
                        OnSuccessListener() {
                            Toast.makeText(
                                this,
                                "Successfully Registered", Toast.LENGTH_LONG
                            ).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    )
                    .addOnFailureListener(
                        this,
                        OnFailureListener() {
                            Toast.makeText(this, "Registration Failed", Toast.LENGTH_LONG).show()
                        }
                    )
            }
        }

        loginBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
