package com.gb.calculator;

import android.os.Parcel;
import android.os.Parcelable;

public class Display implements Parcelable {

    private String display = "";

    public Display() { }

    protected Display(Parcel in) {
        display = in.readString();
    }

    public static final Creator<Display> CREATOR = new Creator<Display>() {
        @Override
        public Display createFromParcel(Parcel in) {
            return new Display(in);
        }

        @Override
        public Display[] newArray(int size) {
            return new Display[size];
        }
    };

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(display);
    }
}
