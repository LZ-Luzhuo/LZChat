package com.example.lzchat.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.lzchat.bean.Conversation;
import com.example.lzchat.bean.Message;

public class MessageDao {
	private HMDBOpenHelper helper;

	public MessageDao(Context context) {
		helper = HMDBOpenHelper.getInstance(context);
	}

	public void addMessage(Message message) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DB.Message.COLUMN_ACCOUNT, message.account);
		values.put(DB.Message.COLUMN_CONTENT, message.content);
		values.put(DB.Message.COLUMN_CREATE_TIME, message.createTime);
		values.put(DB.Message.COLUMN_DIRECTION, message.direction);
		values.put(DB.Message.COLUMN_OWNER, message.owner);
		values.put(DB.Message.COLUMN_STATE, message.state);
		values.put(DB.Message.COLUMN_TYPE, message.type);
		values.put(DB.Message.COLUMN_URL, message.url);
		values.put(DB.Message.COLUMN_READ, message.read ? 1 : 0);

		message.id = (db.insert(DB.Message.TABLE_NAME, null, values));

		String sql = "select * from " + DB.Conversation.TABLE_NAME
				+ " where " + DB.Conversation.COLUMN_ACCOUNT + "=? and "
				+ DB.Conversation.COLUMN_OWNER + "=?";
		Cursor cursor = db.rawQuery(sql, new String[] { message.account,
				message.owner});
		if (cursor != null && cursor.moveToNext()) {

			// 关闭cursor
			cursor.close();
			cursor = null;

			int unread = 0;

			sql = "select count(_id) from " + DB.Message.TABLE_NAME
					+ " where " + DB.Message.COLUMN_READ + "=0 and "
					+ DB.Message.COLUMN_ACCOUNT + "=? and "
					+ DB.Message.COLUMN_OWNER + "=?";
			cursor = db.rawQuery(sql, new String[] { message.account,
					message.owner });
			if (cursor != null && cursor.moveToNext()) {
				unread = cursor.getInt(0);
			}

			values = new ContentValues();
			values.put(DB.Conversation.COLUMN_ACCOUNT, message.account);

			int type = message.type;
			if (type == 0) {
				values.put(DB.Conversation.COLUMN_CONTENT,
						message.content);
			} else if (type == 1) {
				values.put(DB.Conversation.COLUMN_CONTENT, "图片");
			}

			values.put(DB.Conversation.COLUMN_OWNER, message.owner);
			values.put(DB.Conversation.COLUMN_UNREAD, unread);
			values.put(DB.Conversation.COLUMN_UPDATE_TIME,
					System.currentTimeMillis());

			String whereClause = DB.Conversation.COLUMN_OWNER + "=? and "
					+ DB.Conversation.COLUMN_ACCOUNT + "=?";
			String[] whereArgs = new String[] { message.owner,
					message.account };

			db.update(DB.Conversation.TABLE_NAME, values, whereClause,
					whereArgs);

		} else {
			Conversation conversation = new Conversation();
			conversation.account = (message.account);
			int type = message.type;
			if (type == 0) {
				conversation.content = (message.content);
			} else if (type == 1) {
				conversation.content = ("图片");
			}
			// conversation.setIcon(message.get);
			// conversation.setName(message.get);
			conversation.owner = (message.owner);
			conversation.unread = (message.read ? 0 : 1);
			conversation.updateTime = (System.currentTimeMillis());

			values = new ContentValues();
			values.put(DB.Conversation.COLUMN_ACCOUNT,
					conversation.account);
			values.put(DB.Conversation.COLUMN_CONTENT,
					conversation.content);
			values.put(DB.Conversation.COLUMN_ICON, conversation.icon);
			values.put(DB.Conversation.COLUMN_NAME, conversation.name);
			values.put(DB.Conversation.COLUMN_OWNER, conversation.owner);
			values.put(DB.Conversation.COLUMN_UNREAD,
					conversation.unread);
			values.put(DB.Conversation.COLUMN_UPDATE_TIME,
					conversation.updateTime);

			db.insert(DB.Conversation.TABLE_NAME, null, values);
		}
	}

	public void updateMessage(Message message) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DB.Message.COLUMN_CONTENT, message.content);
		values.put(DB.Message.COLUMN_CREATE_TIME, message.createTime);
		values.put(DB.Message.COLUMN_DIRECTION, message.direction);
		values.put(DB.Message.COLUMN_STATE, message.state);
		values.put(DB.Message.COLUMN_TYPE, message.type);
		values.put(DB.Message.COLUMN_URL, message.url);
		values.put(DB.Message.COLUMN_READ, message.read ? 1 : 0);

		String whereClause = DB.Message.COLUMN_ID + "=?";
		String[] whereArgs = new String[] { message.id + "" };
		db.update(DB.Message.TABLE_NAME, values, whereClause, whereArgs);
	}

	public Cursor queryMessage(String owner, String account) {
		String sql = "select * from " + DB.Message.TABLE_NAME + " where "
				+ DB.Message.COLUMN_OWNER + "=? and "
				+ DB.Message.COLUMN_ACCOUNT + "=? order by "
				+ DB.Message.COLUMN_CREATE_TIME + " asc";
		SQLiteDatabase db = helper.getReadableDatabase();
		return db.rawQuery(sql, new String[] { owner, account });
	}

	public Cursor queryConversation(String owner) {
		String sql = "select * from " + DB.Conversation.TABLE_NAME
				+ " where " + DB.Conversation.COLUMN_OWNER + "=? order by "
				+ DB.Conversation.COLUMN_UPDATE_TIME + " desc";
		SQLiteDatabase db = helper.getReadableDatabase();
		return db.rawQuery(sql, new String[] { owner });
	}

	public void clearUnread(String owner, String account) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DB.Message.COLUMN_READ, 1);
		String whereClause = DB.Message.COLUMN_OWNER + "=? and "
				+ DB.Message.COLUMN_ACCOUNT + "=?";
		String[] whereArgs = new String[] { owner, account };
		db.update(DB.Message.TABLE_NAME, values, whereClause, whereArgs);

		values = new ContentValues();
		values.put(DB.Conversation.COLUMN_UNREAD, 0);
		whereClause = DB.Conversation.COLUMN_OWNER + "=? and "
				+ DB.Conversation.COLUMN_ACCOUNT + "=?";
		db.update(DB.Conversation.TABLE_NAME, values, whereClause, whereArgs);
	}

	public int getAllUnread(String owner) {
		String sql = "select sum(" + DB.Conversation.COLUMN_UNREAD
				+ ") from " + DB.Conversation.TABLE_NAME + " where "
				+ DB.Conversation.COLUMN_OWNER + "=?";

		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, new String[] { owner });
		int sum = 0;
		if (cursor != null) {
			if (cursor.moveToNext()) {
				sum = cursor.getInt(0);
			}
			cursor.close();
		}
		return sum;
	}
}
