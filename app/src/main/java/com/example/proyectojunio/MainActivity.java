package com.example.proyectojunio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    Button play_pause, repeat;
    ImageView iv;

    int repetir = 2, posicion = 0;

    private ArrayList<Cancion> listaCancion;
    private ListView vistaCancion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play_pause = (Button)findViewById(R.id.boton_play);
        repeat = (Button)findViewById(R.id.boton_repeat);
        iv = (ImageView)findViewById(R.id.reproductor_default);
        vistaCancion = (ListView)findViewById(R.id.song_list);

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

    //Recupera todas las canciones que tiene el dispositivo almacenadas
    public void getListaCancion(){
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri,null,null,null,null);

        //Mediante un cursor se recupera la información de cada canción
        if(musicCursor!=null && musicCursor.moveToFirst()){
            int titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int genreColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.GENRE);
            do {
                //Se inserta en la lista la canción
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String thisGenre = musicCursor.getString(genreColumn);
                listaCancion.add(new Cancion(thisId,thisTitle,thisArtist,thisGenre));
            }while (musicCursor.moveToNext());
        }

    }
    //Con este método se cambia entre reproducir o pausar
    public void PlayPause (View view){

        if (true){
            //En caso de estar reproduciendo una canción, se pausará
            //pause
            play_pause.setBackgroundResource(R.drawable.img_play);
            Toast.makeText(this, "Pausa", Toast.LENGTH_SHORT).show();

        }else{
            //En caso de no estar reproduciendo una canción, la reproduce
            //play
            play_pause.setBackgroundResource(R.drawable.img_pause);

        }
    }

    //Con este método se para la reproducción
    public void Stop(View view){
    }
}