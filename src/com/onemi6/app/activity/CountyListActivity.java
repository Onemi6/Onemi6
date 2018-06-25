package com.onemi6.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.onemi6.app.R;
import com.onemi6.app.model.County;
import com.onemi6.app.model.Onemi6DB;

public class CountyListActivity extends Activity implements OnClickListener{
	private ListView listcounty;
	private Button bt_addcounty;
	private Button bt_refreshlist;
	private TextView empty_countylist;
	private ArrayAdapter<String> adapter;
	private Onemi6DB onemi6DB;
	private List<String> dataList = new ArrayList<String>();
	private List<County> countyList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.county_list);
		listcounty = (ListView) findViewById(R.id.list_county);
		bt_addcounty = (Button) findViewById(R.id.add_city);
		bt_refreshlist = (Button) findViewById(R.id.refresh_county);
		empty_countylist = (TextView) findViewById(R.id.text_empty_countylist);
		listcounty.setEmptyView(empty_countylist);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, dataList);
		listcounty.setAdapter(adapter);
		onemi6DB = Onemi6DB.getInstance(this);
		
		bt_addcounty.setOnClickListener(this);
		bt_refreshlist.setOnClickListener(this);
		
		listcounty.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO 自动生成的方法存根
				String countyCode = countyList.get(arg2).getCountyCode();
				Intent intent = new Intent(CountyListActivity.this,
						WeatherActivity.class);
				//intent.putExtra("from_countylist_activity", true);
				intent.putExtra("county_code", countyCode);
				startActivity(intent);
				finish();
			}

		});
		listcounty.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO 自动生成的方法存根

				// 通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
				AlertDialog.Builder builder = new AlertDialog.Builder(
						CountyListActivity.this);
				// 设置Title的图标
				builder.setIcon(R.drawable.icon_logo);
				// 设置Title的内容
				// builder.setTitle("弹出警告框");
				// 设置Content来显示一个信息
				builder.setMessage("确定删除？");
				// 设置一个PositiveButton
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								try {
									String countyCode = countyList.get(position).getCountyCode();
									onemi6DB.deteleCountyList(countyCode);
									dataList.remove(position);
									countyList.remove(position);
									adapter.notifyDataSetChanged();
									Toast.makeText(CountyListActivity.this, "删除成功",
											Toast.LENGTH_LONG).show();
								} catch (Exception e) {
									// TODO 自动生成的 catch 块
									e.printStackTrace();
									Toast.makeText(CountyListActivity.this, "删除失败",
											Toast.LENGTH_LONG).show();
								}
							}
						});
				// 设置一个NegativeButton
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						});
				// 显示出该对话框
				builder.show();
				return true;
			}
		});
		queryCountyList();
	}

	/** * 查询城市列表。 */
	private void queryCountyList() {
		countyList = onemi6DB.loadCountyList();
		if (countyList.size() > 0) {
			dataList.clear();
			for (County county : countyList) {
				dataList.add(county.getCountyName());
			}
			adapter.notifyDataSetChanged();
			listcounty.setSelection(0);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.county_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View view) {
		// TODO 自动生成的方法存根
		switch (view.getId()) {
		case R.id.add_city:
			Intent intent = new Intent(this, ChooseAreaActivity.class);		
			startActivity(intent);
			//finish();
			break;
		case R.id.refresh_county:
			queryCountyList();
			Toast.makeText(CountyListActivity.this, "刷新成功",
					Toast.LENGTH_LONG).show();
			break;
		default:
			break;
		}
	}
}
