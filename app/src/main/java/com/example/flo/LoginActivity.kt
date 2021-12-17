package com.example.flo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(), LoginView{
    lateinit var binding: ActivityLoginBinding



    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginSignUpTv.setOnClickListener{
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.loginLoginBtn.setOnClickListener{
            login()
        }

        binding.loginCloseIv.setOnClickListener{
            finish()
        }
    }



//    private fun login(){
//        if(binding.loginIdTv.text.toString().isEmpty() || binding.loginDirectInputTv.text.toString().isEmpty()){
//            Toast.makeText(this,"이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        if(binding.loginPwTv.text.toString().isEmpty()){
//            Toast.makeText(this,"비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val email: String = binding.loginIdTv.text.toString() + "@"+ binding.loginDirectInputTv.text.toString()
//        val pwd: String = binding.loginPwTv.text.toString()
//
//        val songDB = SongDatabase.getInstance(this)!!
//        val user = songDB.userDao().getUser(email,pwd)
//
//        user?.let {
//            Log.d("LOGINACT/GET_USER","userId: ${user.id},$user")
//            saveJwt(user.id)
//        }
//
//        //Toast.makeText(this,"회원정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
//    }

    private fun login(){
        if(binding.loginIdTv.text.toString().isEmpty() || binding.loginDirectInputTv.text.toString().isEmpty()){
            Toast.makeText(this,"이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        if(binding.loginPwTv.text.toString().isEmpty()){
            Toast.makeText(this,"비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val email= binding.loginIdTv.text.toString() + "@"+ binding.loginDirectInputTv.text.toString()
        val password = binding.loginPwTv.text.toString()

        val user= User(email, password, "")

        val authService = AuthService()
        authService.setLoginView(this)
        authService.login(user)
    }

    private fun startMainActivity() {
        val intent = Intent(this,MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private  fun saveJwt(jwt: Int){
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()

        editor.putInt("jwt",jwt)
        editor.apply()
    }

    override fun onLoginLoading() {
        binding.loginLoadingPb.visibility = View.VISIBLE
    }

    override fun onLoginSuccess(auth: Auth) {
        binding.loginLoadingPb.visibility = View.GONE

        saveJwt(this, auth.jwt)
        saveUserIdx(this,auth.userIdx)
        startMainActivity()
    }

    override fun onLoginFailure(code:Int,message:String) {
        binding.loginLoadingPb.visibility = View.GONE

        when(code){
            2015,2019,3014 ->{
                binding.loginErrorTv.visibility = View.VISIBLE
                binding.loginErrorTv.text = message
            }

        }
    }
}