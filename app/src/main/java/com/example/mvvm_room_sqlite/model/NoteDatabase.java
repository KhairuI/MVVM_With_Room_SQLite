package com.example.mvvm_room_sqlite.model;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Note.class},version =1)
public abstract class NoteDatabase extends RoomDatabase {

   private static NoteDatabase instance;

   public abstract NoteDao noteDao();

   public static synchronized NoteDatabase getInstance(Context context){
      if(instance==null){
         instance= Room.databaseBuilder(context.getApplicationContext(),
                 NoteDatabase.class,"note_database")
                 .fallbackToDestructiveMigration()
                 .addCallback(roomCallBack)
                 .build();
      }
      return instance;
   }
   private static Callback roomCallBack= new Callback(){
      @Override
      public void onCreate(@NonNull SupportSQLiteDatabase db) {
         super.onCreate(db);
         new PopulateDBAsyncTask(instance).execute();
      }
   };

   private static class PopulateDBAsyncTask extends AsyncTask<Void,Void,Void>{

      private NoteDao noteDao;

      public PopulateDBAsyncTask(NoteDatabase database) {
         noteDao= database.noteDao();
      }

      @Override
      protected Void doInBackground(Void... voids) {

         noteDao.insert(new Note("title 1","description 1",1));
         noteDao.insert(new Note("title 2","description 2",2));
         noteDao.insert(new Note("title 3","description 3",3));
         return null;
      }
   }


}
