package com.example.mvvm_room_sqlite.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.mvvm_room_sqlite.R;
import com.example.mvvm_room_sqlite.adapter.NoteAdapter;
import com.example.mvvm_room_sqlite.model.Note;
import com.example.mvvm_room_sqlite.viewmodel.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int add_note_request=1;
    public static final int edit_note_request=2;

    private RecyclerView recyclerView;
    private FloatingActionButton addButton;
    private NoteViewModel noteViewModel;
    private NoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView= findViewById(R.id.mainActivityRecycleView);
        addButton= findViewById(R.id.addButtonId);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this,NewNodeActivity.class);

                startActivityForResult(intent,add_note_request);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter= new NoteAdapter();
        //final NoteAdapter adapter= new NoteAdapter();
        recyclerView.setAdapter(adapter);

        //noteViewModel= ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel= new ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getApplication())).get(NoteViewModel.class);

        noteViewModel.getAllNotes().observe(MainActivity.this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //adapter.setNotes(notes);
                adapter.submitList(notes);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adapter.getNotePosition(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Delete Success", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent= new Intent(MainActivity.this,NewNodeActivity.class);
                intent.putExtra(NewNodeActivity.extra_id,note.getId());
                intent.putExtra(NewNodeActivity.extra_title,note.getTitle());
                intent.putExtra(NewNodeActivity.extra_description,note.getDescription());
                intent.putExtra(NewNodeActivity.extra_priority,note.getPriority());
                startActivityForResult(intent,edit_note_request);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==add_note_request && resultCode==RESULT_OK){
            String title= data.getStringExtra(NewNodeActivity.extra_title);
            String description= data.getStringExtra(NewNodeActivity.extra_description);
            int priority= data.getIntExtra(NewNodeActivity.extra_priority,1);

            Note note= new Note(title,description,priority);
            noteViewModel.insert(note);
            Toast.makeText(this, "Save Success", Toast.LENGTH_SHORT).show();
        }
        else if(requestCode==edit_note_request && resultCode==RESULT_OK){

            int id= data.getIntExtra(NewNodeActivity.extra_id,-1);
            if(id==-1){
                Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show();
                return;
            }
            String title= data.getStringExtra(NewNodeActivity.extra_title);
            String description= data.getStringExtra(NewNodeActivity.extra_description);
            int priority= data.getIntExtra(NewNodeActivity.extra_priority,1);
            Note note= new Note(title,description,priority);
            note.setId(id);
            noteViewModel.update(note);
            Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();

        }
        else {
            Toast.makeText(this, "Save Failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.deleteAllId:
                noteViewModel.deleteAllNotes();
                Toast.makeText(this, "Delete All", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
