package adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartnotes.R;

import java.util.List;

import activites.NoteEditActivity;
import data.Data;
import data.SharedPrefsData;
import models.Note;

import static android.content.Context.MODE_PRIVATE;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private Context mContext;
    private Data data;

    public RecyclerViewAdapter(Context mContext, List<Note> notes) {
        this.mContext = mContext;
        this.data = SharedPrefsData.getInstance(mContext.getSharedPreferences("activites", MODE_PRIVATE));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");

        Note note = data.getNotes().get(position);
        if (note == null) return;

        holder.getParentLayout().setTag(note.getId());
        holder.getNoteTitle().setText(note.getTitle());
        holder.getParentLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on "+ note.getTitle());
                Intent intent = new Intent(mContext, NoteEditActivity.class);
                intent.putExtra(NoteEditActivity.EXTRA_KEY, note.getId());
                mContext.startActivity(intent);
            }
        });
        holder.getParentLayout().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.d(TAG, "onLongClick: clicked on "+note.getTitle());
                showDeletionDialog(view, position);
                return true;
            }
        });

    }



    private void showDeletionDialog(View view, int position) {
        new AlertDialog.Builder(mContext)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(R.string.delete_dialog_title)
                .setMessage(R.string.delete_dialog_question)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(TAG, "onClick: yes clicked "+position);
                        int noteId = (Integer) view.getTag();
                        data.deleteNote(noteId);
                        RecyclerViewAdapter.this.notifyItemRemoved(position);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(TAG, "onClick: no clicked");
                    }
                })
                .show();
    }


    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: "+ data.getNotes().size());
        return data.getNotes().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout parentLayout;
        private TextView noteTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.noteTitle);
            parentLayout = itemView.findViewById(R.id.noteView);
        }

        public TextView getNoteTitle() {
            return noteTitle;
        }

        public void setNoteTitle(TextView noteTitle) {
            this.noteTitle = noteTitle;
        }

        public ConstraintLayout getParentLayout() {
            return parentLayout;
        }

        public void setParentLayout(ConstraintLayout parentLayout) {
            this.parentLayout = parentLayout;
        }
    }



}
