package com.example.emsapp
import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor

data class Employee(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val address: String,
    val salary: Double,
    val designation: String
)

class EmployeeDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "EmployeeDB"
        private const val TABLE_NAME = "Employee"
        private const val COLUMN_ID = "id"
        private const val COLUMN_FIRST_NAME = "first_name"
        private const val COLUMN_LAST_NAME = "last_name"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_ADDRESS = "address"
        private const val COLUMN_SALARY = "salary"
        private const val COLUMN_DESIGNATION = "designation"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_FIRST_NAME + " TEXT,"
                + COLUMN_LAST_NAME + " TEXT,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_ADDRESS + " TEXT,"
                + COLUMN_SALARY + " REAL,"
                + COLUMN_DESIGNATION + " TEXT" + ")")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // CRUD operations
    fun addEmployee(employee: Employee): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_FIRST_NAME, employee.firstName)
        contentValues.put(COLUMN_LAST_NAME, employee.lastName)
        contentValues.put(COLUMN_EMAIL, employee.email)
        contentValues.put(COLUMN_ADDRESS, employee.address)
        contentValues.put(COLUMN_SALARY, employee.salary)
        contentValues.put(COLUMN_DESIGNATION, employee.designation)
        val success = db.insert(TABLE_NAME, null, contentValues)
        db.close()
        return success
    }

    @SuppressLint("Range")
    fun getEmployee(id: Int): Employee? {
        val db = this.readableDatabase
        val cursor: Cursor = db.query(
            TABLE_NAME, arrayOf(COLUMN_ID, COLUMN_FIRST_NAME, COLUMN_LAST_NAME, COLUMN_EMAIL,
                COLUMN_ADDRESS, COLUMN_SALARY, COLUMN_DESIGNATION),
            "$COLUMN_ID=?", arrayOf(id.toString()),
            null, null, null, null
        )
        if (cursor != null && cursor.moveToFirst()) {
            val employee = Employee(
                cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME)),
                cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME)),
                cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)),
                cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS)),
                cursor.getDouble(cursor.getColumnIndex(COLUMN_SALARY)),
                cursor.getString(cursor.getColumnIndex(COLUMN_DESIGNATION))
            )
            cursor.close()
            db.close()
            return employee
        }
        cursor.close()
        db.close()
        return null
    }

    fun updateEmployee(employee: Employee): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_FIRST_NAME, employee.firstName)
        contentValues.put(COLUMN_LAST_NAME, employee.lastName)
        contentValues.put(COLUMN_EMAIL, employee.email)
        contentValues.put(COLUMN_ADDRESS, employee.address)
        contentValues.put(COLUMN_SALARY, employee.salary)
        contentValues.put(COLUMN_DESIGNATION, employee.designation)
        val success = db.update(TABLE_NAME, contentValues, "$COLUMN_ID=?", arrayOf(employee.id.toString()))
        db.close()
        return success
    }

    fun deleteEmployee(id: Int): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()
        return success
    }

    @SuppressLint("Range")
    fun getAllEmployees(): List<Employee> {
        val employeeList: ArrayList<Employee> = ArrayList()
        val selectQuery = "SELECT  * FROM $TABLE_NAME"
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val employee = Employee(
                    cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS)),
                    cursor.getDouble(cursor.getColumnIndex(COLUMN_SALARY)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_DESIGNATION))
                )
                employeeList.add(employee)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return employeeList
    }
}
