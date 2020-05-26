package com.example.mvvm_room_sqlite.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mvvm_room_sqlite.R;
import com.example.mvvm_room_sqlite.model.Note;

public class NoteAdapter extends ListAdapter<Note,NoteAdapter.ViewHolder> {

    //private List<Note> notes= new ArrayList<>();
    private OnItemClickListener listener;

    public NoteAdapter() {
        super(diff_callback);
    }

    private static final DiffUtil.ItemCallback<Note> diff_callback= new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            //it's return true when the two item(oldItem,newItem) are same....
            return oldItem.getId()==newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) && oldItem.getDescription().equals(newItem.getDescription())
                    && oldItem.getPriority() == newItem.getPriority();
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       // Note currentNote= notes.get(position);
        Note currentNote= getItem(position);
        holder.titleTextView.setText(currentNote.getTitle());
        holder.descriptionTextView.setText(currentNote.getDescription());
        holder.priorityTextView.setText(String.valueOf(currentNote.getPriority()));

    }

  /*  @Override
    public int getItemCount() {
        return notes.size();
    }*/

   /* //every time data updated
    public void setNotes(List<Note> notes){
        this.notes= notes;
        notifyDataSetChanged();
    }*/

    //this method return the note position......
    public Note getNotePosition(int position){
       // return notes.get(position);
        return getItem(position);
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        private AppCompatTextView titleTextView;
        private AppCompatTextView descriptionTextView;
        private AppCompatTextView priorityTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            titleTextView= itemView.findViewById(R.id.titleId);
            descriptionTextView= itemView.findViewById(R.id.descriptionId);
            priorityTextView= itemView.findViewById(R.id.priorityId);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position= getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(getItem(position));
                    }

                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(Note note);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener= listener;
    }

}
