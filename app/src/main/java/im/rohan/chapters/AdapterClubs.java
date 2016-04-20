package im.rohan.chapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;



public class AdapterClubs extends RecyclerView.Adapter<AdapterClubs.ViewHolderEvents> implements Filterable{


    Context context;
    private static List<ParseObject> listClub = new ArrayList<ParseObject>();
    private static List<ParseObject> filteredListClub = new ArrayList<ParseObject>();
    private LayoutInflater layoutInflater;
    public AdapterClubs(Context context){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }


    public void setListClubChapter(List<ParseObject> listClub) {
        this.listClub = listClub;
    }

    @Override
    public ViewHolderEvents onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_clubs_chapters,parent,false);
        ViewHolderEvents viewHolderEvents = new ViewHolderEvents(view);

        return viewHolderEvents;
    }

    @Override
    public void onBindViewHolder(ViewHolderEvents holder, int position) {

        ParseObject currentEvent = listClub.get(position);
        holder.textName.setText(currentEvent.getString("name"));
    }

    @Override
    public int getItemCount() {
        return listClub.size();
    }

    static class ViewHolderEvents extends RecyclerView.ViewHolder{


        private TextView textName;

        public ViewHolderEvents(View itemView) {
            super(itemView);
            textName = (TextView) itemView.findViewById(R.id.textName);
        }
    }
    @Override
    public Filter getFilter() {
        return new ClubFilter(this,listClub);
    }
    class ClubFilter extends Filter {

        private final AdapterClubs adapter;
        private final List<ParseObject> originalList;
        private final List<ParseObject> filteredList;
        public ClubFilter(AdapterClubs adapterClubsChapters, List<ParseObject> originalList) {
            super();
            this.adapter = adapterClubsChapters;
            this.originalList = new ArrayList<ParseObject>();
            this.originalList.addAll(originalList);
            this.filteredList = new ArrayList<ParseObject>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            //Here you have to implement filtering way
            filteredList.clear();
            final FilterResults results = new FilterResults();
            Log.v("Events: ", "" + constraint+ "\n");
            if (constraint.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();

                for(int i=0;i<originalList.size();i++){
                    if(originalList.get(i).getString("name").toLowerCase().contains(filterPattern)){
                        filteredList.add(originalList.get(i));
                        Log.v("Events: ", "" + originalList.get(i).getString("name").contains(constraint) + "\n");
                    }

                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.listClub.clear();
            adapter.listClub.addAll((ArrayList<ParseObject>) results.values);
            adapter.notifyDataSetChanged();
        }
    }
}
