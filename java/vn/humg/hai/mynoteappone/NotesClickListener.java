package vn.humg.hai.mynoteappone;

import androidx.cardview.widget.CardView;

import vn.humg.hai.mynoteappone.models.Notes;

public interface NotesClickListener {
    void onClick(Notes notes);
    void onLongClick(Notes notes , CardView cardView);

}
