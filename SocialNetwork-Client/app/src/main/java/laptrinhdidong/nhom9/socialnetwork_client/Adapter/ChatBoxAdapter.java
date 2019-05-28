package laptrinhdidong.nhom9.socialnetwork_client.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import laptrinhdidong.nhom9.socialnetwork_client.Model.Message;
import laptrinhdidong.nhom9.socialnetwork_client.R;

public class ChatBoxAdapter extends RecyclerView.Adapter<ChatBoxAdapter.MyViewHolder> {
    private List<Message> MessageList;

    public  class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nickname;
        public TextView message;

        public MyViewHolder(View view) {
            super(view);
            nickname = (TextView) view.findViewById(R.id.nickname);
            message = (TextView) view.findViewById(R.id.message);
        }
    }

    // in this adaper constructor we add the list of messages as a parameter so that
    // we will passe  it when making an instance of the adapter object in our activity
    public ChatBoxAdapter(List<Message>MessagesList) {
        this.MessageList = MessagesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item, viewGroup, false);
        return new ChatBoxAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        //binding the data from our ArrayList of object to the item.xml using the viewholder
        Message m = MessageList.get(i);
        myViewHolder.nickname.setText(m.getNickname());
        myViewHolder.message.setText(m.getMessage() );
    }

    @Override
    public int getItemCount() {
        return MessageList.size();
    }


}
