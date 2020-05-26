package com.example.mvvm_room_sqlite.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.mvvm_room_sqlite.R;

public class NewNodeActivity extends AppCompatActivity {

    public static final String extra_id = "com.example.mvvm_room.view.extra_id";
    public static final String extra_title = "com.example.mvvm_room.view.extra_title";
    public static final String extra_description = "com.example.mvvm_room.view.extra_description";
    public static final String extra_priority = "com.example.mvvm_room.view.extra_priority";

    private AppCompatEditText titleEditText,descriptionEditText;
    private AppCompatButton saveButton;
    private NumberPicker numberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_node);

        titleEditText= findViewById(R.id.newTitleId);
        descriptionEditText= findViewById(R.id.newDescriptionId);
        saveButton= findViewById(R.id.saveButtonId);
        numberPicker= findViewById(R.id.priorityId);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(20);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        //get data from mainActivity and set EditText.....
        Intent intent = getIntent();
        if (intent.hasExtra(extra_id)){
            setTitle("Edit Note");
            titleEditText.setText(intent.getStringExtra(extra_title));
            descriptionEditText.setText(intent.getStringExtra(extra_description));
            numberPicker.setValue(intent.getIntExtra(extra_priority,1));
        }
        else {
            setTitle("Add Note");
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveNote();
            }
        });
    }

    private void saveNote() {

        String title= titleEditText.getEditableText().toString();
        String description= descriptionEditText.getEditableText().toString();
        int priority= numberPicker.getValue();

        if(title.trim().isEmpty() || description.trim().isEmpty()){
            Toast.makeText(this, "Insert Title and Description", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent= new Intent();
        intent.putExtra(extra_title,title);
        intent.putExtra(extra_description,description);
        intent.putExtra(extra_priority,priority);
        int id= getIntent().getIntExtra(extra_id,-1);
        if(id != -1){
            intent.putExtra(extra_id,id);
        }
        setResult(RESULT_OK,intent);
        finish();


    }
}
