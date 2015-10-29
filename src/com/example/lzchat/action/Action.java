package com.example.lzchat.action;

import java.util.Map;

import android.content.Context;

public abstract class Action {

	public abstract String getAction();

	public abstract void doAction(Context context, Map<String, Object> data);
}