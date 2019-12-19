package com.takealookcat.project_demo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CatListAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList

    List<catitem> datas;
    Context context;
    LayoutInflater inflater;

    // ListViewAdapter의 생성자
    public CatListAdapter(List<catitem> data,Context context) {
        this.datas = data;
        this.context = context;
        this.inflater = (LayoutInflater) context. getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return datas.size();
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return datas.get(position) ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        final ImageView iconImageView = (ImageView) convertView.findViewById(R.id.imageView1);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.textView1);
        TextView descTextView = (TextView) convertView.findViewById(R.id.textView2);
        TextView email = (TextView) convertView.findViewById(R.id.textView3);
        //final ImageView profile = (ImageView) convertView.findViewById(R.id.imageView2);
        final CircleImageView profile = (CircleImageView) convertView.findViewById(R.id.imageView2);
        //라운딩입니다.
        GradientDrawable drawable=
                (GradientDrawable)  context.getResources().getDrawable(R.drawable.background_thema);
        iconImageView.setBackground(drawable);
        iconImageView.setClipToOutline(true);
/*
        Bitmap bmp=BitmapFactory.decodeResource(getResources(), R.drawable.img1); // 비트맵 이미지를 만든다.
        int width=(int)(getWindowManager().getDefaultDisplay().getWidth()); // 가로 사이즈 지정
        int height=(int)((width*불러올 이미지의 높이)/불러올 이미지의 너비); // 세로 사이즈 지정
        Bitmap resizedbitmap=Bitmap.createScaledBitmap(bmp, width, height, true); // 이미지 사이즈 조정
        imgview.setImageBitmap(resizedbitmap); // 이미지뷰에 조정한 이미지 넣기
*/
        profile.setBackground(drawable);
        profile.setClipToOutline(true);





        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        catitem cat = datas.get(position);

        StorageReference firebaseStorage = FirebaseStorage.getInstance().getReference();
        StorageReference storageReference = firebaseStorage.child("cat/"+cat.file);
        String useremail = cat.email;
        StorageReference storageReference2 = firebaseStorage.child("user/"+useremail);

        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    // Glide 이용하여 이미지뷰에 로딩

                    Glide.with(context)
                            .load(task.getResult())
                            .into(iconImageView);


                    //Glide.with(context).load(task.getResult()).apply(new RequestOptions().circleCrop()).into(iconImageView);
                } else {
                    // URL을 가져오지 못하면 토스트 메세지
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        storageReference2.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    // Glide 이용하여 이미지뷰에 로딩

                    Glide.with(context)
                            .load(task.getResult())
                            .into(profile);


                    //Glide.with(context).load(task.getResult()).apply(new RequestOptions().circleCrop()).into(iconImageView);
                } else {
                    // URL을 가져오지 못하면 토스트 메세지
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 아이템 내 각 위젯에 데이터 반영
        //iconImageView.setImageDrawable(listViewItem.getIcon());

        titleTextView.setText(cat.content);
        descTextView.setText(cat.info);
        email.setText(cat.email);
        return convertView;
    }

}
