package adapters;

import android.content.Context;
import android.content.DialogInterface;
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

import models.Note;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private Context mContext;
    private List<Note> notes;
    private View.OnClickListener onNoteDeleteListener;
    private View.OnClickListener onNoteEditListener;

    public RecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
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

        Note note = notes.get(position);
        if (note == null) return;

        holder.getParentLayout().setTag(note.getId());
        holder.getNoteTitle().setText(note.getTitle());
        holder.getParentLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on "+ note.getTitle());
                onNoteEditListener.onClick(view);
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
                        notes.remove(position);
                        RecyclerViewAdapter.this.notifyItemRemoved(position);
                        onNoteDeleteListener.onClick(view);
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
        if (notes == null) {
            return 0;
        }
        return notes.size();
    }

    public void setOnNoteDeleteListener(View.OnClickListener onNoteDeleteListener) {
        this.onNoteDeleteListener = onNoteDeleteListener;
    }

    public void setOnNoteEditListener(View.OnClickListener onNoteEditListener) {
        this.onNoteEditListener = onNoteEditListener;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
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
