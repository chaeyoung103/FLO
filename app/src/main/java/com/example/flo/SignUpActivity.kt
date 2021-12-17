package com.example.flo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySignupBinding
import retrofit2.*

class SignUpActivity : AppCompatActivity(), SignUpView{
    lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpBackIv.setOnClickListener{
            finish()
        }

        binding.signUpSignUpBtn.setOnClickListener{
            signUp()
            finish()
        }



    }

    private fun getUser(): User{
        val email: String = binding.signUpIdTv.text.toString() + "@"+ binding.signUpDirectInputTv.text.toString()
        val pwd: String = binding.signUpPwTv.text.toString()
        val name: String = binding.signUpNameTv.text.toString()

        return User(email,pwd,name)
    }

    private fun signUp(){
        if(binding.signUpIdTv.text.toString().isEmpty() || binding.signUpDirectInputTv.text.toString().isEmpty()){
            Toast.makeText(this,"이메일 형식이 잘못되었습니다.",Toast.LENGTH_SHORT).show()
            return
        }

        if(binding.signUpNameTv.text.toString().isEmpty()){
            Toast.makeText(this,"이름 형식이 잘못되었습니다.",Toast.LENGTH_SHORT).show()
            return
        }

        if(binding.signUpPwTv.text.toString() != binding.signUpPwCheckTv.text.toString()){
            Toast.makeText(this,"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show()
            return
        }

        val authService = AuthService()
        authService.setSignUpView(this)
        authService.signUp(getUser())

        Log.d("SIGNUPACT/ASYNC", "hello, ")
    }

    override fun onSignUpLoading() {
        binding.signUpLoadingPb.visibility = View.VISIBLE
    }

    override fun onSignUpSuccess() {
        binding.signUpLoadingPb.visibility = View.GONE

        finish()

    }

    override fun onSignUpFailure(code: Int, message: String) {
        binding.signUpLoadingPb.visibility = View.GONE

        when(code) {
            2016,2017->{
                binding.signUpEmailErrorTv.visibility = View.VISIBLE
                binding.signUpEmailErrorTv.text = message
            }
        }
    }
}