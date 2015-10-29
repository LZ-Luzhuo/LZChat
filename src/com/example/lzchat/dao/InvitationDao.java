package com.example.lzchat.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.lzchat.bean.Invitation;

public class InvitationDao {
	private HMDBOpenHelper helper;

	public InvitationDao(Context context) {
		helper = HMDBOpenHelper.getInstance(context);
	}

	public Cursor queryCursor(String owner) {
		SQLiteDatabase db = helper.getReadableDatabase();

		String sql = "select * from " + DB.Invitation.TABLE_NAME + " where "
				+ DB.Invitation.COLUMN_OWNER + "=?";
		return db.rawQuery(sql, new String[] { owner });
	}

	public void addInvitation(Invitation invitation) {
		SQLiteDatabase db = helper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(DB.Invitation.COLUMN_OWNER, invitation.owner);
		values.put(DB.Invitation.COLUMN_INVITATOR_ACCOUNT,
				invitation.account);
		values.put(DB.Invitation.COLUMN_INVITATOR_NAME, invitation.name);
		values.put(DB.Invitation.COLUMN_INVITATOR_ICON, invitation.icon);
		values.put(DB.Invitation.COLUMN_CONTENT, invitation.content);
		values.put(DB.Invitation.COLUMN_AGREE, invitation.agree ? 1 : 0);
		db.insert(DB.Invitation.TABLE_NAME, null, values);
	}

	public void updateInvitation(Invitation invitation) {
		SQLiteDatabase db = helper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(DB.Invitation.COLUMN_INVITATOR_NAME, invitation.name);
		values.put(DB.Invitation.COLUMN_INVITATOR_ICON, invitation.icon);
		values.put(DB.Invitation.COLUMN_CONTENT, invitation.content);
		values.put(DB.Invitation.COLUMN_AGREE, invitation.agree ? 1 : 0);

		String whereClause = DB.Invitation.COLUMN_OWNER + "=? and "
				+ DB.Invitation.COLUMN_INVITATOR_ACCOUNT + "=?";
		String[] whereArgs = new String[] { invitation.owner,
				invitation.account };

		db.update(DB.Invitation.TABLE_NAME, values, whereClause, whereArgs);
	}

	public Invitation queryInvitation(String owner, String account) {
		SQLiteDatabase db = helper.getWritableDatabase();
		String sql = "select * from " + DB.Invitation.TABLE_NAME + " where "
				+ DB.Invitation.COLUMN_OWNER + "=? and "
				+ DB.Invitation.COLUMN_INVITATOR_ACCOUNT + "=?";

		Cursor cursor = db.rawQuery(sql, new String[] { owner, account });
		Invitation invitation = null;
		if (cursor != null) {
			if (cursor.moveToNext()) {
				String name = cursor.getString(cursor
						.getColumnIndex(DB.Invitation.COLUMN_INVITATOR_NAME));
				String icon = cursor.getString(cursor
						.getColumnIndex(DB.Invitation.COLUMN_INVITATOR_ICON));
				boolean agree = cursor.getInt(cursor
						.getColumnIndex(DB.Invitation.COLUMN_AGREE)) == 1;
				String content = cursor.getString(cursor
						.getColumnIndex(DB.Invitation.COLUMN_CONTENT));
				long id = cursor.getLong(cursor
						.getColumnIndex(DB.Invitation.COLUMN_ID));

				invitation = new Invitation();
				invitation.account = (account);
				invitation.agree = (agree);
				invitation.content = (content);
				invitation.icon = (icon);
				invitation.name = (name);
				invitation.owner = (owner);
				invitation.id = (id);
			}
			cursor.close();
		}
		return invitation;
	}

	public boolean hasUnagree(String owner) {
		SQLiteDatabase db = helper.getWritableDatabase();
		String sql = "select count(" + DB.Invitation.COLUMN_ID + ") from "
				+ DB.Invitation.TABLE_NAME + " where "
				+ DB.Invitation.COLUMN_AGREE + "=0";
		Cursor cursor = db.rawQuery(sql, null);

		int count = 0;
		if (cursor != null) {
			if (cursor.moveToNext()) {
				count = cursor.getInt(0);
			}
			cursor.close();
		}
		return count != 0;
	}
}
