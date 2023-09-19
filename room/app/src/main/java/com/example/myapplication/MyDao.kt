package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MyDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)  // INSERT, key 충돌이 나면 새 데이터로 교체
    fun insertStudent(student: Student)

    @Query("SELECT * FROM student_table")
    fun getAllStudents(): LiveData<List<Student>>        // LiveData<> 사용

    @Query("SELECT * FROM student_table WHERE name = :sname")   // 메소드 인자를 SQL문에서 :을 붙여 사용
    fun getStudentByName(sname: String): List<Student>

    @Delete
    fun deleteStudent(student: Student); // primary key is used to find the student

}