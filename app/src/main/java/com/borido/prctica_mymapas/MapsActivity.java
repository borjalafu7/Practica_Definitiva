package com.borido.prctica_mymapas;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.PreferenceManager;

import com.borido.prctica_mymapas.Intefaces.Persistencia;
import com.borido.prctica_mymapas.SQLITE.SQLManager;
import com.borido.prctica_mymapas.modelos.GestionLocalidadesAsync;
import com.borido.prctica_mymapas.modelos.GestionLocalidadesFicheros;
import com.borido.prctica_mymapas.modelos.GestionLocalidadesSQLITE;
import com.borido.prctica_mymapas.modelos.GestionLocalidadesXML;
import com.borido.prctica_mymapas.Clases.Localizacion;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import static java.net.Proxy.Type.HTTP;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private double mLatitude = 0.0, mLongitude = 0.0;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private List<Marker> mPosiciones = new ArrayList<>();

    private Drawer mDrawer;
    private Toolbar tbMiToolBar;

    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    private boolean notificaciones;
    private String tipoPersistencia;
    private List<Localizacion> mLocalizaciones;

    private Persistencia gestionLocalidades;

    private static final int PENDING_REQUEST = 1;
    private PendingIntent pendingIntent;
    private static final String CHANNEL_ID = "NOTIFICACION";
    public static final int NOTIFICACION_ID = 0;

    public MyBroadcastReceiver br;
    public IntentFilter filter;
    public static final String MY_ACTION_RECEIVER = MapsActivity.class.getCanonicalName() + ".ACTION_RECEIVER";
    public static final String MY_ACTION_RECEIVER_EXTRA = MapsActivity.class.getCanonicalName() + ".RECEIVER_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        br = new MyBroadcastReceiver();
        filter = new IntentFilter();
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(MY_ACTION_RECEIVER);

        /*--------------------------------------------------------------------------------------------------------------------------*/

        tbMiToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tbMiToolBar);

        /*--------------------------------------------------------------------------------------------------------------------------*/

        rellenarFicheros();
        rellenarSQLITE();

        recuperarPreferencias();
        recuperarInfo();

        /*--------------------------------------------------------------------------------------------------------------------------*/

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                recuperarPreferencias();
                recuperarInfo();
            }
        };
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);

        /*--------------------------------------------------------------------------------------------------------------------------*/

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(16000);
        locationRequest.setFastestInterval(8000);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        mLatitude = location.getLatitude();
                        mLongitude = location.getLongitude();

                        LatLng posicion_actualizada = new LatLng(mLatitude, mLongitude);
                        mPosiciones.add(mMap.addMarker(new MarkerOptions().position(posicion_actualizada).title("Mi localización").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_person))));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(posicion_actualizada));
                    }
                }
            }
        };

        /*--------------------------------------------------------------------------------------------------------------------------*/

        new DrawerBuilder().withActivity(this).build();

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName("Borja Rios Doñate")
                                .withEmail("borja85@gmail.com")
                                .withIcon(getResources().getDrawable(R.drawable.ic_user, getTheme()))
                )
                .build();

        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .withToolbar(tbMiToolBar)
                .withActionBarDrawerToggle(true)
                .withDrawerGravity(Gravity.START)
                .withSliderBackgroundColor(getResources().getColor(android.R.color.white))
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withIdentifier(8)
                                .withName("Ver mi perfil"),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                                .withIdentifier(1)
                                .withName("Ver mi localización"),
                        new SecondaryDrawerItem()
                                .withIdentifier(2)
                                .withName("Ocultar mi localización"),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                                .withIdentifier(3)
                                .withName("Ver restaurantes"),
                        new SecondaryDrawerItem()
                                .withIdentifier(4)
                                .withName("Ver comercios"),
                        new SecondaryDrawerItem()
                                .withIdentifier(9)
                                .withName("Ver ciudades"),
                        new SecondaryDrawerItem()
                                .withIdentifier(5)
                                .withName("Ocultar todo"),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                                .withIdentifier(6)
                                .withName("Cerrar menú"),
                        new SecondaryDrawerItem()
                                .withIdentifier(7)
                                .withName("Salir App")
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch ((int) drawerItem.getIdentifier()) {
                            case 1: {
                                requestLocations();
                                break;
                            }
                            case 2: {
                                for (Marker posicion: mPosiciones){
                                    posicion.remove();
                                }
                                removeLocations();
                                break;
                            }
                            case 3: {
                                ver_makers("restaurantes");
                                break;
                            }
                            case 4: {
                                ver_makers("comercios");
                                break;
                            }
                            case 5: {
                                mMap.clear();
                                break;
                            }
                            case 6: {
                                break;
                            }
                            case 7: {
                                finish();
                                break;
                            }
                            case 8: {
                                sendBroadcastCustom();
                                break;
                            }
                            case 9: {
                                ver_makers("ciudades");
                                break;
                            }
                        }
                        return false;
                    }
                }).build();
    }

    /*---------------------------------------------------BROADCAST--------------------------------------------------------------*/

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(br, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }

    private void sendBroadcastCustom() {
        Intent intent = new Intent(MY_ACTION_RECEIVER);
        Bundle parametros = new Bundle();
        parametros.putString(MY_ACTION_RECEIVER_EXTRA, "Borja Rios Doñate");
        intent.putExtras(parametros);
        sendBroadcast(intent);
    }

    /*---------------------------------------------------PREFERENCIAS E INFO--------------------------------------------------------------*/

    private void recuperarPreferencias() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        notificaciones = sharedPreferences.getBoolean("switch_preference_notificaciones", true);
        Log.i("info", "Tipo de notificaciones: "+notificaciones);

        tipoPersistencia = sharedPreferences.getString("list_preference_persistencia", "XML");
        Log.i("info", "Tipo de persistencia: "+tipoPersistencia);
    }

    private void recuperarInfo() {
        if(tipoPersistencia.equals("Base de datos (SQLite)")){
            gestionLocalidades = new GestionLocalidadesSQLITE(this);
        }else {
            if (tipoPersistencia.equals("Ficheros")) {
                gestionLocalidades = new GestionLocalidadesFicheros(this);
            } else {
                if (tipoPersistencia.equals("XML")) {
                    getResources().openRawResource(R.raw.localizaciones);
                    gestionLocalidades = new GestionLocalidadesXML(this);
                }else{
                    if (tipoPersistencia.equals("Async (HTTPS)")) {
                        gestionLocalidades = new GestionLocalidadesAsync();
                    }
                }
            }
        }
        mLocalizaciones = gestionLocalidades.leerLocalizaciones();
    }

    /*-----------------------------------------------MAPA------------------------------------------------------------------*/

    public void ver_makers(String tipo){
        for (Localizacion localizacion : mLocalizaciones) {
            if(tipo.equals("restaurantes") && localizacion.getEtiqueta().equals("restaurante")) {
                LatLng restaurante = new LatLng(localizacion.getLatitud(), localizacion.getLongitud());
                Marker myRestaurante = mMap.addMarker(new MarkerOptions().position(restaurante).title(localizacion.getTitulo()).snippet(localizacion.getFragmento()).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_restaurant)));
                myRestaurante.setTag(localizacion.getEtiqueta());
            }else{
                if(tipo.equals("comercios") && localizacion.getEtiqueta().equals("comercio")) {
                    LatLng comercio = new LatLng(localizacion.getLatitud(), localizacion.getLongitud());
                    Marker myComercio = mMap.addMarker(new MarkerOptions().position(comercio).title(localizacion.getTitulo()).snippet(localizacion.getFragmento()).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_comercio)));
                    myComercio.setTag(localizacion.getEtiqueta());
                }else{
                    if(tipo.equals("ciudades") && localizacion.getEtiqueta().equals("ciudad")){
                        LatLng comercio = new LatLng(localizacion.getLatitud(), localizacion.getLongitud());
                        Marker myComercio = mMap.addMarker(new MarkerOptions().position(comercio).title(localizacion.getTitulo()).snippet(localizacion.getFragmento()).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_city)));
                        myComercio.setTag(localizacion.getEtiqueta());
                    }
                }
            }
        }
    }

    private void requestLocations() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
        } else {
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    private void removeLocations() {
        mFusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng ayuntamientoTorrent = new LatLng(39.43675923414676, -0.466030288181505);

        mMap.addMarker(new MarkerOptions().position(ayuntamientoTorrent));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ayuntamientoTorrent,17f));

        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        mMap.setOnInfoWindowClickListener(this);

        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        switch (marker.getTag().toString()){
            case "miposicion": {
                if(notificaciones == true){
                    setPendingIntent(marker);
                    createNotificacionChannel();
                    crearNotificacion(marker);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("myMapas")
                            .setMessage("Su ubicación actual es: \n Lat: "+marker.getPosition().latitude+" Lon: "+marker.getPosition().longitude)
                            .setIcon(R.mipmap.ic_map)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Log.i("none", "NONE");
                                }
                            });
                    builder.create();
                    builder.show();
                }
                break;
            }
            case "comercio": {
                if(notificaciones == true){
                    setPendingIntent(marker);
                    createNotificacionChannel();
                    crearNotificacion(marker);
                }else{
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(marker.getSnippet()));
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
                break;
            }
            case "restaurante": {
                if(notificaciones == true){
                    setPendingIntent(marker);
                    createNotificacionChannel();
                    crearNotificacion(marker);
                }else{
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: "+marker.getSnippet()));
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
                break;
            }
            case "ciudad": {
                if(notificaciones == true){
                    setPendingIntent(marker);
                    createNotificacionChannel();
                    crearNotificacion(marker);
                }else{
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Petición");
                    intent.putExtra(Intent.EXTRA_TEXT, "Solicitando petición");
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(marker.getSnippet()));
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
                break;
            }
        }
    }

    /*--------------------------------------------------MENU-------------------------------------------------------------*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflaterMenu = getMenuInflater();
        inflaterMenu.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.navigation_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*----------------------------------------------------NOTIFICACIONES----------------------------------------------------------*/

    //Versiones posteriores a Oreo
    private void createNotificacionChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Notificacion Normal";
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                notificationChannel.setAllowBubbles(true);
            }
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    //Inferiores a Oreo API 26 Android 8.0
    private void crearNotificacion(Marker marker) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_baseline_map_24);
        builder.setContentTitle(marker.getTitle());
        builder.setContentText((CharSequence) marker.getTag());
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText("Más información acerca de tu ubicación seleccionada"));
        builder.setColor(Color.BLUE);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setLights(Color.RED,10000,10000);
        builder.setVibrate(new long[]{1000, 500, 1000, 1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setContentIntent(pendingIntent);
        builder.setNumber(7);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());
    }

    private void setPendingIntent(Marker marker) {
        Intent intent = new Intent(this, NotificationActivity.class);
        Bundle parametros = new Bundle();
        parametros.putString("title", marker.getTitle());
        parametros.putString("snippet", marker.getSnippet());
        parametros.putString("tag", (String) marker.getTag());
        parametros.putString("latitude", String.valueOf(marker.getPosition().latitude));
        parametros.putString("longitude", String.valueOf(marker.getPosition().longitude));
        intent.putExtras(parametros);

        //Para que al dar hacia atrás vaya a la main y no salga (opcional)
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MapsActivity.class);
        stackBuilder.addNextIntent(intent);

        pendingIntent = stackBuilder.getPendingIntent(PENDING_REQUEST, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /*----------------------------------------------------RELLENAR DATOS----------------------------------------------------------*/

    private void rellenarFicheros() {
        List<Localizacion> localizaciones = new ArrayList<Localizacion>();
        Localizacion l1 = new Localizacion((int) Math.floor(Math.random()*(0-100000)+100000), "Restaurante Asador Azorín", "+34961574702", "restaurante", 39.43162415426558, -0.4764209532649998);
        Localizacion l2 = new Localizacion((int) Math.floor(Math.random()*(0-100000)+100000), "Folder Papelerías", "http://www.folder.es/", "comercio", 39.43314605170049, -0.47139780673038345);
        localizaciones.add(l1);
        localizaciones.add(l2);

        File appFilesDirectory = this.getFilesDir();
        try {
            FileOutputStream fos = new FileOutputStream(new File(appFilesDirectory, "misLocalizaciones.dat"));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(localizaciones);
            oos.close();
        } catch (IOException ex) {
            Log.i("info","Fallo al guardar el fichero");
            Log.i("info", String.valueOf(ex));
        }
    }

    private void rellenarSQLITE() {
        List<Localizacion> localizaciones = new ArrayList<Localizacion>();
        Localizacion l1 = new Localizacion((int) Math.floor(Math.random()*(0-100000)+100000), "Little Thai", "+34960261040", "restaurante", 39.43254841415737, -0.4708583178118206);
        Localizacion l2 = new Localizacion((int) Math.floor(Math.random()*(0-100000)+100000), "Foto Ya", "https://www.google.com/url?sa=t&source=web&rct=j&url=https://m.facebook.com/fotoyatorrent/&ved=2ahUKEwiV8ajujPbtAhUSuRoKHQxLBgoQFjANegQIIhAC&usg=AOvVaw1fF4l8eg8hEAPMz8Udv9u6", "comercio", 39.433283496754726, -0.4705630989702177);
        localizaciones.add(l1);
        localizaciones.add(l2);

        SQLManager sQLManager = new SQLManager(this);
        for(Localizacion localizacion: localizaciones){
            sQLManager.insert(localizacion);
        }
    }
}