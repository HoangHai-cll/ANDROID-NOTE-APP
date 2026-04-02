package vn.humg.hai.mynoteappone.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.test.services.storage.file.PropertyFile;

import org.intellij.lang.annotations.Pattern;

import java.io.Serializable;

import kotlin.time.ExperimentalTime;

@Entity(tableName = "notes")
public class Notes implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int ID = 0 ;
    @ColumnInfo(name = "title")
    public String title ="";
    @ColumnInfo(name = "notes")
    public String notes =  "";

    @ColumnInfo(name = "date")
    public String date = "";
    @ColumnInfo(name = "pinned")
    public boolean pinned = false;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }
}
