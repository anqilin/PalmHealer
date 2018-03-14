package com.moxi.palmhealer.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.moxi.palmhealer.R;
import com.moxi.palmhealer.beans.Device;
import com.moxi.palmhealer.db.DBHelper;
import com.moxi.palmhealer.fragment.ControlorFragment;
import com.moxi.palmhealer.utils.AppConfig;
import com.moxi.palmhealer.utils.LogUtils;
import com.zhy.autolayout.AutoLayoutActivity;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

public class RemoteActivity extends AutoLayoutActivity implements View.OnClickListener {
    private final static String TAG = "---RemoteActivity---";
    ControlorFragment controlorFragment;
    DrawerLayout drawer;
    ActionBarDrawerToggle mDrawerToggle;
    MydeviceListAdapter mydeviceListAdapter;
    ArrayList<Device> deviceArrayList = new ArrayList<>();
    ListView controler_mydevicelist;
    Context context;
    boolean is_control_delete_imv;
    ImageView controler_add_imv;
    ImageView control_delete_imv;
    private DBHelper dbHelper = null;// 数据库对象

    private boolean Isconnect=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);
        context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
//        toolbar.setNavigationIcon(getResources().getIdentifier("topbar_right_more", "mipmap", getBaseContext().getPackageName()));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to
                /** Called when a drawer has settled in a completely open state. */
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to
            }
        };
        drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();


        Fragment controltarget = getSupportFragmentManager().findFragmentByTag("ControlorFragment");
        if (controltarget == null) {
            controlorFragment = new ControlorFragment();
        } else {
            controlorFragment = (ControlorFragment) controltarget;
        }
        FragmentTransaction controlorFragmenttransaction = getSupportFragmentManager().beginTransaction().setCustomAnimations(
                R.anim.right_in, R.anim.left_out);
        controlorFragmenttransaction.replace(R.id.content_remote_main, controlorFragment, "ControlorFragment");
        controlorFragmenttransaction.commit();
        //mydeviceListAdapter = new MydeviceListAdapter();
        initView();
        initData();

    }

    private void initView() {
        controler_add_imv = (ImageView) findViewById(R.id.controler_add_imv);
        control_delete_imv = (ImageView) findViewById(R.id.control_delete_imv);
        controler_add_imv.setOnClickListener(this);
        control_delete_imv.setOnClickListener(this);

    }

    private void initData() {
        dbHelper = new DBHelper(getApplicationContext());
        Cursor cur = dbHelper.queryRecently();
        while (cur.moveToNext()) {
            LogUtils.debug(TAG, "一共有多少行数据=" + cur.getCount());
            for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                int device_mac_int = cur.getColumnIndex("device_mac");
                int device_name_int = cur.getColumnIndex("device_name");
                int device_name_EN_int = cur.getColumnIndex("device_name_EN");
                int deviceName_CN_int = cur.getColumnIndex("deviceName_CN");
                int device_Pic_int = cur.getColumnIndex("device_Pic");
                String device_Mac = cur.getString(device_mac_int);
                String device_Name = cur.getString(device_name_int);
                String device_Name_EN = cur.getString(device_name_EN_int);
                String deviceName_CN = cur.getString(deviceName_CN_int);
                String device_Pic = cur.getString(device_Pic_int);


                Device device = new Device();
                device.device_mac = device_Mac;
                device.device_Name = device_Name;
                device.deviceName_EN = device_Name_EN;
                device.deviceName_CN = deviceName_CN;
                device.device_Pic = device_Pic;

                deviceArrayList.add(device);
                LogUtils.debug(TAG, "查询出来的device为=" + device_Name + device_Mac + device_Pic);
                controler_mydevicelist = (ListView) findViewById(R.id.controler_mydevicelist);
                mydeviceListAdapter = new MydeviceListAdapter();
                controler_mydevicelist.setAdapter(mydeviceListAdapter);
                mydeviceListAdapter.notifyDataSetChanged();

            }
        }
        if (cur != null) {
            cur.close();
            cur = null;
        }
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.remote, menu);
        MenuItem item = menu.add(Menu.NONE, 0, 1, "返回");
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == 0) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.controler_add_imv:
                if (deviceArrayList.size() == 0) {
                    LogUtils.debug(TAG, "点击了添加按钮");
                    Intent controlintent1 = new Intent(context, ControlActivity.class);
                    controlintent1.putExtra("key", "from_remote");
                    startActivity(controlintent1);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "一次只能连接一个设备，请先删除设备，然后连接新的设备",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.control_delete_imv:
                is_control_delete_imv = true;
                mydeviceListAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (dbHelper != null) {
            dbHelper.close();
            dbHelper = null;
        }
    }

    public void IsConnect(){
        Isconnect=true;
        mydeviceListAdapter.notifyDataSetChanged();
    }
    class MydeviceListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return deviceArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return deviceArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            final ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = View.inflate(context, R.layout.controler_edit_item, null);
                viewHolder = new ViewHolder();
                viewHolder.device_Pic = (ImageView) view.findViewById(R.id.controler_listitem_pic);
                viewHolder.device_Name = (TextView) view.findViewById(R.id.controler_item_devicename);
                viewHolder.deviceName_CN = (TextView) view.findViewById(R.id.controler_item_devicename_CN);
                viewHolder.controler_deviceitem_offline = (TextView) view.findViewById(R.id.controler_deviceitem_offline);
                viewHolder.controler_edit_delete_btn = (ImageView) view.findViewById(R.id.controler_edit_delete_btn);
                viewHolder.controler_edit_delete_bg = (AutoRelativeLayout) view.findViewById(R.id.controler_edit_delete_bg);
                view.setTag(viewHolder);
                AutoUtils.autoSize(view);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            Device device = deviceArrayList.get(position);
            String imageName = "portable_moxibustion";
            final String mac = device.getDevice_mac();
            Context ctx = getBaseContext();
            int resId = getResources().getIdentifier(imageName, "mipmap", ctx.getPackageName());
            if (device != null) {
                viewHolder.device_Name.setText(device.getDevice_Name());
                viewHolder.deviceName_CN.setText(device.getDeviceName_CN());
                viewHolder.device_Pic.setImageResource(resId);
            }
            if (Isconnect==true){
                viewHolder.controler_deviceitem_offline.setText("已连接");
                viewHolder.controler_deviceitem_offline.setTextSize(10);
            }
            viewHolder.controler_deviceitem_offline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   /* if (viewHolder.controler_deviceitem_offline.getText().equals("断开")) {
                        viewHolder.controler_deviceitem_offline.setText("已断开");
                        viewHolder.controler_deviceitem_offline.setTextSize(10);
                        Intent intent = new Intent();
                        intent.setAction(AppConfig.CONTROLER_DISCONNECTED);
                        sendBroadcast(intent);

                    }*/
                }
            });

            viewHolder.controler_edit_delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog(position, mac);
                }
            });

            if (is_control_delete_imv) {
                viewHolder.controler_edit_delete_btn.setVisibility(View.VISIBLE);
            }

            return view;
        }

        private void dialog(final int position, final String mac) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("确认删除吗？");
            builder.setTitle("删除设备");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent = new Intent();
                    intent.setAction(AppConfig.CONTROLER_DISCONNECTED);
                    sendBroadcast(intent);

                    dbHelper = new DBHelper(getApplicationContext());
                    dbHelper.delete(mac);
                    deviceArrayList.remove(position);
                    mydeviceListAdapter.notifyDataSetChanged();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }

    }

    static class ViewHolder {
        TextView device_Name;
        TextView deviceName_CN;
        ImageView device_Pic;
        TextView controler_deviceitem_offline;
        AutoRelativeLayout controler_edit_delete_bg;
        ImageView controler_edit_delete_btn;
    }
}
