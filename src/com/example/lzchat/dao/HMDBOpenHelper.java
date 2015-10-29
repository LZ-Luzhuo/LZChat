package com.example.lzchat.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HMDBOpenHelper extends SQLiteOpenHelper {

	private HMDBOpenHelper(Context context) {
		super(context, DB.NAME, null, DB.VERSION);
	}

	private static HMDBOpenHelper instance;

	public static HMDBOpenHelper getInstance(Context context) {
		if (instance == null) {
			synchronized (HMDBOpenHelper.class) {
				if (instance == null) {
					instance = new HMDBOpenHelper(context);
				}
			}
		}
		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DB.Account.SQL_CREATE_TABLE);
		db.execSQL(DB.Friend.SQL_CREATE_TABLE);
		db.execSQL(DB.Invitation.SQL_CREATE_TABLE);
		db.execSQL(DB.Message.SQL_CREATE_TABLE);
		db.execSQL(DB.Conversation.SQL_CREATE_TABLE);
		db.execSQL(DB.BackTask.SQL_CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
