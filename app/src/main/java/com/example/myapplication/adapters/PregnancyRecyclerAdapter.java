package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.PregnancyDbHandler;
import com.example.myapplication.models.Pregnancy;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PregnancyRecyclerAdapter extends RecyclerView.Adapter<PregnancyRecyclerAdapter.ViewHolder>
{
    private PregnancyDbHandler pregnancyDbHandler;
    private Context context;
    private ArrayList<Pregnancy> pregnancyArrayList;

    public PregnancyRecyclerAdapter(Context context, PregnancyDbHandler pregnancyDbHandler)
    {
        this.pregnancyDbHandler=pregnancyDbHandler;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View pregnancyRecyclerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pregnancy_recycler,parent,false);
        return new ViewHolder(pregnancyRecyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        for(Object object:pregnancyArrayList)
        {
            Pregnancy pregnancy = (Pregnancy) object;
            pregnancyArrayList.add(pregnancy);
        }
        Pregnancy aPregnancy =pregnancyArrayList.get(position);
        holder.pregnancyRecyclerBuckTag.setText(aPregnancy.getBuckTag());
        holder.pregnancyRecyclerCrossedDate.setText(aPregnancy.getCrossedDate().toString());
        holder.pregnancyRecyclerId.setText(aPregnancy.getId());
        holder.pregnancyRecyclerDoeTag.setText(aPregnancy.getDoeTag());
        holder.pregnancyRecyclerNumberOfDays.setText(aPregnancy.getNumberOfDays());

    }


    @Override
    public int getItemCount() {
        return pregnancyDbHandler.ObjectCount();
    }

    //::::::::::::VIEW HOLDER CLASS::::::::::
    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView pregnancyRecyclerBuckTag;
       public TextView pregnancyRecyclerDoeTag;
       public TextView pregnancyRecyclerCrossedDate;
      public TextView pregnancyRecyclerNumberOfDays;
      public TextView pregnancyRecyclerId;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pregnancyRecyclerBuckTag=itemView.findViewById(R.id.pregnancyRecyclerBuckTag);
            pregnancyRecyclerDoeTag=itemView.findViewById(R.id.pregnancyRecyclerDoeTag);
            pregnancyRecyclerCrossedDate=itemView.findViewById(R.id.pregnancyRecyclerCrossedDate);
            pregnancyRecyclerNumberOfDays=itemView.findViewById(R.id.pregnancyRecyclerNumberOfDays);
            pregnancyRecyclerId=itemView.findViewById(R.id.pregnancyRecyclerId);

        }


    }
}
