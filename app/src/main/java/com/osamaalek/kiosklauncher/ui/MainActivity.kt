package com.osamaalek.kiosklauncher.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.osamaalek.kiosklauncher.R
import com.osamaalek.kiosklauncher.util.KioskUtil
import android.view.WindowManager
import android.view.KeyEvent
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
        
        setContentView(R.layout.activity_main)

        KioskUtil.startKioskMode(this)
    }

    override fun onStart() {
        super.onStart()
        KioskUtil.startKioskMode(this)
    }
    override fun onResume() {
        super.onResume()
        // If we are in single app mode, we want to make sure the app is launched when we return to home
        // The HomeFragment.onResume() will handle the auto-launch if SINGLE_APP_PACKAGE is set.
    }
    override fun onBackPressed() {
        if (supportFragmentManager.findFragmentById(R.id.fragmentContainerView) is AppsListFragment) {
            supportFragmentManager.popBackStack()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            event?.startTracking()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyLongPress(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showPasswordDialog()
            return true
        }
        return super.onKeyLongPress(keyCode, event)
    }

    private fun showPasswordDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.exit_kiosk_mode))
        builder.setMessage(getString(R.string.enter_password))

        val input = EditText(this)
        input.hint = getString(R.string.password_hint)
        input.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        builder.setView(input)

        builder.setPositiveButton(getString(R.string.ok)) { _, _ ->
            val password = input.text.toString()
            if (password == "P@55wORd") {
                KioskUtil.stopKioskMode(this)
                finish()
            } else {
                Toast.makeText(this, getString(R.string.wrong_password), Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }
}