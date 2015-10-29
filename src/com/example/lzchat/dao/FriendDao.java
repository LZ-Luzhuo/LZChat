package com.example.lzchat.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.lzchat.bean.Friend;

public class FriendDao {
	private HMDBOpenHelper helper;

	public FriendDao(Context context) {
		helper = HMDBOpenHelper.getInstance(context);
	}

	public Cursor queryFriends(String owner) {
		SQLiteDatabase db = helper.getReadableDatabase();

		String sql = "select * from " + DB.Friend.TABLE_NAME + " where "
				+ DB.Friend.COLUMN_OWNER + "=?";
		return db.rawQuery(sql, new String[] { owner });
	}

	public Friend queryFriendByAccount(String owner, String account) {
		SQLiteDatabase db = helper.getReadableDatabase();

		String sql = "select * from " + DB.Friend.TABLE_NAME + " where "
				+ DB.Friend.COLUMN_OWNER + "=? and "
				+ DB.Friend.COLUMN_ACCOUNT + "=?";
		Cursor cursor = db.rawQuery(sql, new String[] { owner, account });
		if (cursor != null) {
			Friend friend = null;
			if (cursor.moveToNext()) {
				String alpha = cursor.getString(cursor
						.getColumnIndex(DB.Friend.COLUMN_ALPHA));
				String area = cursor.getString(cursor
						.getColumnIndex(DB.Friend.COLUMN_AREA));
				String icon = cursor.getString(cursor
						.getColumnIndex(DB.Friend.COLUMN_ICON));
				String name = cursor.getString(cursor
						.getColumnIndex(DB.Friend.COLUMN_NAME));
				String nickName = cursor.getString(cursor
						.getColumnIndex(DB.Friend.COLUMN_NICKNAME));
				int sex = cursor.getInt(cursor
						.getColumnIndex(DB.Friend.COLUMN_SEX));
				String sign = cursor.getString(cursor
						.getColumnIndex(DB.Friend.COLUMN_SIGN));
				int sort = cursor.getInt(cursor
						.getColumnIndex(DB.Friend.COLUMN_SORT));

				friend = new Friend();
				friend.account = (account);
				friend.alpha = (alpha);
				friend.area = (area);
				friend.icon = (icon);
				friend.name = (name);
				friend.nickName = (nickName);
				friend.owner = (owner);
				friend.sex = (sex);
				friend.sign = (sign);
				friend.sort = (sort);
			}
			cursor.close();
			return friend;
		}
		return null;
	}

	public void addFriend(Friend friend) {
		SQLiteDatabase db = helper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(DB.Friend.COLUMN_ACCOUNT, friend.account);
		values.put(DB.Friend.COLUMN_ALPHA, friend.alpha);
		values.put(DB.Friend.COLUMN_AREA, friend.area);
		values.put(DB.Friend.COLUMN_ICON, friend.icon);
		values.put(DB.Friend.COLUMN_NAME, friend.name);
		values.put(DB.Friend.COLUMN_NICKNAME, friend.nickName);
		values.put(DB.Friend.COLUMN_OWNER, friend.owner);
		values.put(DB.Friend.COLUMN_SEX, friend.sex);
		values.put(DB.Friend.COLUMN_SIGN, friend.sign);
		values.put(DB.Friend.COLUMN_SORT, friend.sort);

		friend.id = (db.insert(DB.Friend.TABLE_NAME, null, values));
	}

	public void updateFriend(Friend friend) {
		SQLiteDatabase db = helper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(DB.Friend.COLUMN_ACCOUNT, friend.account);
		values.put(DB.Friend.COLUMN_ALPHA, friend.alpha);
		values.put(DB.Friend.COLUMN_AREA, friend.area);
		values.put(DB.Friend.COLUMN_ICON, friend.icon);
		values.put(DB.Friend.COLUMN_NAME, friend.name);
		values.put(DB.Friend.COLUMN_NICKNAME, friend.nickName);
		values.put(DB.Friend.COLUMN_OWNER, friend.owner);
		values.put(DB.Friend.COLUMN_SEX, friend.sex);
		values.put(DB.Friend.COLUMN_SIGN, friend.sign);
		values.put(DB.Friend.COLUMN_SORT, friend.sort);

		String whereClause = DB.Friend.COLUMN_ID + "=?";
		String[] whereArgs = new String[] { friend.id + ""};
		db.update(DB.Friend.TABLE_NAME, values, whereClause, whereArgs);
	}
}
