package com.example.emsapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EmployeeAdapter(
    private val context: MainActivity,
    private val employees: List<Employee>,
    private val onEditClick: (Employee) -> Unit,
    private val onDeleteClick: (Employee) -> Unit
) : RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>() {

    inner class EmployeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvEmployeeName: TextView = itemView.findViewById(R.id.tvEmployeeName)
        val tvEmployeeEmail: TextView = itemView.findViewById(R.id.tvEmployeeEmail)
        val btnViewMore: Button = itemView.findViewById(R.id.btnViewMore)
        val btnEdit: Button = itemView.findViewById(R.id.btnEdit)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.employee_item, parent, false)
        return EmployeeViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val employee = employees[position]
        holder.tvEmployeeName.text = "${employee.firstName} ${employee.lastName}"
        holder.tvEmployeeEmail.text = employee.email

        holder.btnViewMore.setOnClickListener {
            val intent = Intent(context, EmployeeDetailsActivity::class.java).apply {
                putExtra("FIRST_NAME", employee.firstName)
                putExtra("LAST_NAME", employee.lastName)
                putExtra("EMAIL", employee.email)
                putExtra("ADDRESS", employee.address)
                putExtra("SALARY", employee.salary)
                putExtra("DESIGNATION", employee.designation)
            }
            context.startActivity(intent)
        }

        holder.btnEdit.setOnClickListener {
            onEditClick(employee)
        }
        holder.btnDelete.setOnClickListener {
            onDeleteClick(employee)
        }
    }

    override fun getItemCount(): Int = employees.size
}
