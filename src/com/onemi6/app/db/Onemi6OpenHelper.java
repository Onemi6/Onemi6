package com.onemi6.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class Onemi6OpenHelper extends SQLiteOpenHelper {
	private Context mContext; 

	/** * Province表建表语句 */
	public static final String CREATE_PROVINCE = "create table Province ("
			+ "id integer primary key autoincrement, " + "province_name text, "
			+ "province_code text)";

	/** * City表建表语句 */
	public static final String CREATE_CITY = "create table City ("
			+ "id integer primary key autoincrement, " + "city_name text, "
			+ "city_code text, " + "province_id integer)";

	/** * County表建表语句 */
	public static final String CREATE_COUNTY = "create table County ("
			+ "id integer primary key autoincrement, " + "county_name text, "
			+ "county_code text, " + "city_id integer)";
	
	/** * CountyList表建表语句 */
	public static final String CREATE_COUNTY_LIST = "create table County_List ("
			+ "id integer primary key autoincrement, " + "county_name text, "
			+ "county_code text)";

	public Onemi6OpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_PROVINCE); // 创建Province表
		db.execSQL(CREATE_CITY); // 创建City表
		db.execSQL(CREATE_COUNTY); // 创建County表 
		db.execSQL(CREATE_COUNTY_LIST); // 创建County表 
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch (oldVersion) {
		case 1:
			db.execSQL(CREATE_COUNTY_LIST);
		default:
		}
	}

}
