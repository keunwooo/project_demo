package com.takealookcat.project_demo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class FragBoardItem extends Fragment {
    public String key;
    public String title ;
    public String content ;
    public String file;
    public String email;
    public String type;
    /*
    //뒤로가기 구현
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //메인뷰 액티비티의 뒤로가기 callback 붙이기
        ((MainActivity)context).setOnKeyBackPressedListener(this);
    }

    @Override
    public void onBackKey() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new FragCateBoard())
                .addToBackStack(null)
                .commit();

    }
     */

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_frag_board_item, container, false);

        TextView ttitle = (TextView) v.findViewById(R.id.postTitle);
        TextView temail = (TextView) v.findViewById(R.id.nicknameInPost);
        TextView tcontent = (TextView) v.findViewById(R.id.contentsText);
        TextView boardTitle = (TextView) v.findViewById(R.id.boardTitle);


        final ImageView iconImageView = (ImageView) v.findViewById(R.id.contentsImg) ;
        iconImageView.setAdjustViewBounds(true);
        iconImageView.requestLayout();

        Bundle extra = this.getArguments();
        if(extra != null) {
            extra = getArguments();
            title = extra.getString("title");
            content = extra.getString("content");
            file = extra.getString("file");
            type = extra.getString("type");
            email= extra.getString("email");
            ttitle.setText(title);
            tcontent.setText(content);
            temail.setText(email);
        }

        StorageReference firebaseStorage = FirebaseStorage.getInstance().getReference();
        StorageReference storageReference = null;
        if(type.equals("cat")) {
            storageReference = firebaseStorage.child("cat/" + file);
            boardTitle.setText("고양이");
        }
        else if(type.equals("feed")){
            storageReference = firebaseStorage.child("feed/" + file);
            boardTitle.setText("급식소");
        }
        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    // Glide 이용하여 이미지뷰에 로딩
                    Glide.with(getActivity())
                            .load(task.getResult())
                            .into(iconImageView);
                } else {
                    // URL을 가져오지 못하면 토스트 메세지
                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }


}
