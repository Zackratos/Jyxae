package com.zackratos.jyxae

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zackratos.jyxae.databinding.ActivityMainBinding
import com.zackratos.ultimatebarx.ultimatebarx.UltimateBarX

class MainActivity : AppCompatActivity() {


    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val mainFragment by lazy { MainFragment.newInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        UltimateBarX.with(this).transparent().applyStatusBar()
        supportFragmentManager.beginTransaction()
            .add(R.id.flContainer, mainFragment)
            .commit()
    }


    override fun onBackPressed() {
        if (mainFragment.onBackPressed()) {
            return
        }
        super.onBackPressed()
    }
}