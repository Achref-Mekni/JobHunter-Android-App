package com.esprit.jobhunter.SQLite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.esprit.jobhunter.Entity.Job
import java.util.*

class DatabaseHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    // Creating Tables
    override fun onCreate(db: SQLiteDatabase) {
        // create notes table
        db.execSQL(Job.CREATE_TABLE)
    }

    // Upgrading database
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Job.TABLE_NAME)

        // Create tables again
        onCreate(db)
    }

    fun deleteDB() {
        this.writableDatabase.execSQL("DROP TABLE jobs")
    }

    fun addDB() {
        this.writableDatabase.execSQL(Job.CREATE_TABLE)
    }

    fun insertJob(job: Job) {
        // get writable database as we want to write data
        val db = this.writableDatabase
        val values = ContentValues()
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Job.COLUMN_ID, job.id)
        values.put(Job.COLUMN_NAME, job.name)
        values.put(Job.COLUMN_LABEL, job.label)
        values.put(Job.COLUMN_DESCRIPTION, job.description)
        values.put(Job.COLUMN_START_DATE, job.start_date)
        values.put(Job.COLUMN_CONTRACT_TYPE, job.contract_type)
        values.put(Job.COLUMN_CAREER_REQ, job.career_req)
        values.put(Job.COLUMN_USER_ID, job.user_id)
        values.put(Job.COLUMN_SKILLS, job.skills)
        values.put(Job.COLUMN_COMPANY_NAME, job.company_name)
        values.put(Job.COLUMN_COMPANY_PIC, job.company_pic)
        values.put(Job.COLUMN_TYPE, job.type)
        // insert row
        val id = db.insert(Job.TABLE_NAME, null, values)
        // close db connection
        db.close()
    }

    fun getJob(id: Long): Job {
        // get readable database as we are not inserting anything
        val db = this.readableDatabase
        val cursor = db.query(Job.TABLE_NAME, arrayOf(Job.COLUMN_ID, Job.COLUMN_NAME, Job.COLUMN_LABEL, Job.COLUMN_DESCRIPTION, Job.COLUMN_START_DATE, Job.COLUMN_CONTRACT_TYPE, Job.COLUMN_CAREER_REQ, Job.COLUMN_USER_ID, Job.COLUMN_SKILLS, Job.COLUMN_COMPANY_NAME, Job.COLUMN_COMPANY_PIC, Job.COLUMN_TYPE),
                Job.COLUMN_ID + "=?", arrayOf(id.toString()), null, null, null, null)
        cursor?.moveToFirst()

        // prepare note object
        val job = Job(
                cursor!!.getInt(cursor.getColumnIndex(Job.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Job.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(Job.COLUMN_LABEL)),
                cursor.getString(cursor.getColumnIndex(Job.COLUMN_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndex(Job.COLUMN_START_DATE)),
                cursor.getString(cursor.getColumnIndex(Job.COLUMN_CONTRACT_TYPE)),
                cursor.getString(cursor.getColumnIndex(Job.COLUMN_CAREER_REQ)),
                cursor.getString(cursor.getColumnIndex(Job.COLUMN_USER_ID)),
                cursor.getString(cursor.getColumnIndex(Job.COLUMN_SKILLS)),
                cursor.getString(cursor.getColumnIndex(Job.COLUMN_COMPANY_NAME)),
                cursor.getString(cursor.getColumnIndex(Job.COLUMN_COMPANY_PIC)),
                cursor.getString(cursor.getColumnIndex(Job.COLUMN_TIMESTAMP))
        )

        // close the db connection
        cursor.close()
        return job
    }

    // Select All Query
    val allJobs: ArrayList<Job>
        get() {
            val jobs = ArrayList<Job>()

            // Select All Query
            val selectQuery = "SELECT  * FROM " + Job.TABLE_NAME + " ORDER BY " +
                    Job.COLUMN_TIMESTAMP + " DESC"
            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    val job = Job()
                    job.id = cursor.getInt(cursor.getColumnIndex(Job.COLUMN_ID))
                    job.name = cursor.getString(cursor.getColumnIndex(Job.COLUMN_NAME))
                    job.label = cursor.getString(cursor.getColumnIndex(Job.COLUMN_LABEL))
                    job.description = cursor.getString(cursor.getColumnIndex(Job.COLUMN_DESCRIPTION))
                    job.start_date = cursor.getString(cursor.getColumnIndex(Job.COLUMN_START_DATE))
                    job.contract_type = cursor.getString(cursor.getColumnIndex(Job.COLUMN_CONTRACT_TYPE))
                    job.career_req = cursor.getString(cursor.getColumnIndex(Job.COLUMN_CAREER_REQ))
                    job.user_id = cursor.getString(cursor.getColumnIndex(Job.COLUMN_USER_ID))
                    job.skills = cursor.getString(cursor.getColumnIndex(Job.COLUMN_SKILLS))
                    job.company_name = cursor.getString(cursor.getColumnIndex(Job.COLUMN_COMPANY_NAME))
                    job.company_pic = cursor.getString(cursor.getColumnIndex(Job.COLUMN_COMPANY_PIC))
                    job.inserted_sq_date = cursor.getString(cursor.getColumnIndex(Job.COLUMN_TIMESTAMP))
                    jobs.add(job)
                } while (cursor.moveToNext())
            }

            // close db connection
            db.close()

            return jobs
        }

    val jobsCount: Int
        get() {
            val countQuery = "SELECT  * FROM " + Job.TABLE_NAME
            val db = this.readableDatabase
            val cursor = db.rawQuery(countQuery, null)
            val count = cursor.count
            cursor.close()


            return count
        }


    fun deleteJob(job: Job) {
        val db = this.writableDatabase
        db.delete(Job.TABLE_NAME, Job.COLUMN_ID + " = ?", arrayOf(job.id.toString()))
        db.close()
    }

    companion object {
        // Database Version
        private const val DATABASE_VERSION = 1

        // Database Name
        private const val DATABASE_NAME = "bookmark_jo_db"
    }
}