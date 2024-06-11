package com.example.emsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class EmployeeDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_details)

        // Get data from Intent
        val firstName = intent.getStringExtra("FIRST_NAME")
        val lastName = intent.getStringExtra("LAST_NAME")
        val email = intent.getStringExtra("EMAIL")
        val address = intent.getStringExtra("ADDRESS")
        val salary = intent.getDoubleExtra("SALARY", 0.0)
        val designation = intent.getStringExtra("DESIGNATION")

        // Bind data to views
        findViewById<TextView>(R.id.tvFirstName).text = firstName
        findViewById<TextView>(R.id.tvLastName).text = lastName
        findViewById<TextView>(R.id.tvEmail).text = email
        findViewById<TextView>(R.id.tvAddress).text = address
        findViewById<TextView>(R.id.tvSalary).text = salary.toString()
        findViewById<TextView>(R.id.tvDesignation).text = designation
    }
}
