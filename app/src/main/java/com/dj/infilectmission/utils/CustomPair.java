package com.dj.infilectmission.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class CustomPair implements Parcelable {

	private String first;
	private String  second;

	public CustomPair(String url, String title){

		first = url;
		second = title;

	}


	public String getUrl(){

		return first;
	}


	public String getTitle(){

		return second;
	}



	protected CustomPair(Parcel in) {
		first = in.readString();
		second = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(first);
		dest.writeString(second);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<CustomPair> CREATOR = new Parcelable.Creator<CustomPair>() {
		@Override
		public CustomPair createFromParcel(Parcel in) {
			return new CustomPair(in);
		}

		@Override
		public CustomPair[] newArray(int size) {
			return new CustomPair[size];
		}
	};
}
