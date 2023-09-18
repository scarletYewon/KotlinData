package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    lateinit var myDao: MyDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        myDao = MyDatabase.getDatabase(this).getMyDao()

        val allStudents = myDao.getAllStudents()
        allStudents.observe(this) {
            val str = StringBuilder().apply {
                for ((id, name) in it) {
                    append(id)
                    append("-")
                    append(name)
                    append("\n")
                }
            }.toString()
            binding.textStudentList.text = str
        }

        binding.addStudent.setOnClickListener {
            val id = binding.editStudentId.text.toString().toInt()
            val name = binding.editStudentName.text.toString()
            if (id > 0 && name.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    myDao.insertStudent(Student(id, name))
                }
            }

            binding.editStudentId.text = null
            binding.editStudentName.text = null
        }

        binding.queryStudent.setOnClickListener {
            val name = binding.editStudentName.text.toString()
            CoroutineScope(Dispatchers.IO).launch {

                val results = myDao.getStudentByName(name)

                if (results.isNotEmpty()) {
                    val str = StringBuilder().apply {
                        results.forEach { student ->
                            append(student.id)
                            append("-")
                            append(student.name)
                        }
                    }
                    withContext(Dispatchers.Main) {
                        binding.textQueryStudent.text = str
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        binding.textQueryStudent.text = ""
                    }
                }
            }
        }
    }
}