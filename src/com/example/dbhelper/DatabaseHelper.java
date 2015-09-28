package com.example.dbhelper;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

	// Logcat tag
	private static final String LOG = "DatabaseHelper";

	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "myDB";

	// Table Names
	private static final String TABLE_QUESTIONS="questions";
	private static final String TABLE_LESSONS="lessons";
	private static final String TABLE_ANSWERS="answers";
	private static final String TABLE_GAMES="game";

	// GAMES Table - column nmaes
	private static final String COLUMN_GAMES_ID="id";
	private static final String COLUMN_GAMES_THEGAME="thegame";
	
	// QUESTIONS Table - column nmaes
	private static final String COLUMN_QUESTIONS_ID="id";
	private static final String COLUMN_QUESTIONS_LESSONID="lessonid";
	private static final String COLUMN_QUESTIONS_QUESTION="question";

	// LESSONS Table - column names
	private static final String COLUMN_LESSONS_ID="id";
	private static final String COLUMN_LESSONS_LESSONNAME="lessonname";
	private static final String COLUMN_LESSONS_CLASS="class";

	// ANSWERS Table - column names
	private static final String COLUMN_ANSWERS_ID="id";
	private static final String COLUMN_ANSWERS_THEANSWER="theanswer";
	private static final String COLUMN_ANSWERS_POINTS="points";
	private static final String COLUMN_ANSWERS_QUESTIONID="questionid";

	// Table Create Statements
	// Todo table create statement

	private static final String CREATE_TABLE_QUESTIONS = "CREATE TABLE " + TABLE_QUESTIONS
			+ "(" + COLUMN_QUESTIONS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_QUESTIONS_LESSONID + " INTEGER," + COLUMN_QUESTIONS_QUESTION + " TEXT" + ")";

	// Tag table create statement
	private static final String CREATE_TABLE_LESSONS = "CREATE TABLE " + TABLE_LESSONS
			+ "(" + COLUMN_LESSONS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_LESSONS_LESSONNAME + " TEXT," + COLUMN_LESSONS_CLASS + " TEXT" + ")";

	// Tag table create statement
	private static final String CREATE_TABLE_ANSWERS = "CREATE TABLE " + TABLE_ANSWERS
			+ "(" + COLUMN_ANSWERS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_ANSWERS_THEANSWER + " TEXT," + COLUMN_ANSWERS_POINTS + " INTEGER," + COLUMN_ANSWERS_QUESTIONID + " INTEGER" + ")";

	private static final String CREATE_TABLE_GAMES = "CREATE TABLE " + TABLE_GAMES
			+ "(" + COLUMN_GAMES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_GAMES_THEGAME + " TEXT " + ")";

	
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// creating required tables
		db.execSQL(CREATE_TABLE_QUESTIONS);
		db.execSQL(CREATE_TABLE_LESSONS);
		db.execSQL(CREATE_TABLE_ANSWERS);
		db.execSQL(CREATE_TABLE_GAMES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LESSONS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANSWERS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMES);
		// create new tables
		onCreate(db);
	}

	// ------------------------ "todos" table methods ----------------//

	
	public void createGame() {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_GAMES_THEGAME, "A");
		// insert row
		db.insert(TABLE_GAMES, null, values);
	}
	
	public int getGame() {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<Integer> UserslistLessons=new ArrayList<Integer>();
		String selectQuery;
		selectQuery = "SELECT " +COLUMN_GAMES_ID+ " FROM " + TABLE_GAMES;

		Log.e(LOG, selectQuery);

		Cursor c = db.rawQuery(selectQuery, null);
		UserslistLessons.clear();
		if (c.moveToFirst()) {
			do {
				// adding to Node list
				UserslistLessons.add(c.getInt(c.getColumnIndex(COLUMN_GAMES_ID)));
			} while (c.moveToNext());
		}
		if(UserslistLessons.size()==0)
			return -1;
		return UserslistLessons.get(UserslistLessons.size()-1);
	}
	
	
	
	/*
	 * Creating a Question
	 */
	public void createQuestion(int id , int lid , String username) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_QUESTIONS_ID, id);
		values.put(COLUMN_QUESTIONS_LESSONID, lid);
		values.put(COLUMN_QUESTIONS_QUESTION, username);

		// insert row
		db.insert(TABLE_QUESTIONS, null, values);
	}

	/*
	 * get questions
	 */
	public ArrayList<String> getQuestionsByLesson(int lid) {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<String> UserslistQuestions=new ArrayList<String>();
		String selectQuery;
		if(lid==0)
			selectQuery = "SELECT " +COLUMN_QUESTIONS_QUESTION+ " FROM " + TABLE_QUESTIONS;
		else
			selectQuery = "SELECT " +COLUMN_QUESTIONS_QUESTION+ " FROM " + TABLE_QUESTIONS + " WHERE "
				+ COLUMN_QUESTIONS_LESSONID + " = '" + lid +"'";

		Log.e(LOG, selectQuery);

		Cursor c = db.rawQuery(selectQuery, null);
		UserslistQuestions.clear();
		if (c.moveToFirst()) {
			do {
				// adding to Node list
				UserslistQuestions.add(c.getString(c.getColumnIndex(COLUMN_QUESTIONS_QUESTION)));
			} while (c.moveToNext());
		}
		return UserslistQuestions;
	}

	public int getQuestionIdByName(String q) {
		SQLiteDatabase db = this.getReadableDatabase();
		int id = 0;
		String selectQuery;
		selectQuery = "SELECT " +COLUMN_QUESTIONS_ID+ " FROM " + TABLE_QUESTIONS + " WHERE "
				+ COLUMN_QUESTIONS_QUESTION + " = '" + q +"'";

		Log.e(LOG, selectQuery);

		Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			do {
				// adding to Node list
				id=c.getInt(c.getColumnIndex(COLUMN_QUESTIONS_ID));
			} while (c.moveToNext());
		}
		return id;
	}
	
	public void deleteQuestion() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_QUESTIONS, null,null);
	}


	// ------------------------ "tags" table methods ----------------//

	/*
	 * Creating tag
	 */
	public void createLesson(int id,String name,String clas) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_LESSONS_ID, id);
		values.put(COLUMN_LESSONS_LESSONNAME, name);
		values.put(COLUMN_LESSONS_CLASS, clas);

		// insert row
		db.insert(TABLE_LESSONS, null, values);

	}

	public int getLessonIdByName(String name) {
		SQLiteDatabase db = this.getReadableDatabase();
		int id = 0;
		String selectQuery = "SELECT " + COLUMN_LESSONS_ID + " FROM " + TABLE_LESSONS + " WHERE "
				+ COLUMN_LESSONS_LESSONNAME + " = '" + name +"'";

		Log.e(LOG, selectQuery);

		Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			do {
				id =c.getInt(c.getColumnIndex(COLUMN_LESSONS_ID));
				
				
				// adding to Node list
			} while (c.moveToNext());
		}
		return id;
	}
	
	public ArrayList<String> getLessons() {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<String> UserslistLessons=new ArrayList<String>();
		String selectQuery;
		selectQuery = "SELECT " +COLUMN_LESSONS_LESSONNAME+ " FROM " + TABLE_LESSONS;

		Log.e(LOG, selectQuery);

		Cursor c = db.rawQuery(selectQuery, null);
		UserslistLessons.clear();
		if (c.moveToFirst()) {
			do {
				// adding to Node list
				UserslistLessons.add(c.getString(c.getColumnIndex(COLUMN_LESSONS_LESSONNAME)));
			} while (c.moveToNext());
		}
		return UserslistLessons;
	}
	
	public void deleteLessons() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_LESSONS, null,null);
	}


	// closing database
	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}

	


/*
	 * Creating tag
	 */
	public void createAnswer(int id,String answer,int points,int qid) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_ANSWERS_ID, id);
		values.put(COLUMN_ANSWERS_THEANSWER, answer);
		values.put(COLUMN_ANSWERS_POINTS, points);
		values.put(COLUMN_ANSWERS_QUESTIONID, qid);

		// insert row
		db.insert(TABLE_ANSWERS, null, values);

	}

	public ArrayList<String> getAnswersByQuestionId(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<String> UserslistAnswers=new ArrayList<String>();
		String selectQuery = "SELECT " + COLUMN_ANSWERS_THEANSWER +" FROM " + TABLE_ANSWERS + " WHERE "
				+ COLUMN_ANSWERS_QUESTIONID + " = '" + id +"'";

		Log.e(LOG, selectQuery);

		Cursor c = db.rawQuery(selectQuery, null);
		UserslistAnswers.clear();
		if (c.moveToFirst()) {
			do {
				// adding to Node list
				UserslistAnswers.add(c.getString(c.getColumnIndex(COLUMN_ANSWERS_THEANSWER)));
			} while (c.moveToNext());
		}
		return UserslistAnswers;
	}

	public int getPointsByAnswer(String ans,int que) {
		SQLiteDatabase db = this.getReadableDatabase();
		int p = 0;
		String selectQuery = "SELECT "+COLUMN_ANSWERS_POINTS+" FROM " + TABLE_ANSWERS + " WHERE "
				+ COLUMN_ANSWERS_THEANSWER + " = '" + ans +"' and "+COLUMN_ANSWERS_QUESTIONID+" = '"+que+"'";

		Log.e(LOG, selectQuery);

		Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			do {
				// adding to Node list
				p=c.getInt(c.getColumnIndex(COLUMN_ANSWERS_POINTS));
			} while (c.moveToNext());
		}
		return p;
	}
	
	public void deleteAnswer() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_ANSWERS, null,null);
	}
}