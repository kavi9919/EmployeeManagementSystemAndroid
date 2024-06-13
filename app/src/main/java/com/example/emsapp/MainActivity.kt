package com.example.emsapp
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.view.LayoutInflater
class MainActivity : AppCompatActivity() {

    private lateinit var employeeDatabaseHelper: EmployeeDatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var employeeAdapter: EmployeeAdapter
    private lateinit var fabAddEmployee: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        employeeDatabaseHelper = EmployeeDatabaseHelper(this)
        recyclerView = findViewById(R.id.recyclerViewEmployees)
        fabAddEmployee = findViewById(R.id.fabAddEmployee)

        recyclerView.layoutManager = LinearLayoutManager(this)
        loadEmployees()

        fabAddEmployee.setOnClickListener {
            showAddEditEmployeeBottomSheet(null)
        }
    }

    private fun loadEmployees() {
        val employees = employeeDatabaseHelper.getAllEmployees()
        employeeAdapter = EmployeeAdapter(
            context = this,
            employees = employees,
            onEditClick = { employee -> showAddEditEmployeeBottomSheet(employee) },
            onDeleteClick = { employee -> deleteEmployee(employee) }
        )
        recyclerView.adapter = employeeAdapter
    }


    private fun showAddEditEmployeeBottomSheet(employee: Employee?) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_add_employee, null)
        bottomSheetDialog.setContentView(view)

        val etFirstName = view.findViewById<android.widget.EditText>(R.id.etFirstName)
        val etLastName = view.findViewById<android.widget.EditText>(R.id.etLastName)
        val etEmail = view.findViewById<android.widget.EditText>(R.id.etEmail)
        val etAddress = view.findViewById<android.widget.EditText>(R.id.etAddress)
        val etSalary = view.findViewById<android.widget.EditText>(R.id.etSalary)
        val etDesignation = view.findViewById<android.widget.EditText>(R.id.etDesignation)
        val btnSave = view.findViewById<android.widget.Button>(R.id.btnSave)
        val tvFormTitle = view.findViewById<android.widget.TextView>(R.id.tvFormTitle)

        employee?.let {
            etFirstName.setText(it.firstName)
            etLastName.setText(it.lastName)
            etEmail.setText(it.email)
            etAddress.setText(it.address)
            etSalary.setText(it.salary.toString())
            etDesignation.setText(it.designation)
        }
        if (employee == null) {
            tvFormTitle.text  = "Add Employee"
        } else {
            tvFormTitle.text = "Edit Employee"
        }
        btnSave.setOnClickListener {
            val firstName = etFirstName.text.toString()
            val lastName = etLastName.text.toString()
            val email = etEmail.text.toString()
            val address = etAddress.text.toString()
            val salaryText = etSalary.text.toString()
            val designation = etDesignation.text.toString()

            // Validate fields
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || address.isEmpty() ||
                salaryText.isEmpty() || designation.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validate email
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validate salary
            val salary = salaryText.toDoubleOrNull()
            if (salary == null || salary <= 0) {
                Toast.makeText(this, "Invalid salary", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (employee == null) {
                val newEmployee = Employee(0, firstName, lastName, email, address, salary, designation)
                employeeDatabaseHelper.addEmployee(newEmployee)
                Toast.makeText(this, "Employee Added", Toast.LENGTH_SHORT).show()
            } else {
                val updatedEmployee = Employee(employee.id, firstName, lastName, email, address, salary, designation)
                employeeDatabaseHelper.updateEmployee(updatedEmployee)
                Toast.makeText(this, "Employee Updated", Toast.LENGTH_SHORT).show()
            }
            loadEmployees()
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    private fun deleteEmployee(employee: Employee) {
        val dialog = android.app.AlertDialog.Builder(this)
        dialog.setTitle("Delete Employee")
        dialog.setMessage("Are you sure you want to delete ${employee.firstName} ${employee.lastName}?")
        dialog.setPositiveButton("Yes") { _, _ ->
            employeeDatabaseHelper.deleteEmployee(employee.id)
            Toast.makeText(this, "Employee Deleted", Toast.LENGTH_SHORT).show()
            loadEmployees()
        }
        dialog.setNegativeButton("No", null)
        dialog.show()
    }
}
