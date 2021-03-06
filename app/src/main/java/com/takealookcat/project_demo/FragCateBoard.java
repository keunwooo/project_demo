package com.takealookcat.project_demo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FragCateBoard extends Fragment {
    private FragCateDona.OnFragmentInteractionListener mListener;
    FirebaseDatabase database;
    DatabaseReference boardRef;

    ListView listView;
    BoardListAdapter adapter;
    List<BoardItem> board_list = new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        // 인플레이션이 가능하다, container 이쪽으로 붙여달라, fragment_main을
        final ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_frag_cate_board,container,false);

        // 1. 파이어베이스 연결 - DB Connection
        database = FirebaseDatabase.getInstance();

        // 2. CRUD 작업의 기준이 되는 노드를 레퍼런스로 가져온다.
        boardRef = database.getReference("board");

        // 3. 레퍼런스 기준으로 데이터베이스에 쿼리를 날리는데, 자동으로 쿼리가 된다.
        //    ( * 파이어 베이스가
        boardRef.addValueEventListener(postListener);

        // 4. 리스트뷰에 목록 세팅
        listView = (ListView)rootview.findViewById(R.id.listview);
        adapter = new BoardListAdapter(board_list, getActivity());
        listView.setAdapter(adapter);

        /*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // TODO :
                String content = board_list.get(position).getContent();
                String title = board_list.get(position).getTitle();
                String file = board_list.get(position).getFile();
                mListener.onFragmentInteraction_board(title, content , file);

            }
        }) ;
        */

        return rootview;            // 플레그먼트 화면으로 보여주게 된다.
    }

    // 5. 파이어베이스가 호출해주는 이벤트 리스너 콜백
    // ValueEventListener : 경로의 전체 내용에 대한 변경을 읽고 수신 대기합니다.
    ValueEventListener postListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // 위에 선언한 저장소인 list를 초기화하고
            board_list.clear();
            // donation 레퍼런스의 스냅샷을 가져와서 레퍼런스의 자식노드를 반복문을 통해 하나씩 꺼내서 처리.
            for( DataSnapshot snapshot : dataSnapshot.getChildren() ) {
                String key  = snapshot.getKey();
                BoardItem item = snapshot.getValue(BoardItem.class);
                item.key = key;
                board_list.add(0,item);
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w("MainActivity", "loadPost:onCancelled", databaseError.toException());
            // ...
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mListener = (FragCateDona.OnFragmentInteractionListener) context;
        }catch(ClassCastException e){
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

}
