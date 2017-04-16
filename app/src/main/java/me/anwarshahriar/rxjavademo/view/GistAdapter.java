package me.anwarshahriar.rxjavademo.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import me.anwarshahriar.rxjavademo.model.Gist;

class GistAdapter extends RecyclerView.Adapter<GistViewHolder> {
  private List<Gist> gists;

  GistAdapter() {
    gists = new ArrayList<>();
  }

  void setData(List<Gist> data) {
    this.gists = data;
    notifyDataSetChanged();
  }

  @Override public GistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(android.R.layout.simple_list_item_1, parent, false);
    return new GistViewHolder(view);
  }

  @Override public void onBindViewHolder(GistViewHolder holder, int position) {
    Gist gist = gists.get(position);
    holder.bind(gist);
  }

  @Override public int getItemCount() {
    return gists.size();
  }
}

class GistViewHolder extends RecyclerView.ViewHolder {
  private TextView textGistName;

  GistViewHolder(View itemView) {
    super(itemView);
    textGistName = (TextView) itemView.findViewById(android.R.id.text1);
  }

  void bind(Gist data) {
    String gistName = data.getFiles().entrySet().iterator().next().getValue().getFileName();
    textGistName.setText(gistName);
  }
}