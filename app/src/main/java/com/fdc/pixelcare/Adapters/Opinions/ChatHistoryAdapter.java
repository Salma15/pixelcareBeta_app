package com.fdc.pixelcare.Adapters.Opinions;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fdc.pixelcare.DataModel.ChatHistory;
import com.fdc.pixelcare.R;
import com.fdc.pixelcare.Views.CustomTextView;
import com.fdc.pixelcare.Views.CustomTextViewSemiBold;

import java.util.List;

public class ChatHistoryAdapter extends RecyclerView.Adapter<ChatHistoryAdapter.MyViewHolder> {

    private List<ChatHistory> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CustomTextViewSemiBold chat_date, chat_label;
        public CustomTextView chat_message;

        public MyViewHolder(View view) {
            super(view);
            chat_date = (CustomTextViewSemiBold) view.findViewById(R.id.chat_date);
            chat_message = (CustomTextView) view.findViewById(R.id.chat_message);
            chat_label = (CustomTextViewSemiBold) view.findViewById(R.id.chat_label);
        }
    }


    public ChatHistoryAdapter(List<ChatHistory> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ChatHistory chats = moviesList.get(position);
        holder.chat_label.setText("Response: ");
        holder.chat_message.setText(chats.getChatMessages());
        holder.chat_date.setText(chats.getChatTime());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
