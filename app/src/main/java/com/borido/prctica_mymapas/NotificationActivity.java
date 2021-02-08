package com.borido.prctica_mymapas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class NotificationActivity extends AppCompatActivity {

    private TextView TextViewTitulo, TextViewTag, TextViewLogitud, TextViewLatitud, TextViewSnippet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.cancel(MapsActivity.NOTIFICACION_ID);

        TextViewTitulo = (TextView) findViewById(R.id.textViewTitle);
        TextViewTag = (TextView) findViewById(R.id.textViewTag);
        TextViewLogitud = (TextView) findViewById(R.id.textViewLogitud);
        TextViewLatitud = (TextView) findViewById(R.id.textViewLatitud);
        TextViewSnippet = (TextView) findViewById(R.id.textViewSnippet);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            TextViewTitulo.setText("Titulo: "+extras.getString("title"));
            TextViewTag.setText("Tag: "+extras.getString("tag"));
            TextViewLogitud.setText("Longitud: "+extras.getString("longitude"));
            TextViewLatitud.setText("Latitud: "+extras.getString("latitude"));
            TextViewSnippet.setText("Snippet: "+extras.getString("snippet"));
        }
    }
}