package im.rohan.chapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Rohan on 24-10-2015.
 */
public class AdapterEvents extends RecyclerView.Adapter<AdapterEvents.ViewHolderEvents>{


    Context context;
    private static List<ParseObject> listEvents = new ArrayList<ParseObject>();
    private LayoutInflater layoutInflater;
    public AdapterEvents(Context context){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setListEvents(List<ParseObject> listEvents) {
        this.listEvents = listEvents;
    }

    @Override
    public ViewHolderEvents onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_event,parent,false);
        ViewHolderEvents viewHolderEvents = new ViewHolderEvents(view);

        return viewHolderEvents;
    }

    @Override
    public void onBindViewHolder(ViewHolderEvents holder, int position) {

        ParseObject currentEvent = listEvents.get(position);
        holder.eventName.setText(currentEvent.getString("name"));
        holder.eventTime.setText(currentEvent.getString("time"));
    }

    @Override
    public int getItemCount() {
        return listEvents.size();
    }

    static class ViewHolderEvents extends RecyclerView.ViewHolder{


        private TextView eventName;
        private TextView eventTime;

        public ViewHolderEvents(View itemView) {
            super(itemView);
            eventName = (TextView) itemView.findViewById(R.id.textEvent);
            eventTime = (TextView) itemView.findViewById(R.id.textTime);
        }
    }

}
