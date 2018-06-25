package com.onemi6.app.model;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.onemi6.app.db.Onemi6OpenHelper;

public class Onemi6DB {
	/** * 数据库名 */
	public static final String DB_NAME = "onemi6";
	/** * 数据库版本 */
	public static final int VERSION = 2;
	private static Onemi6DB onemi6DB;
	private SQLiteDatabase db;

	/** * 将构造方法私有化 */
	private Onemi6DB(Context context) {
		Onemi6OpenHelper dbHelper = new Onemi6OpenHelper(context, DB_NAME,
				null, VERSION);
		db = dbHelper.getWritableDatabase();
	}

	public void closeDB() {
		db.close();
	}

	/** * 获取Onemi6DB的实例。 */
	public synchronized static Onemi6DB getInstance(Context context) {
		if (onemi6DB == null) {
			onemi6DB = new Onemi6DB(context);
		}
		return onemi6DB;
	}

	/** * 将Province实例存储到数据库。 */
	public void saveProvince(Province province) {
		if (province != null) {
			db.execSQL(
					"insert into Province(province_name,province_code) values(?,?)",
					new String[] { province.getProvinceName(),
							province.getProvinceCode() });
		}
	}

	/** * 从数据库读取全国所有的省份信息。 */
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

	/** * 将City实例存储到数据库。 */
	public void saveCity(City city) {
		if (city != null) {
			db.execSQL(
					"insert into City(city_name,city_code,province_id) values(?,?,?)",
					new Object[] { city.getCityName(), city.getCityCode(),
							city.getProvinceId() });
		}
	}

	/** * 从数据库读取某省下所有的城市信息。 */
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

	/** * 将County实例存储到数据库。 */
	public void saveCounty(County county) {
		if (county != null) {
			db.execSQL(
					"insert into County(county_name,county_code,city_id) values(?,?,?)",
					new Object[] { county.getCountyName(),
							county.getCountyCode(), county.getCityId() });
		}
	}

	/** * 从数据库读取某城市下所有的县信息。 */
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

	/** * 将选择的County存储到County_List。 */
	public void saveCountyList(County county) {
		if (county != null) {
			db.execSQL(
					"insert into County_List(county_name,county_code) values(?,?)",
					new Object[] { county.getCountyName(),
							county.getCountyCode() });
		}
	}

	// 删除County_List的County
	public void deteleCountyList(String county_code) {
		db.execSQL("delete from County_List where county_code = ?",
				new String[] { county_code });
	}

	/** * 从数据库读取已添加的城市列表。 */
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

	/** * 查询城市列表中是否存在。 */
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
