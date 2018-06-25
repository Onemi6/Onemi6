package com.onemi6.app.model;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.onemi6.app.db.Onemi6OpenHelper;

public class Onemi6DB {
	/** * ���ݿ��� */
	public static final String DB_NAME = "onemi6";
	/** * ���ݿ�汾 */
	public static final int VERSION = 2;
	private static Onemi6DB onemi6DB;
	private SQLiteDatabase db;

	/** * �����췽��˽�л� */
	private Onemi6DB(Context context) {
		Onemi6OpenHelper dbHelper = new Onemi6OpenHelper(context, DB_NAME,
				null, VERSION);
		db = dbHelper.getWritableDatabase();
	}

	public void closeDB() {
		db.close();
	}

	/** * ��ȡOnemi6DB��ʵ���� */
	public synchronized static Onemi6DB getInstance(Context context) {
		if (onemi6DB == null) {
			onemi6DB = new Onemi6DB(context);
		}
		return onemi6DB;
	}

	/** * ��Provinceʵ���洢�����ݿ⡣ */
	public void saveProvince(Province province) {
		if (province != null) {
			db.execSQL(
					"insert into Province(province_name,province_code) values(?,?)",
					new String[] { province.getProvinceName(),
							province.getProvinceCode() });
		}
	}

	/** * �����ݿ��ȡȫ�����е�ʡ����Ϣ�� */
	public List<Province> loadProvinces() {
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db.rawQuery("select * from Province", null);
		if (cursor.moveToFirst()) {
			do {
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor
						.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor
						.getColumnIndex("province_code")));
				list.add(province);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return list;
	}

	/** * ��Cityʵ���洢�����ݿ⡣ */
	public void saveCity(City city) {
		if (city != null) {
			db.execSQL(
					"insert into City(city_name,city_code,province_id) values(?,?,?)",
					new Object[] { city.getCityName(), city.getCityCode(),
							city.getProvinceId() });
		}
	}

	/** * �����ݿ��ȡĳʡ�����еĳ�����Ϣ�� */
	public List<City> loadCities(int provinceId) {
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.rawQuery("select * from City where province_id = ?",
				new String[] { String.valueOf(provinceId) });
		if (cursor.moveToFirst()) {
			Log.d("cursor",
					cursor.getString(cursor.getColumnIndex("city_name")));
			do {
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor
						.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor
						.getColumnIndex("city_code")));
				city.setProvinceId(provinceId);
				list.add(city);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return list;
	}

	/** * ��Countyʵ���洢�����ݿ⡣ */
	public void saveCounty(County county) {
		if (county != null) {
			db.execSQL(
					"insert into County(county_name,county_code,city_id) values(?,?,?)",
					new Object[] { county.getCountyName(),
							county.getCountyCode(), county.getCityId() });
		}
	}

	/** * �����ݿ��ȡĳ���������е�����Ϣ�� */
	public List<County> loadCounties(int cityId) {
		List<County> list = new ArrayList<County>();
		Cursor cursor = db.rawQuery("select * from County where city_id = ?",
				new String[] { String.valueOf(cityId) });
		if (cursor.moveToFirst()) {
			do {
				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyName(cursor.getString(cursor
						.getColumnIndex("county_name")));
				county.setCountyCode(cursor.getString(cursor
						.getColumnIndex("county_code")));
				county.setCityId(cityId);
				list.add(county);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return list;
	}

	/** * ��ѡ���County�洢��County_List�� */
	public void saveCountyList(County county) {
		if (county != null) {
			db.execSQL(
					"insert into County_List(county_name,county_code) values(?,?)",
					new Object[] { county.getCountyName(),
							county.getCountyCode() });
		}
	}

	// ɾ��County_List��County
	public void deteleCountyList(String county_code) {
		db.execSQL("delete from County_List where county_code = ?",
				new String[] { county_code });
	}

	/** * �����ݿ��ȡ����ӵĳ����б� */
	public List<County> loadCountyList() {
		List<County> list = new ArrayList<County>();
		Cursor cursor = db.rawQuery("select * from County_List", null);
		if (cursor.moveToFirst()) {
			do {
				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyName(cursor.getString(cursor
						.getColumnIndex("county_name")));
				county.setCountyCode(cursor.getString(cursor
						.getColumnIndex("county_code")));
				list.add(county);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return list;
	}

	/** * ��ѯ�����б����Ƿ���ڡ� */
	public boolean selectCounty(String countycode) {
		Cursor cursor = db.rawQuery(
				"select * from County_List where county_code=?",
				new String[] { countycode });
		if (cursor.getCount()== 0) {
			cursor.close();
			return false;
		}else {
			cursor.close();
			return true;
		}
	}
}
