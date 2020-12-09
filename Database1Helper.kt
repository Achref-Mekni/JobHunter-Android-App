package com.esprit.jobhunter.SQLite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.esprit.jobhunter.Entity.Internship
import java.util.*

class Database1Helper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    // Creating Tables
    override fun onCreate(db: SQLiteDatabase) {

        // create notes table
        db.execSQL(Internship.CREATE_TABLE)
    }

    // Upgrading database
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Internship.TABLE_NAME)

        // Create tables again
        onCreate(db)
    }

    fun insertInternship(job: Internship) {
        // get writable database as we want to write data
        val db = this.writableDatabase
        val values = ContentValues()
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Internship.COLUMN_ID, job.id)
        values.put(Internship.COLUMN_NAME, job.name)
        values.put(Internship.COLUMN_LABEL, job.label)
        values.put(Internship.COLUMN_DESCRIPTION, job.description)
        values.put(Internship.COLUMN_START_DATE, job.start_date)
        values.put(Internship.COLUMN_DURATION, job.duration)
        values.put(Internship.COLUMN_EDUC_REC, job.educ_req)
        values.put(Internship.COLUMN_USER_ID, job.user_id)
        values.put(Internship.COLUMN_SKILLS, job.skills)
        values.put(Internship.COLUMN_COMPANY_NAME, job.company_name)
        values.put(Internship.COLUMN_COMPANY_PIC, job.company_pic)
        // insert row
        val id = db.insert(Internship.TABLE_NAME, null, values)
        // close db connection
        db.close()
    }

    fun getInternship(id: Long): Internship {
        // get readable database as we are not inserting anything
        val db = this.readableDatabase
        val cursor = db.query(Internship.TABLE_NAME, arrayOf(Internship.COLUMN_ID, Internship.COLUMN_NAME, Internship.COLUMN_LABEL, Internship.COLUMN_DESCRIPTION, Internship.COLUMN_START_DATE, Internship.COLUMN_DURATION, Internship.COLUMN_EDUC_REC, Internship.COLUMN_USER_ID, Internship.COLUMN_SKILLS, Internship.COLUMN_COMPANY_NAME, Internship.COLUMN_COMPANY_PIC),
                Internship.COLUMN_ID + "=?", arrayOf(id.toString()), null, null, null, null)
        cursor?.moveToFirst()

        // prepare note object
        val job = Internship(
                cursor!!.getInt(cursor.getColumnIndex(Internship.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Internship.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(Internship.COLUMN_LABEL)),
                cursor.getString(cursor.getColumnIndex(Internship.COLUMN_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndex(Internship.COLUMN_START_DATE)),
                cursor.getString(cursor.getColumnIndex(Internship.COLUMN_DURATION)),
                cursor.getString(cursor.getColumnIndex(Internship.COLUMN_EDUC_REC)),
                cursor.getString(cursor.getColumnIndex(Internship.COLUMN_USER_ID)),
                cursor.getString(cursor.getColumnIndex(Internship.COLUMN_SKILLS)),
                cursor.getString(cursor.getColumnIndex(Internship.COLUMN_COMPANY_NAME)),
                cursor.getString(cursor.getColumnIndex(Internship.COLUMN_COMPANY_PIC)),
                cursor.getString(cursor.getColumnIndex(Internship.COLUMN_TIMESTAMP))
        )

        // close the db connection
        cursor.close()
        return job
    }

    // Select All Query
    val allInternships: ArrayList<Internship>
        get() {
            val jobs = ArrayList<Internship>()

            // Select All Query
            val selectQuery = "SELECT  * FROM " + Internship.TABLE_NAME + " ORDER BY " +
                    Internship.COLUMN_TIMESTAMP + " DESC"
            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    val job = Internship()
                    job.id = cursor.getInt(cursor.getColumnIndex(Internship.COLUMN_ID))
                    job.name = cursor.getString(cursor.getColumnIndex(Internship.COLUMN_NAME))
                    job.label = cursor.getString(cursor.getColumnIndex(Internship.COLUMN_LABEL))
                    job.description = cursor.getString(cursor.getColumnIndex(Internship.COLUMN_DESCRIPTION))
                    job.start_date = cursor.getString(cursor.getColumnIndex(Internship.COLUMN_START_DATE))
                    job.duration = cursor.getString(cursor.getColumnIndex(Internship.COLUMN_DURATION))
                    job.educ_req = cursor.getString(cursor.getColumnIndex(Internship.COLUMN_EDUC_REC))
                    job.user_id = cursor.getString(cursor.getColumnIndex(Internship.COLUMN_USER_ID))
                    job.skills = cursor.getString(cursor.getColumnIndex(Internship.COLUMN_SKILLS))
                    job.company_name = cursor.getString(cursor.getColumnIndex(Internship.COLUMN_COMPANY_NAME))
                    job.company_pic = cursor.getString(cursor.getColumnIndex(Internship.COLUMN_COMPANY_PIC))
                    job.inserted_sq_date = cursor.getString(cursor.getColumnIndex(Internship.COLUMN_TIMESTAMP))
                    jobs.add(job)
                } while (cursor.moveToNext())
            }

            // close db connection
            db.close()


            return jobs
        }


    val internshipsCount: Int
        get() {
            val countQuery = "SELECT  * FROM " + Internship.TABLE_NAME
            val db = this.readableDatabase
            val cursor = db.rawQuery(countQuery, null)
            val count = cursor.count
            cursor.close()



            return count
        }


    fun deleteInternship(job: Internship) {
        val db = this.writableDatabase
        db.delete(Internship.TABLE_NAME, Internship.COLUMN_ID + " = ?", arrayOf(job.id.toString()))
        db.close()
    }

    companion object {
        // Database Version
        private const val DATABASE_VERSION = 1

        // Database Name
        private const val DATABASE_NAME = "bookmark_internship_db"
    }
}