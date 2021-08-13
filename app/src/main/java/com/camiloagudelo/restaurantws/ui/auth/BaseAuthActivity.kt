package com.camiloagudelo.restaurantws.ui.auth

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


abstract class BaseAuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeBinding()

        observeFormState()
        observeAuthAction()
        setUpInputsValidations()
    }

    protected abstract fun initializeBinding()

    protected abstract fun observeFormState()

    protected abstract fun observeAuthAction()

    protected abstract fun setUpInputsValidations()

    protected fun showActionFailed(errorString: String) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}