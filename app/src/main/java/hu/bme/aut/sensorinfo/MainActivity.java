package hu.bme.aut.sensorinfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity
{
	private SensorManager sensorManager;
	private LayoutInflater inflater;
	private LinearLayout listOfRows;
	private DrawerLayout drawerLayout;
	private MenuItem home;

	private Map<Integer, String> sensorNames = new HashMap<Integer, String>();
	private Map<String, MenuItem> activeSensors = new HashMap<String, MenuItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_activity);

		sensorNames.put(Sensor.TYPE_ACCELEROMETER, "Accelerometer");
		sensorNames.put(Sensor.TYPE_MAGNETIC_FIELD, "Magnetometer");
		sensorNames.put(Sensor.TYPE_ORIENTATION, "Orientation");
		sensorNames.put(Sensor.TYPE_GYROSCOPE, "Gyroscope");
		sensorNames.put(Sensor.TYPE_LIGHT, "Light");
		sensorNames.put(Sensor.TYPE_PROXIMITY, "Proximity");


		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

		drawerLayout = findViewById(R.id.start_drawer);
		Toolbar toolbar = findViewById(R.id.start_toolbar);
		setSupportActionBar(toolbar);

		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_button_open, R.string.nav_button_close);
		drawerLayout.addDrawerListener(toggle);
		toggle.syncState();


		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final NavigationView navView = findViewById(R.id.start_navigation);

		final Menu navMenu = navView.getMenu();

		home = navMenu.add("Home");
		home.setIcon(R.drawable.ic_home_black_24dp);

		getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
		for(Sensor item : sensorList)
		{
			Log.println(Log.INFO, "SensorNames", item.getName() + "\t"+ item.getType());
			for(int k : sensorNames.keySet())
			{
				if(item.getType() == k)
				{
					activeSensors.put(sensorNames.get(item.getType()),navMenu.add(R.id.nav_menu,k,1,sensorNames.get(item.getType())));
				}

			}

		}

		navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
		{
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
			{
				Log.println(Log.INFO,"appInfo", "Tapped menuItem: "+ menuItem.getTitle().toString());
				Log.println(Log.INFO,"appInfo", "Selected menuItem: "+ navView.getCheckedItem());
				NavigationView navigationView = findViewById(R.id.start_navigation);


				switch(menuItem.getTitle().toString())
				{
					case "Home":
						getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
						break;

					case "Accelerometer":
						getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AccFragment()).commit();
						break;

					case "Gyroscope":
						getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new GyroFragment()).commit();
						break;

					case "Proximity":
						getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProxFragment()).commit();
						break;

					case "Magnetometer":
						getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MagFragment()).commit();
						break;

					case "Light":
						getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LigFragment()).commit();
						break;

					case "Orientation":
						getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OriFragment()).commit();
						break;

					default:
						Log.println(Log.INFO,"appInfo", "New menuItem");
						break;

				}
				for(MenuItem i: activeSensors.values())
				{
					i.setChecked(false);
				}
				home.setChecked(false);

				menuItem.setChecked(true);
				drawerLayout.closeDrawer(GravityCompat.START);
				return true;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.opt_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.Settings:
				Intent settings_intent = new Intent(this, SettingsActivity.class);
				startActivity(settings_intent);
			break;

			case R.id.Help:
				Intent help_intent = new Intent(this, HelpActivity.class);
				startActivity(help_intent);
			break;

			default:
				Log.println(Log.INFO, "OptionMenu", "New Item: "+ item.getTitle());
			break;
		}
		Log.println(Log.INFO, "OptionMenu", "Item: "+ item.getTitle());
		return true;
	}

	@Override
	public void onBackPressed()
	{
		if(drawerLayout.isDrawerOpen(GravityCompat.START))
		{
			drawerLayout.closeDrawer(GravityCompat.START);
		}
		else
		{
			super.onBackPressed();
		}
	}
}
