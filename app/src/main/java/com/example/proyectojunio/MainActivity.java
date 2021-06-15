package com.example.proyectojunio;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.proyectojunio.model.Cancion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    ImageView iv;
    private AppBarConfiguration mAppBarConfiguration;
    int posicion = 0;

    private ArrayList<Cancion> listaCancion;
    int REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Se comprueba que se tengan los permisos necesarios para continuar
        comprobarPermisos();

        listaCancion = new ArrayList<Cancion>();
        getListaCancion();

        //Ordena por título la lista de canciones recuperada en el método anterior
        Collections.sort(listaCancion, new Comparator<Cancion>() {
            @Override
            public int compare(Cancion o1, Cancion o2) {
                return o1.getTitulo().compareTo(o2.getTitulo());
            }
        });
    }

    //En este metodo se comprueban si se tienen los permisos necesarios para funcionar
    private void comprobarPermisos() {
       int permisosLeerExterna = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

       //En caso de no tener permisos de lectura del almacenamiento externo los solicita al usuario
        if(permisosLeerExterna == PackageManager.PERMISSION_DENIED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
        }
    }

    //Recupera todas las canciones que tiene el dispositivo almacenadas
    public void getListaCancion(){
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        Cursor musicCursor = musicResolver.query(musicUri,null,selection,null,null);

        //Mediante un cursor se recupera la información de cada canción
        if(musicCursor!=null && musicCursor.moveToFirst()){
            int idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int genreColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.GENRE);
            int duratColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            do {
                //Se inserta en la lista la canción
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String thisGenre = musicCursor.getString(genreColumn);
                String thisDurat = musicCursor.getString(duratColumn);
                listaCancion.add(new Cancion(thisId,thisTitle,thisArtist,thisGenre,thisDurat));
            }while (musicCursor.moveToNext());
        }
    }
    //Con este método se cambia entre reproducir o pausar


    //Con este método se para la reproducción
    public void Stop(View view){
    }
}