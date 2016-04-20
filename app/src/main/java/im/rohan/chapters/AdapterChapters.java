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



public class AdapterChapters extends RecyclerView.Adapter<AdapterChapters.ViewHolderEvents> implements Filterable{


    Context context;
    private static List<ParseObject> listChapters = new ArrayList<ParseObject>();
    private static List<ParseObject> filteredListChapters = new ArrayList<ParseObject>();
    private LayoutInflater layoutInflater;
    public AdapterChapters(Context context){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }


    public void setListChapter(List<ParseObject> listChapters) {
        this.listChapters = listChapters;
    }

    @Override
    public ViewHolderEvents onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_clubs_chapters,parent,false);
        ViewHolderEvents viewHolderEvents = new ViewHolderEvents(view);

        return viewHolderEvents;
    }

    @Override
    public void onBindViewHolder(ViewHolderEvents holder, int position) {

        ParseObject currentEvent = listChapters.get(position);
        holder.textName.setText(currentEvent.getString("name"));
    }

    @Override
    public int getItemCount() {
        return listChapters.size();
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
        return new ChapterFilter(this,listChapters);
    }
    class ChapterFilter extends Filter {

        private final AdapterChapters adapter;
        private final List<ParseObject> originalList;
        private final List<ParseObject> filteredList;
        public ChapterFilter(AdapterChapters adapterChapters, List<ParseObject> originalList) {
            super();
            this.adapter = adapterChapters;
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
            adapter.listChapters.clear();
            adapter.listChapters.addAll((ArrayList<ParseObject>) results.values);
            adapter.notifyDataSetChanged();
        }
    }
}
