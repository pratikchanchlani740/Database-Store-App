package com.example.faculty.dbapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {
    public static String DATABASE_NAME = "myemployeedatabase";

    TextView textViewViewEmployees;
    EditText editTextName, editTextSalary;
    Spinner spinnerDepartment;

    SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewViewEmployees = (TextView) findViewById(R.id.textViewViewEmployees);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextSalary = (EditText) findViewById(R.id.editTextSalary);
        spinnerDepartment = (Spinner) findViewById(R.id.spinnerDepartment);

        findViewById(R.id.buttonAddEmployee).setOnClickListener(this);
        textViewViewEmployees.setOnClickListener(this);

        //creating a database
        mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

    }
    private boolean inputsAreCorrect(String name, String salary) {
        if (name.isEmpty()) {
            editTextName.setError("Please enter a name");
            editTextName.requestFocus();
            return false;
        }

        if (salary.isEmpty() || Integer.parseInt(salary) <= 0) {
            editTextSalary.setError("Please enter salary");
            editTextSalary.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonAddEmployee:
                createEmployeeTable();
                addEmployee();
                break;
            case R.id.textViewViewEmployees:
                startActivity(new Intent(this, EmployeeActivity.class));
                break;
        }
    }

    private void createEmployeeTable() {
        mDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS employees (\n" +
                        "    id int NOT NULL CONSTRAINT employees_pk PRIMARY KEY,\n" +
                        "    name varchar(200) NOT NULL,\n" +
                        "    department varchar(200) NOT NULL,\n" +
                        "    joiningdate datetime NOT NULL,\n" +
                        "    salary double NOT NULL\n" +
                        ");"
        );
    }

    private void addEmployee() {
        String name = editTextName.getText().toString().trim();
        String salary = editTextSalary.getText().toString().trim();
        String dept = spinnerDepartment.getSelectedItem().toString();
        String joiningDate = "28/03/2022";

        SharedPreferences sp = getSharedPreferences("id",MODE_PRIVATE);
        SharedPreferences.Editor prefEditor= sp.edit();
        Integer intEmpId = sp.getInt("id",0);
Log.d("intEmpId",intEmpId.toString());
        //validating the inptus
        if (inputsAreCorrect(name, salary)) {
            intEmpId = intEmpId + 1;
            String insertSQL = "INSERT INTO employees \n" +
                    "(id,name, department, joiningdate, salary)\n" +
                    "VALUES("+ intEmpId+",'"+name+"','"+dept+"',"+joiningDate+","+salary+");";
            prefEditor.putInt("id",intEmpId);
            prefEditor.commit();
            //using the same method execsql for inserting values
            //this time it has two parameters
            //first is the sql string and second is the parameters that is to be binded with the query
            //mDatabase.execSQL(insertSQL, new String[]{name, dept, joiningDate, salary});
            Log.d("insertQuery",insertSQL);
            mDatabase.execSQL(insertSQL);

            Toast.makeText(this, "Employee Added Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    public static class Employee {
        int id;
        String name, dept, joiningDate;
        double salary;

        public Employee(int id, String name, String dept, String joiningDate, double salary) {
            this.id = id;
            this.name = name;
            this.dept = dept;
            this.joiningDate = joiningDate;
            this.salary = salary;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDept() {
            return dept;
        }

        public String getJoiningDate() {
            return joiningDate;
        }

        public double getSalary() {
            return salary;
        }
    }
}
