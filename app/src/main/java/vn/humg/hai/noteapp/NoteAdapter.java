package vn.humg.hai.noteapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private List<Note> noteList;
    private OnNoteActionListener listener;

    public interface OnNoteActionListener {
        void onNoteClick(Note note);
        void onNotePinChanged(Note note);
        void onNoteDelete(Note note);
    }

    public NoteAdapter(List<Note> noteList, OnNoteActionListener listener) {
        this.noteList = noteList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.tvTitle.setText(note.getTitle());
        holder.tvContent.setText(note.getContent());
        
        // Hiển thị icon trạng thái
        holder.imgPin.setVisibility(note.isPinned() ? View.VISIBLE : View.GONE);
        holder.imgImportant.setVisibility(note.isImportant() ? View.VISIBLE : View.GONE);
        
        // Hiển thị màu nền
        holder.cardNote.setCardBackgroundColor(note.getColor());

        // Hiển thị ngày tháng
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM - HH:mm", Locale.getDefault());
        holder.tvDate.setText(sdf.format(new Date(note.getTimestamp())));

        holder.itemView.setOnClickListener(v -> listener.onNoteClick(note));

        holder.itemView.setOnLongClickListener(v -> {
            PopupMenu popup = new PopupMenu(v.getContext(), v);
            popup.getMenu().add(note.isPinned() ? "Bỏ ghim" : "Ghim lên đầu");
            popup.getMenu().add("Xóa");
            popup.setOnMenuItemClickListener(item -> {
                if (item.getTitle().equals("Xóa")) {
                    listener.onNoteDelete(note);
                } else {
                    listener.onNotePinChanged(note);
                }
                return true;
            });
            popup.show();
            return true;
        });
    }

    @Override
    public int getItemCount() { return noteList.size(); }

    public void updateList(List<Note> newList) {
        this.noteList = newList;
        notifyDataSetChanged();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent, tvDate;
        ImageView imgPin, imgImportant;
        MaterialCardView cardNote;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvDate = itemView.findViewById(R.id.tv_date);
            imgPin = itemView.findViewById(R.id.img_pin);
            imgImportant = itemView.findViewById(R.id.img_important);
            cardNote = itemView.findViewById(R.id.card_note);
        }
    }
}